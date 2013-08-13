package es.uji.apps.par.report;

import java.io.File;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.xml.utils.URI;

import es.uji.apps.fopreports.Report;
import es.uji.apps.fopreports.fop.Block;
import es.uji.apps.fopreports.fop.BorderStyleType;
import es.uji.apps.fopreports.fop.DisplayAlignType;
import es.uji.apps.fopreports.fop.ExternalGraphic;
import es.uji.apps.fopreports.fop.FontStyleType;
import es.uji.apps.fopreports.fop.PageBreakAfterType;
import es.uji.apps.fopreports.fop.Table;
import es.uji.apps.fopreports.fop.TableBody;
import es.uji.apps.fopreports.fop.TableCell;
import es.uji.apps.fopreports.fop.TableRow;
import es.uji.apps.fopreports.fop.TextAlignType;
import es.uji.apps.fopreports.serialization.FopPDFSerializer;
import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.fopreports.serialization.ReportSerializer;
import es.uji.apps.fopreports.serialization.ReportSerializerInitException;
import es.uji.apps.fopreports.style.ReportStyle;
import es.uji.apps.par.Utils;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.report.components.BaseTable;
import es.uji.apps.par.report.components.EntradaReportStyle;

public class EntradaReport extends Report
{
    private static final String FONDO_GRIS = "#EEEEEE";
    private static final String FONDO_BLANCO = "#FFFFFF";

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
    private String urlPublicidad;
    private String urlPortada;

    private EntradaReport(ReportSerializer serializer, ReportStyle style, Locale locale)
            throws ReportSerializerInitException
    {
        super(serializer, style);

        this.locale = locale;
    }

    public void generaPaginaButaca(ButacaDTO butaca)
    {
        this.setFila(butaca.getFila());
        this.setNumero(butaca.getNumero());
        this.setZona(butaca.getParLocalizacion().getNombreEs());
        this.setTotal(Utils.formatEuros(butaca.getPrecio()));
      
        Table secciones = withNewTable();
        TableBody seccionesBody = new TableBody();
        secciones.getTableBody().add(seccionesBody);

        Block entradaBlock = createSeccion(seccionesBody);
        creaSeccionEntrada(entradaBlock);

        Block condicionesBlock = createSeccion(seccionesBody);
        creaSeccionCondiciones(condicionesBlock);

        Block publicidadBlock = createSeccion(seccionesBody);
        creaSeccionPublicidad(publicidadBlock);
        
        Block pageBreak = withNewBlock();
        pageBreak.setPageBreakAfter(PageBreakAfterType.ALWAYS);
    }

    private void creaSeccionPublicidad(Block publicidadBlock)
    {
        ExternalGraphic externalGraphic = new ExternalGraphic();
        externalGraphic.setSrc(this.urlPublicidad);

        publicidadBlock.getContent().add(externalGraphic);
    }

    private void creaSeccionCondiciones(Block condicionesBlock)
    {
        condicionesBlock.setMarginTop("0.3cm");

        Block block = new Block();
        block.setFontSize("8pt");
        block.setColor("#666666");
        block.setFontWeight("bold");
        block.setMarginBottom("0.2em");
        block.getContent().add(ResourceProperties.getProperty(locale, "entrada.condiciones"));
        condicionesBlock.getContent().add(block);

        for (int i = 1; i <= 10; i++)
        {
            block = new Block();
            block.setFontSize("8pt");
            block.setColor("#666666");
            block.setMarginBottom("0.2em");
            block.getContent().add(ResourceProperties.getProperty(locale, String.format("entrada.condicion%d", i)));
            condicionesBlock.getContent().add(block);
        }
    }

    private void creaSeccionEntrada(Block entradaBlock)
    {
        EntradaReportStyle style = new EntradaReportStyle();
        BaseTable entradaTable = new BaseTable(style, 2, "11.8cm", "6.1cm");

        entradaTable.withNewRow();

        TableCell cellIzquierda = entradaTable.withNewCell(createEntradaIzquierda());
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
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "9cm", "2cm");

        table.withNewRow();
        table.withNewCell(createTextParanimf("32pt"));

        TableCell cell = table.withNewCell(logoUji());
        cell.setTextAlign(TextAlignType.CENTER);
        cell.setDisplayAlign(DisplayAlignType.CENTER);

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.universitat"), "2");

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
        externalGraphic.setSrc(this.urlPortada);
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

        table.withNewRow();
        TableCell cell = table.withNewCell(ResourceProperties.getProperty(locale, "entrada.zona"), "3");
        cell.setPaddingTop("0.2cm");

        Block zona = new Block();
        zona.getContent().add(this.zona);
        zona.setFontSize("12pt");

        table.withNewRow();
        table.withNewCell(zona, "3");

        table.withNewRow();
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.butaca", this.fila, this.numero), "3");

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
        table.withNewCell("113540612587569562354154114   5455665466874");
        table.withNewCell(ResourceProperties.getProperty(locale, "entrada.importe", this.total));

        table.withNewRow();
        TableCell cell = table.withNewCell("", "2");
        cell.setPaddingTop("0.2cm");
        cell.setMinHeight("0.4cm");
        cell.setBackgroundColor("black");

        return table;
    }

    private Block createEntradaDerecha()
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

    private Block createTextParanimf(String fontSize)
    {
        Block textParanimf = new Block();
        textParanimf.getContent().add(ResourceProperties.getProperty(locale, "entrada.paranimf"));
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

        table.withNewRow();
        TableCell cell = table.withNewCell(ResourceProperties.getProperty(locale, "entrada.zona"), "2");
        cell.setPaddingTop("0.2cm");

        Block zona = new Block();
        zona.getContent().add(this.zona);
        zona.setFontSize("12pt");

        table.withNewRow();
        table.withNewCell(zona, "2");

        table.withNewRow();
        TableCell butacaCell = table.withNewCell(
                ResourceProperties.getProperty(locale, "entrada.butaca", this.fila, this.numero), "2");
        butacaCell.setPaddingBottom("0.2cm");

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

    private Block createSeccion(TableBody seccionesBody)
    {
        TableRow seccionRow = new TableRow();
        seccionesBody.getTableRow().add(seccionRow);

        TableCell seccionCell = new TableCell();
        seccionRow.getTableCell().add(seccionCell);

        Block seccionBlock = new Block();
        seccionCell.getMarkerOrBlockOrBlockContainer().add(seccionBlock);

        return seccionBlock;
    }

    private static void initStatics() throws ReportSerializerInitException
    {
        if (reportSerializer == null)
            reportSerializer = new FopPDFSerializer();
    }

    public static EntradaReport create(Locale locale)
    {
        try
        {
            initStatics();

            return new EntradaReport(reportSerializer, new ReportStyle(), locale);
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

    public String getUrlPublicidad()
    {
        return urlPublicidad;
    }

    public void setUrlPublicidad(String urlPublicidad)
    {
        this.urlPublicidad = urlPublicidad;
    }

    public String getUrlPortada()
    {
        return urlPortada;
    }

    public void setUrlPortada(String urlPortada)
    {
        this.urlPortada = urlPortada;
    }

}
