package es.uji.apps.par.drawer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.butacas.DatosButaca;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.services.ButacasService;

@Service
public class MapaDrawer
{
    private static final String IMAGES_PATH = "/etc/uji/par/imagenes/";
    private static final String BUTACAS_PATH = "/etc/uji/par/butacas/";

    @InjectParam
    ButacasService butacasService;

    private BufferedImage butacaOcupada;
    private BufferedImage butacaReservada;
    private BufferedImage butacaOcupadaDiscapacitado;
    private BufferedImage butacaReservadaDiscapacitado;

    // Para cuando necesitamos saber el (x, y) que ocupa la butaca en la imagen
    private Map<String, DatosButaca> datosButacas;
    
    // Imágenes de distintas localizaciones
    private Map<String, BufferedImage> imagenes;

    public MapaDrawer() throws IOException
    {
        cargaImagenes();
        leeJson();
    }

    private String[] getLocalizacionesEnImagen(String localizacion)
    {
    	return Configuration.getLocalizacionesEnImagen(localizacion);
    }

    public ByteArrayOutputStream generaImagen(long idSesion, String codigoLocalizacion, boolean mostrarReservadas) throws IOException
    {
        BufferedImage img = dibujaButacas(idSesion, codigoLocalizacion, mostrarReservadas);

        return imagenToOutputStream(img);
    }

    private void leeJson() throws IOException
    {
        if (datosButacas == null)
        {
            datosButacas = new HashMap<String, DatosButaca>();

            for (String localizacion : Configuration.getImagenesFondo())
            {
            	for (String localizacionImagen: Configuration.getLocalizacionesEnImagen(localizacion))
            		loadJsonLocalizacion(localizacionImagen);
            }
        }
    }

    private void loadJsonLocalizacion(String localizacion) throws FileNotFoundException
    {
        List<DatosButaca> listaButacas = parseaJsonButacas(localizacion);

        for (DatosButaca datosButaca : listaButacas)
        {
            datosButacas.put(
                    String.format("%s_%d_%d", datosButaca.getLocalizacion(), datosButaca.getFila(),
                            datosButaca.getNumero()), datosButaca);
        }
    }

    private List<DatosButaca> parseaJsonButacas(String localizacion) throws FileNotFoundException
    {
        Gson gson = new Gson();
        Type fooType = new TypeToken<List<DatosButaca>>()
        {
        }.getType();
        
        InputStream inputStream = new FileInputStream(BUTACAS_PATH + "/" + localizacion + ".json");
        InputStreamReader jsonReader = new InputStreamReader(inputStream);

        List<DatosButaca> listaButacas = gson.fromJson(jsonReader, fooType);
        return listaButacas;
    }

    private ByteArrayOutputStream imagenToOutputStream(BufferedImage img) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bos);
        bos.close();
        return bos;
    }

    private BufferedImage dibujaButacas(long idSesion, String localizacionDeImagen, boolean mostrarReservadas)
    {
        BufferedImage imgButacas = imagenes.get(localizacionDeImagen);
        BufferedImage imgResult = new BufferedImage(imgButacas.getWidth(), imgButacas.getHeight(), imgButacas.getType());
        Graphics2D graphics = imgResult.createGraphics();
        graphics.drawImage(imgButacas, 0, 0, null);

        for (String localizacion : getLocalizacionesEnImagen(localizacionDeImagen))
        {
            List<ButacaDTO> butacas = butacasService.getButacas(idSesion, localizacion);

            for (ButacaDTO butacaDTO : butacas)
            {
                if (butacaDTO.getAnulada()==null || !butacaDTO.getAnulada())
                {
                    String key = String.format("%s_%s_%s", butacaDTO.getParLocalizacion().getCodigo(),
                            butacaDTO.getFila(), butacaDTO.getNumero());
                    DatosButaca butaca = datosButacas.get(key);

                    BufferedImage imagenOcupada;

                    if (esDiscapacitadoAnfiteatro(butaca))
                        continue;
                        
                    if (esDiscapacitado(butaca))
                    {
                        if (mostrarReservadas && esReserva(butacaDTO))
                            imagenOcupada = butacaReservadaDiscapacitado;
                        else
                            imagenOcupada = butacaOcupadaDiscapacitado;
                    }
                    else
                    {
                        if (mostrarReservadas && esReserva(butacaDTO))
                            imagenOcupada = butacaReservada;
                        else
                            imagenOcupada = butacaOcupada;
                    }

                    graphics.drawImage(imagenOcupada, butaca.getxIni(), butaca.getyIni(), null);
                }
            }
        }

        return imgResult;
    }

    private Boolean esReserva(ButacaDTO butacaDTO)
    {
        return butacaDTO.getParCompra().getReserva();
    }

	private boolean esDiscapacitadoAnfiteatro(DatosButaca butaca) {
		return butaca.getLocalizacion().startsWith("discapacitados3");
	}

	private boolean esDiscapacitado(DatosButaca butaca)
    {
        return butaca.getLocalizacion().startsWith("discapacitados");
    }

    private void cargaImagenes() throws IOException
    {
        if (imagenes == null)
        {
            imagenes = new HashMap<String, BufferedImage>();

            for (String localizacion : Configuration.getImagenesFondo())
            {
                loadImage(IMAGES_PATH, localizacion);
            }
        }

        if (butacaOcupada == null)
        {
            butacaOcupada = ImageIO.read(new File(IMAGES_PATH + "/ocupada.png"));
        }

        if (butacaOcupadaDiscapacitado == null)
        {
            butacaOcupadaDiscapacitado = ImageIO.read(new File(IMAGES_PATH + "/ocupadaDiscapacitado.png"));
        }
        
        if (butacaReservada == null)
        {
            butacaReservada = ImageIO.read(new File(IMAGES_PATH + "/reservada.png"));
        }
        
        if (butacaReservadaDiscapacitado == null)
        {
            butacaReservadaDiscapacitado = ImageIO.read(new File(IMAGES_PATH + "/reservadaDiscapacitado.png"));
        }
    }

    private void loadImage(String imagesPath, String localizacion) throws IOException
    {
        File f = new File(imagesPath + localizacion + ".png");
        imagenes.put(localizacion, ImageIO.read(f));
    }

}
