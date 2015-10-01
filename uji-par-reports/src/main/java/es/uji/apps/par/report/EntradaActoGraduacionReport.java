package es.uji.apps.par.report;

import es.uji.apps.fopreports.Report;
import es.uji.apps.fopreports.fop.*;
import es.uji.apps.fopreports.serialization.FopPDFSerializer;
import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.fopreports.serialization.ReportSerializer;
import es.uji.apps.fopreports.serialization.ReportSerializerInitException;
import es.uji.apps.fopreports.style.ReportStyle;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.report.components.BaseTable;
import es.uji.apps.par.report.components.EntradaReportStyle;
import es.uji.apps.par.sync.utils.SyncUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.OutputStream;
import java.util.Locale;

public class EntradaActoGraduacionReport extends Report implements EntradaReportOnlineInterface
{
    private static final Logger log = LoggerFactory.getLogger(EntradaActoGraduacionReport.class);

    private static final String GRIS_OSCURO = "#666666";
    private static final String FONDO_GRIS = "#EEEEEE";
    private static final String FONDO_BLANCO = "#FFFFFF";

    protected static FopPDFSerializer reportSerializer;

    private Locale locale;

    private String titulo;
    private String fecha;
    private String hora;
    private String horaApertura;
    protected String urlPublicidad;
    protected String urlPortada;
    private String barcode;

    public EntradaActoGraduacionReport() throws ReportSerializerInitException {
        super(reportSerializer, new EntradaReportStyle());
    }

    public EntradaActoGraduacionReport(ReportSerializer serializer, ReportStyle style, Locale locale)
            throws ReportSerializerInitException
    {
        super(serializer, style);

        this.locale = locale;
    }

    public void generaPaginaButaca(EntradaModelReport entrada, String urlPublic)
    {
        this.setBarcode(entrada.getBarcode());

        creaSeccionEntrada(urlPublic);
        add(creaHorizontalLine());

        creaSeccionCondiciones();
        creaSeccionPublicidad();

        Block pageBreak = withNewBlock();
        pageBreak.setPageBreakAfter(PageBreakAfterType.ALWAYS);
    }

    protected boolean existeImagen(String url) {
        try {
            byte[] imagen = SyncUtils.getImageFromUrl(url);
            if (imagen != null)
                return true;
        } catch (Exception e) {
            log.error("Error al comprobar si existe la imagen " + url, e);
            return false;
        }

        return false;
    }

    private void creaSeccionPublicidad()
    {
        if (existeImagen(this.urlPortada)) {
            Block publicidadBlock = withNewBlock();

            publicidadBlock.setMarginTop("0.3cm");

            ExternalGraphic externalGraphic = new ExternalGraphic();
            externalGraphic.setSrc(this.urlPortada);
            externalGraphic.setContentWidth("17.9cm");

            publicidadBlock.getContent().add(externalGraphic);
        }
    }

    private void creaSeccionCondiciones()
    {
        String puntos = "";
        Block block = new Block();
        Block condicionesBlock = withNewBlock();
        condicionesBlock.setMarginTop("0.3cm");

        block.setFontSize("8pt");
        block.setColor(GRIS_OSCURO);
        block.setFontWeight("bold");
        block.setMarginBottom("0.2em");
        block.getContent().add(ResourceProperties.getProperty(locale, "entrada.condiciones"));
        condicionesBlock.getContent().add(block);

        block = new Block();
        block.setLinefeedTreatment(LinefeedTreatmentType.PRESERVE);
        block.setFontSize("8pt");
        block.setColor(GRIS_OSCURO);
        block.setMarginBottom("0.2em");

        //TODO: Esto solo es para que lo vean ellos ahora
        /*for (int i = 1; i <= 6; i++)
            puntos += ResourceProperties.getProperty(locale, String.format("entrada.actograduacion.condicion%d", i));*/

        puntos += "1. L'entrada garanteix una localitat al recinte si accedeix durant l'obertura de portes en l'horari establert. Una vegada finalitze aquest horari l'entrada serà de lliure accés, segons l'aforament, fins que comence l'acte.\n"
                + "2. Es demana puntualitat. Una vegada començat l'acte, és decisió de l'organització permetre l'accés a la sala. En cas que es permeta accedir-hi, es farà en les condicions indicades pel personal de l'organització.\n"
                + "3. L'entrada s'ha de conservar completa i en bon estat.\n"
                + "4. L'organització es reserva el dret d'admissió. La persona portadora d'aquesta entrada es compromet a complir els requisits i condicions de seguretat del recinte. L'organització no es fa responsable de la utilització indeguda de les instal·lacions. L'organització es reserva el dret de denegar l'entrada al recinte al portador de qualsevol objecte o producte que l'organització considere perillós o que estiga prohibit per la normativa vigent.\n"
                + "5. El portador d'aquesta entrada es compromet a ser respectuós amb el personal del recinte, així com a desconnectar els telèfons mòbils i tot tipus d'alarmes abans d'accedir-hi, i a no menjar, beure o fumar en l'interior. També es prohibeix l'entrada d'animals, excepte dels gossos d'assistència.\n"
                + "6. L'organització no es fa responsable de les pèrdues, robatoris, deterioraments, danys o perjudicis causats al portador o als seus objectes personals durant l'estada al recinte.\n";

        block.getContent().add(puntos);
        condicionesBlock.getContent().add(block);
    }

