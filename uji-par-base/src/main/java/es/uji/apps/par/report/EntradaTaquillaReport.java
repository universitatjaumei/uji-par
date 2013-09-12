package es.uji.apps.par.report;

import java.io.File;
import java.io.OutputStream;
import java.util.Locale;

import es.uji.apps.fopreports.Report;
import es.uji.apps.fopreports.fop.Block;
import es.uji.apps.fopreports.fop.DisplayAlignType;
import es.uji.apps.fopreports.fop.ExternalGraphic;
import es.uji.apps.fopreports.fop.FontStyleType;
import es.uji.apps.fopreports.fop.PageBreakAfterType;
import es.uji.apps.fopreports.fop.TableCell;
import es.uji.apps.fopreports.fop.TextAlignType;
import es.uji.apps.fopreports.serialization.FopPDFSerializer;
import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.fopreports.serialization.ReportSerializer;
import es.uji.apps.fopreports.serialization.ReportSerializerInitException;
import es.uji.apps.fopreports.style.ReportStyle;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.report.components.BaseTable;
import es.uji.apps.par.report.components.EntradaReportStyle;
import es.uji.apps.par.utils.Utils;

public class EntradaTaquillaReport extends Report
{
    private static final String FONDO_BLANCO = "#FFFFFF";
    private static final String sizeTxtParanimf = "13pt";
    private static final String sizeTxtDireccionParanimf = "7pt";
    private static final String sizeTxtTituloEvento = "9pt";
    private static final String sizeTxtFechaHora = "7pt";

    private static FopPDFSerializer reportSerializer;

    private Locale locale;

    private String titulo;
    private String fecha;
    private String hora;
    private String horaApertura;
    private String zona;
    private String fila;
    private String numero;
    private String total;
    private String urlPortada;
    private String barcode;

    private EntradaTaquillaReport(ReportSerializer serializer, ReportStyle style, Locale locale)
            throws ReportSerializerInitException
    {
        super(serializer, style);

        this.locale = locale;
    }

    public void generaPaginaButaca(CompraDTO compra, ButacaDTO butaca)
    {
        this.setFila(butaca.getFila());
        this.setNumero(butaca.getNumero());
        this.setZona(butaca.getParLocalizacion().getNombreEs());
        this.setTotal(Utils.formatEuros(butaca.getPrecio()));
        this.setBarcode(compra.getUuid() + "-" + butaca.getId());

        creaSeccionEntrada();

        Block pageBreak = withNewBlock();
        pageBreak.setPageBreakAfter(PageBreakAfterType.ALWAYS);
    }

    private void creaSeccionEntrada()
    {
        Block entradaBlock = withNewBlock();

        EntradaReportStyle style = new EntradaReportStyle();
        BaseTable entradaTable = new BaseTable(style, 1/*2*/, "10.4cm"/*, "3cm"*/);

        entradaTable.withNewRow();

        TableCell cellIzquierda = entradaTable.withNewCell(createEntradaIzquierda());
        cellIzquierda.setPadding("0.1cm");
        cellIzquierda.setPaddingTop("0.0cm");
        cellIzquierda.setBackgroundColor(FONDO_BLANCO);

        /*TableCell cellDerecha = entradaTable.withNewCell(createEntradaDerecha());
        cellDerecha.setPadding("0.3cm");
        cellDerecha.setPaddingTop("0.3cm");
        cellDerecha.setBackgroundColor(FONDO_GRIS);
        cellDerecha.setBorderLeftWidth("0.03cm");
        cellDerecha.setBorderLeftColor("white");
        cellDerecha.setBorderLeftStyle(BorderStyleType.DOTTED);*/

        entradaBlock.getContent().add(entradaTable);
    }

    private Block createEntradaIzquierda()
    {
        Block block = new Block();

        block.getContent().add(createEntradaIzquierdaArriba());
        block.getContent().add(createEntradaIzquierdaCentro());
        block.getContent().add(createEntradaIzquierdaAbajo());

        return block;
    }

