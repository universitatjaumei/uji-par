package es.uji.apps.par.services;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BarcodeService
{
    public void generaBarcode(String text, OutputStream output) throws IOException
    {
        DataMatrixBean bean = new DataMatrixBean();

        final int dpi = 100;

        //makes the narrow bar width exactly one pixel
        bean.setModuleWidth(UnitConv.in2mm(3.0f / dpi));

        bean.doQuietZone(false);

        try
        {
            //Set up the canvas provider for monochrome PNG output 
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(output, "image/x-png", dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            //Generate the barcode
            bean.generateBarcode(canvas, text);

            //Signal end of generation
            canvas.finish();
        }
        finally
        {
            output.close();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-db.xml");

        BarcodeService service = ctx.getBean(BarcodeService.class);

        service.generaBarcode("d26e40ab-0c9f-4868-8797-6ce8c8381e6c-123456", new FileOutputStream("/tmp/codigo.png"));
    }
}