    private void creaSeccionEntrada(String urlPublic)
    {
        Block entradaBlock = withNewBlock();

        EntradaReportStyle style = new EntradaReportStyle();
        BaseTable entradaTable = new BaseTable(style, 2, "11.8cm", "6.1cm");

        entradaTable.withNewRow();

        TableCell cellIzquierda = entradaTable.withNewCell(createEntradaIzquierda(urlPublic));
        cellIzquierda.setPadding("0.3cm");
        cellIzquierda.setPaddingTop("0.0cm");
        cellIzquierda.setBackgroundColor(FONDO_GRIS);

        TableCell cellDerecha = entradaTable.withNewCell(createEntradaDerecha());
        cellDerecha.setPadding("0.3cm");
        cellDerecha.setPaddingTop("0.3cm");
        cellDerecha.setBackgroundColor(FONDO_GRIS);
        cellDerecha.setBorderLeftWidth("0.03cm");
        cellDerecha.setBorderLeftColor("white");
        cellDerecha.setBorderLeftStyle(BorderStyleType.DOTTED);

        TableRow rowAbajo = entradaTable.withNewRow();
        rowAbajo.setBackgroundColor(FONDO_GRIS);
        entradaTable.withNewCell(ResourceProperties.getProperty(locale, "entrada.entradaValida"), "2");

        entradaBlock.getContent().add(entradaTable);
    }

    private Block createEntradaIzquierda(String urlPublic)
    {
        Block block = new Block();

        block.getContent().add(createEntradaIzquierdaArriba());
        block.getContent().add(createEntradaIzquierdaCentro());
        block.getContent().add(createEntradaIzquierdaAbajo(urlPublic));

        return block;
    }

