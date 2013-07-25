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

    @InjectParam
    ButacasService butacasService;

    private BufferedImage butacaOcupada;
    private Map<String, DatosButaca> datosButacas;
    private Map<String, BufferedImage> imagenes;

    public ByteArrayOutputStream generaImagen(String imagesPath, long idSesion, String codigoLocalizacion)
            throws IOException
    {
        cargaImagenes(imagesPath);
        leeJson();

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

    private BufferedImage dibujaButacas(long idSesion, String codigoLocalizacion)
    {
        BufferedImage imgButacas = imagenes.get(codigoLocalizacion);
        BufferedImage imgResult = new BufferedImage(imgButacas.getWidth(), imgButacas.getHeight(), imgButacas.getType());
        Graphics2D graphics = imgResult.createGraphics();
        graphics.drawImage(imgButacas, 0, 0, null);

        List<ButacaDTO> butacas = butacasService.getButacas(idSesion, codigoLocalizacion);

        for (ButacaDTO butacaDTO : butacas)
        {
            String key = String.format("%s_%s_%s", butacaDTO.getParLocalizacion().getCodigo(), butacaDTO.getFila(),
                    butacaDTO.getNumero());
            DatosButaca butaca = datosButacas.get(key);

            // Necesitamos saber el (x, y) que ocupa la butaca en la imagen
            graphics.drawImage(butacaOcupada, butaca.getxIni(), butaca.getyIni(), null);
        }

        return imgResult;
    }

    private void cargaImagenes(String imagesPath) throws IOException
    {
        if (imagenes == null)
        {
            imagenes = new HashMap<String, BufferedImage>();

            for (String localizacion : LOCALIZACIONES)
            {
                loadImage(imagesPath, localizacion);
            }
        }

        if (butacaOcupada == null)
        {
            File f = new File(imagesPath + "/img/ocupada.png");
            butacaOcupada = ImageIO.read(f);
        }
    }

    private void loadImage(String imagesPath, String localizacion) throws IOException
    {
        File f = new File(imagesPath + "/img/" + localizacion + ".png");
        imagenes.put(localizacion, ImageIO.read(f));
    }

}
