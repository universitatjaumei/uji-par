package es.uji.apps.par.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.Element;

public class ImageUtils
{

    public static void changeDpi(byte[] imagen, OutputStream output, float anchoCm) throws IOException
    {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagen));

        final String formatName = "jpg";

        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();)
        {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier
                    .createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported())
            {
                continue;
            }

            int dpi = calculateDpi(bufferedImage.getWidth(), anchoCm);

            Element tree = (Element) metadata.getAsTree("javax_imageio_jpeg_image_1.0");
            Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(dpi));
            jfif.setAttribute("Ydensity", Integer.toString(dpi));
            jfif.setAttribute("resUnits", "1"); // density is dots per inch     

            metadata.setFromTree("javax_imageio_jpeg_image_1.0", tree);

            final ImageOutputStream stream = ImageIO.createImageOutputStream(output);
            try
            {
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(bufferedImage, null, metadata), writeParam);
            }
            finally
            {
                stream.close();
            }
            break;
        }
    }

    private static int calculateDpi(int pixels, float cm)
    {
        float inches = cm / 2.54f;

        return Math.round(pixels / inches);
    }

}