    private BaseTable createEntradaIzquierdaArriba()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "9cm", "2cm");

        table.withNewRow();
        table.withNewCell(createTextParanimf("32pt"));

        TableCell cell = table.withNewCell(logoUji());
        cell.setTextAlign(TextAlignType.CENTER);
        cell.setDisplayAlign(DisplayAlignType.CENTER);

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.nombreEntidad"), "2");

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.direccion"), "2");

        return table;
    }

    private BaseTable createEntradaIzquierdaCentro()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 4, "3cm", "7.2cm", "0.2cm", "0.6cm");

        table.setMarginTop("0.2cm");

        table.withNewRow();

        TableCell cellIzquierda = table.withNewCell(createEntradaIzquierdaCentroFoto());
        cellIzquierda.setPadding("0.2cm");
        cellIzquierda.setBackgroundColor(FONDO_BLANCO);

        TableCell cellEnmedio = table.withNewCell(createEntradaIzquierdaCentroDatos());
        cellEnmedio.setPadding("0.2cm");
        cellEnmedio.setBackgroundColor(FONDO_BLANCO);

        // Espacio a la izquierda de bloque negro
        table.withNewCell("");

        TableCell cell = table.withNewCell("");
        cell.setPaddingLeft("0.2cm");
        cell.setBackgroundColor("black");

        return table;
    }

    private Block createEntradaIzquierdaCentroFoto()
    {
        Block block = new Block();

        ExternalGraphic externalGraphic = new ExternalGraphic();
        //TODO: Cambiar al etc para que la puedan cambiar
        //externalGraphic.setSrc("/etc/uji/par/imagenes/portada_acto_graduacion.jpg");
        externalGraphic.setSrc(this.getClass().getClassLoader().getResource("portada_acto_graduacion.jpg").getPath());
        externalGraphic.setContentWidth("2.5cm");

        externalGraphic.setMaxWidth("2.5cm");
        block.getContent().add(externalGraphic);
        return block;
    }

    private ExternalGraphic logoUji()
    {
        ExternalGraphic externalGraphic = new ExternalGraphic();
        externalGraphic.setSrc(new File("/etc/uji/par/imagenes/uji_logo.png").getAbsolutePath());
        externalGraphic.setMaxWidth("2cm");

        return externalGraphic;
    }

    private Block createEntradaIzquierdaCentroDatos()
    {
        Block block = new Block();

        BaseTable table = new BaseTable(new EntradaReportStyle(), 3, "2.5cm", "1.8cm", "2.1cm");

        Block titulo = new Block();
        titulo.setFontSize("14pt");
        titulo.setFontStyle(FontStyleType.ITALIC);
        titulo.getContent().add(this.titulo);
        titulo.setMarginBottom("0.2cm");

        table.withNewRow();
        table.withNewCell(titulo, "3");

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.fecha"));
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.hora"));
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.apertura"));

        table.withNewRow();
        table.withNewCell(this.fecha);
        table.withNewCell(this.hora);
        table.withNewCell(this.horaApertura);

        block.getContent().add(table);

        return block;
    }

    private BaseTable createEntradaIzquierdaAbajo(String urlPublic)
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 1, "11cm");

        table.setMarginTop("0.2cm");

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.cif"));

        table.withNewRow();
        table.withNewCell(this.barcode);

        table.withNewRow();
        table.withNewCell(createBarcode(urlPublic));

        return table;
    }

    private Block createBarcode(String urlPublic)
    {
        ExternalGraphic externalGraphic = new ExternalGraphic();
        externalGraphic.setSrc(urlPublic + "/rest/barcode/" + this.barcode);

        Block blockCodebar = new Block();
        blockCodebar.getContent().add(externalGraphic);

        return blockCodebar;
    }

    private Block createEntradaDerecha()
    {
        Block block = new Block();

        block.getContent().add(createEntradaDerechaArriba());
        block.getContent().add(createEntradaDerechaCentro());

        return block;
    }

    private BaseTable createEntradaDerechaArriba()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "3.5cm", "2cm");

        table.withNewRow();
        table.withNewCell(createTextParanimf("18pt"));

        TableCell cell = table.withNewCell(logoUji());
        cell.setTextAlign(TextAlignType.CENTER);
        cell.setDisplayAlign(DisplayAlignType.CENTER);

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.nombreEntidad"), "2");

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.direccion"), "2");

        return table;
    }

    private Block createTextParanimf(String fontSize)
    {
        Block textParanimf = new Block();
        textParanimf.getContent().add(ResourceProperties.getProperty(locale, "entrada.nombreLocalizacion"));
        textParanimf.setFontSize(fontSize);
        textParanimf.setFontStyle(FontStyleType.ITALIC);
        return textParanimf;
    }

    private BaseTable createEntradaDerechaCentro()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "2.5cm", "2.5cm");

        String margin = "0.3cm";
        table.setMarginTop("0.5cm");
        table.setMarginBottom(margin);
        table.setMarginLeft(margin);
        table.setMarginRight(margin);

        table.setBackgroundColor(FONDO_BLANCO);

        Block titulo = new Block();
        titulo.setFontSize("14pt");
        titulo.setFontStyle(FontStyleType.ITALIC);
        titulo.getContent().add(this.titulo);
        titulo.setMarginBottom("0.2cm");

        table.withNewRow();
        TableCell tituloCell = table.withNewCell(titulo, "2");
        tituloCell.setPaddingTop("0.2cm");

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.fecha"));
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.hora"));

        table.withNewRow();
        table.withNewCell(this.fecha);
        table.withNewCell(this.hora);

        table.withNewRow();
        TableCell aperturaCell = table.withNewCell(ResourceProperties.getProperty(locale, "entrada.apertura"), "2");
        aperturaCell.setPaddingTop("0.2cm");

        table.withNewRow();
        table.withNewCell(this.horaApertura, "2");

        return table;
    }

    public Block creaHorizontalLine()
    {
        Leader line = new Leader();
        line.setColor(GRIS_OSCURO);
        line.setBorderAfterStyle(BorderStyleType.DASHED);
        line.setLeaderLengthOptimum("100%");

        Block b = new Block();
        b.getContent().add(line);

        return b;
    }

    protected static void initStatics() throws ReportSerializerInitException
    {
        if (reportSerializer == null)
            reportSerializer = new FopPDFSerializer();
    }

    public EntradaReportOnlineInterface create(Locale locale)
    {
        try
        {
            initStatics();
            EntradaReportStyle estilo = new EntradaReportStyle();
            estilo.setSimplePageMasterMarginBottom("0cm");
            estilo.setSimplePageMasterRegionBodyMarginBottom("0cm");
            return new EntradaActoGraduacionReport(reportSerializer, estilo, locale);
        }
        catch (ReportSerializerInitException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void serialize(OutputStream output) throws ReportSerializationException
    {
        super.serialize(output);
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }


    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }

    public void setHora(String hora)
    {
        this.hora = hora;
    }

    public void setHoraApertura(String horaApertura)
    {
        this.horaApertura = horaApertura;
    }

    public void setUrlPublicidad(String urlPublicidad)
    {
        this.urlPublicidad = urlPublicidad;
    }

    public void setUrlPortada(String urlPortada)
    {
        this.urlPortada = urlPortada;
    }

    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }
}
