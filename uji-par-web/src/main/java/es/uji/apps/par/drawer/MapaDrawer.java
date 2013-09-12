package es.uji.apps.par.drawer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.services.ButacasService;

@Service
public class MapaDrawer
{
    private static final String[] LOCALIZACIONES = new String[] { "anfiteatro", "platea1", "platea2" };
    private static final String IMAGES_PATH = "/etc/uji/par/imagenes/";

    @InjectParam
    ButacasService butacasService;

    private BufferedImage butacaOcupada;
    private BufferedImage butacaOcupadaDiscapacitado;

    // Para cuando necesitamos saber el (x, y) que ocupa la butaca en la imagen
    private Map<String, DatosButaca> datosButacas;
    private Map<String, BufferedImage> imagenes;

    public MapaDrawer() throws IOException
    {
        cargaImagenes();
        leeJson();
    }

    private String[] getLocalizacionesEnImagen(String localizacion)
    {
        if (localizacion.equals("anfiteatro"))
            return new String[] { "anfiteatro", "discapacitados3" };
        else if (localizacion.equals("platea1"))
            return new String[] { "platea1", "discapacitados1" };
        else
            return new String[] { "platea2", "discapacitados2" };
    }

    public ByteArrayOutputStream generaImagen(long idSesion, String codigoLocalizacion) throws IOException
    {

        BufferedImage img = dibujaButacas(idSesion, codigoLocalizacion);

        return imagenToOutputStream(img);
    }

    private void leeJson() throws IOException
    {
        if (datosButacas == null)
        {
            datosButacas = new HashMap<String, DatosButaca>();

            for (String localizacion : LOCALIZACIONES)
            {
                loadJsonLocalizacion(localizacion);
            }
        }
    }

    private void loadJsonLocalizacion(String localizacion)
    {
        List<DatosButaca> listaButacas = parseaJsonButacas(localizacion);

        for (DatosButaca datosButaca : listaButacas)
        {
            datosButacas.put(
                    String.format("%s_%d_%d", datosButaca.getLocalizacion(), datosButaca.getFila(),
                            datosButaca.getNumero()), datosButaca);
        }
    }

    private List<DatosButaca> parseaJsonButacas(String localizacion)
    {
        Gson gson = new Gson();
        Type fooType = new TypeToken<List<DatosButaca>>()
        {
        }.getType();

        InputStreamReader jsonReader = new InputStreamReader(MapaDrawer.class.getResourceAsStream("/butacas/"
                + localizacion + ".json"));

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

    private BufferedImage dibujaButacas(long idSesion, String localizacionDeImagen)
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

                    if (!esDiscapacitadoAnfiteatro(butaca)) {
	                    if (esDiscapacitado(butaca))
	                        imagenOcupada = butacaOcupadaDiscapacitado;
	                    else
	                        imagenOcupada = butacaOcupada;
	
	                    graphics.drawImage(imagenOcupada, butaca.getxIni(), butaca.getyIni(), null);
                    }
                }
            }
        }

        return imgResult;
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

            for (String localizacion : LOCALIZACIONES)
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
    }

    private void loadImage(String imagesPath, String localizacion) throws IOException
    {
        File f = new File(imagesPath + localizacion + ".png");
        imagenes.put(localizacion, ImageIO.read(f));
    }

}