    private BaseTable createEntradaIzquierdaArriba()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "7.9cm", "2.5cm");

        table.withNewRow();
        table.withNewCell(createTextParanimf(EntradaTaquillaReport.sizeTxtParanimf));

        TableCell cell = table.withNewCell(logoUji());
        cell.setTextAlign(TextAlignType.CENTER);
        cell.setDisplayAlign(DisplayAlignType.CENTER);

        table.withNewRow();
        table.withNewCell(getBlockWithText(ResourceProperties.getProperty(locale, "entrada.universitat"), EntradaTaquillaReport.sizeTxtDireccionParanimf, true), "2");

        table.withNewRow();
        table.withNewCell(getBlockWithText(ResourceProperties.getProperty(locale, "entrada.direccion"), EntradaTaquillaReport.sizeTxtDireccionParanimf, true), "2");

        return table;
    }

    private Block getBlockWithText(String property, String size, boolean italic) {
    	Block text = new Block();
        text.getContent().add(property);
        text.setFontSize(size);
        if (italic)
        	text.setFontStyle(FontStyleType.ITALIC);
        return text;
	}

	private BaseTable createEntradaIzquierdaCentro()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 4, "5cm", "4.8cm", "0.2cm", "0.4cm");

        table.setMarginTop("0.2cm");

        table.withNewRow();
        table.withNewCell(createTitulo(), "4");

        table.withNewRow();
        TableCell cellEnmedio = table.withNewCell(createEntradaIzquierdaCentroDatos());
        cellEnmedio.setPadding("0cm");
        cellEnmedio.setBackgroundColor(FONDO_BLANCO);
        
        TableCell cellBarCode = table.withNewCell(createBarcode());
        cellBarCode.setTextAlign(TextAlignType.RIGHT);
        //cellBarCode.setBackgroundColor("black");
        cellBarCode.setPaddingLeft("0.2cm");

        // Espacio a la izquierda de bloque negro
        table.withNewCell("");

        TableCell cell = table.withNewCell("");
        cell.setPaddingLeft("0.2cm");
        cell.setBackgroundColor("black");
        
        table.withNewRow();
        TableCell celdaCodigoBarrasTxt = table.withNewCell(this.barcode, "2");
        celdaCodigoBarrasTxt.setTextAlign(TextAlignType.RIGHT);
        
        table.withNewCell("", "2");

        return table;
    }
	
	private Block createTitulo() {
		Block titulo = new Block();
        titulo.setFontSize(EntradaTaquillaReport.sizeTxtTituloEvento);
        titulo.setFontStyle(FontStyleType.ITALIC);
        titulo.getContent().add(this.titulo);
        titulo.setMarginBottom("0.2cm");

        return titulo;
	}

    private ExternalGraphic logoUji()
    {
        ExternalGraphic externalGraphic = new ExternalGraphic();
        externalGraphic.setSrc(new File("/etc/uji/par/imagenes/uji_logo.png").getAbsolutePath());
        externalGraphic.setMaxWidth("3cm");

        return externalGraphic;
    }

    private Block createEntradaIzquierdaCentroDatos()
    {
        Block block = new Block();
        String txtButaca = "";

        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "2.3cm", "2.3cm");

        table.withNewRow();
        table.withNewCell(getBlockWithText(ResourceProperties.getProperty(locale, "entrada.fecha"), EntradaTaquillaReport.sizeTxtFechaHora, true));
        table.withNewCell(getBlockWithText(ResourceProperties.getProperty(locale, "entrada.hora"), EntradaTaquillaReport.sizeTxtFechaHora, true));
        table.withNewRow();
        table.withNewCell(this.fecha);
        table.withNewCell(this.hora);
        
        
        table.withNewRow();
        TableCell celdaHora = table.withNewCell(getBlockWithText(ResourceProperties.getProperty(locale, "entrada.apertura"), 
        		EntradaTaquillaReport.sizeTxtFechaHora, true));
        celdaHora.setPaddingTop("0.2cm");
        table.withNewCell("");
        table.withNewRow();
        table.withNewCell(this.horaApertura);
        
        table.withNewCell("");
        
        if (this.fila != null && this.numero != null) {
            txtButaca = ": " + ResourceProperties.getProperty(locale, "entrada.butaca", this.fila, this.numero);
        }

        table.withNewRow();
        TableCell cell = table.withNewCell(getBlockWithText(ResourceProperties.getProperty(locale, "entrada.zona")+":", EntradaTaquillaReport.sizeTxtFechaHora, true), "2");
        cell.setPaddingTop("0.2cm");

        Block zona = new Block();
        zona.getContent().add(this.zona + txtButaca);
        zona.setFontSize("7pt");

        table.withNewRow();
        table.withNewCell(zona, "2");

        block.getContent().add(table);
        return block;
    }

    private BaseTable createEntradaIzquierdaAbajo()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "9.2cm", "1cm");

        table.setMarginTop("0.2cm");

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.cif"));
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.total"));

        table.withNewRow();
        table.withNewCell("");
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.importe", this.total));

        return table;
    }

    private Block createBarcode()
    {
        ExternalGraphic externalGraphic = new ExternalGraphic();
        externalGraphic.setSrc(Configuration.getUrlPublic() + "/rest/barcode/" + this.barcode);

        Block blockCodebar = new Block();
        blockCodebar.getContent().add(externalGraphic);

        return blockCodebar;
    }
    
    private Block createTextParanimf(String fontSize)
    {
        Block textParanimf = new Block();
        textParanimf.getContent().add(ResourceProperties.getProperty(locale, "entrada.paranimf"));
        textParanimf.setFontSize(fontSize);
        textParanimf.setFontStyle(FontStyleType.ITALIC);
        return textParanimf;
    }

    /*private Block createEntradaDerecha()
    {
        Block block = new Block();

        block.getContent().add(createEntradaDerechaArriba());
        block.getContent().add(createEntradaDerechaCentro());
        block.getContent().add(createEntradaDerechaAbajo());

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
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.universitat"), "2");

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.direccion"), "2");

        return table;
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

        table.withNewRow();
        TableCell cell = table.withNewCell(ResourceProperties.getProperty(locale, "entrada.zona"), "2");
        cell.setPaddingTop("0.2cm");

        Block zona = new Block();
        zona.getContent().add(this.zona);
        zona.setFontSize("12pt");

        table.withNewRow();
        TableCell zonaCell = table.withNewCell(zona, "2");

        if (this.fila != null && this.numero != null)
        {
            table.withNewRow();
            TableCell butacaCell = table.withNewCell(
                    ResourceProperties.getProperty(locale, "entrada.butaca", this.fila, this.numero), "2");
            butacaCell.setPaddingBottom("0.2cm");
        }
        else
        {
            zonaCell.setPaddingBottom("0.2cm");
        }

        return table;
    }

    private Table createEntradaDerechaAbajo()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "4.5cm", "1cm");

        table.withNewRow();
        table.withNewCell("");
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.total"));

        table.withNewRow();
        table.withNewCell("");
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.importe", this.total));

        return table;
    }

    public Block createHorizontalLine()
    {
        Leader line = new Leader();
        line.setColor(GRIS_OSCURO);
        line.setBorderAfterStyle(BorderStyleType.DASHED);
        line.setLeaderLengthOptimum("100%");

        Block b = new Block();
        b.getContent().add(line);

        return b;
    }*/

    private static void initStatics() throws ReportSerializerInitException
    {
        if (reportSerializer == null)
            reportSerializer = new FopPDFSerializer();
    }

    public static EntradaTaquillaReport create(Locale locale)
    {
        try
        {
            initStatics();
            EntradaReportStyle reportStyle = new EntradaReportStyle();
            reportStyle.setSimplePageMasterMarginRight("0.3cm");
            reportStyle.setSimplePageMasterMarginLeft("0.3cm");
            reportStyle.setSimplePageMasterMarginBottom("0.3cm");
            reportStyle.setSimplePageMasterMarginTop("0.3cm");
            reportStyle.setSimplePageMasterPageWidth("11cm");
            reportStyle.setSimplePageMasterPageHeight("8cm");
            reportStyle.setSimplePageMasterRegionBeforeExtent("0cm");
            reportStyle.setSimplePageMasterRegionAfterExtent("0cm");
            reportStyle.setSimplePageMasterRegionBodyMarginTop("0cm");
            reportStyle.setSimplePageMasterRegionBodyMarginBottom("0cm");

            return new EntradaTaquillaReport(reportSerializer, reportStyle, locale);
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

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getFecha()
    {
        return fecha;
    }

    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }

    public String getHora()
    {
        return hora;
    }

    public void setHora(String hora)
    {
        this.hora = hora;
    }

    public String getHoraApertura()
    {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura)
    {
        this.horaApertura = horaApertura;
    }

    public String getZona()
    {
        return zona;
    }

    public void setZona(String zona)
    {
        this.zona = zona;
    }

    public String getFila()
    {
        return fila;
    }

    public void setFila(String fila)
    {
        this.fila = fila;
    }

    public String getNumero()
    {
        return numero;
    }

    public void setNumero(String numero)
    {
        this.numero = numero;
    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }

    public String getUrlPortada()
    {
        return urlPortada;
    }

    public void setUrlPortada(String urlPortada)
    {
        this.urlPortada = urlPortada;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }
}
