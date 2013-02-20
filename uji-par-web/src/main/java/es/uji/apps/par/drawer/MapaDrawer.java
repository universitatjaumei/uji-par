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
    @InjectParam
    ButacasService butacasService;

    private BufferedImage butacas;
    private BufferedImage butacaOcupada;
    private Map<String, DatosButaca> datosButacas;

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
            List<DatosButaca> listaButacas = parseaJsonButacas();

            datosButacas = new HashMap<String, DatosButaca>();
            for (DatosButaca datosButaca : listaButacas)
            {
                datosButacas.put(
                        String.format("%s_%d_%d", datosButaca.getLocalizacion(), datosButaca.getFila(),
                                datosButaca.getNumero()), datosButaca);
            }
        }
    }

    private List<DatosButaca> parseaJsonButacas()
    {
        Gson gson = new Gson();
        Type fooType = new TypeToken<List<DatosButaca>>()
        {
        }.getType();

        InputStreamReader jsonReader = new InputStreamReader(
                MapaDrawer.class.getResourceAsStream("/butacas/anfiteatro.json"));

        //            char[] buff = new char[1000];
        //            jsonReader.read(buff);

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
        BufferedImage img = new BufferedImage(butacas.getWidth(), butacas.getHeight(), butacas.getType());
        Graphics2D graphics = img.createGraphics();

        graphics.drawImage(butacas, 0, 0, null);

        List<ButacaDTO> butacas = butacasService.getButacas(idSesion, codigoLocalizacion);

        for (ButacaDTO butacaDTO : butacas)
        {
            String key = String.format("%s_%s_%s", butacaDTO.getParLocalizacion().getCodigo(), butacaDTO.getFila(),
                    butacaDTO.getNumero());
            DatosButaca butaca = datosButacas.get(key);

            graphics.drawImage(butacaOcupada, butaca.getxIni(), butaca.getyIni(), null);
        }

        return img;
    }

    private void cargaImagenes(String imagesPath) throws IOException
    {
        if (butacas == null)
        {
            File f = new File(imagesPath + "/img/butacas.png");
            butacas = ImageIO.read(f);
        }

        if (butacaOcupada == null)
        {
            File f = new File(imagesPath + "/img/ocupada.png");
            butacaOcupada = ImageIO.read(f);
        }
    }

}
