package es.uji.apps.par.report;

import java.io.File;
import java.io.OutputStream;

import es.uji.apps.fopreports.Report;
import es.uji.apps.fopreports.fop.Block;
import es.uji.apps.fopreports.fop.BorderStyleType;
import es.uji.apps.fopreports.fop.DisplayAlignType;
import es.uji.apps.fopreports.fop.ExternalGraphic;
import es.uji.apps.fopreports.fop.FontStyleType;
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
import es.uji.apps.par.report.components.BaseTable;
import es.uji.apps.par.report.components.EntradaReportStyle;

public class EntradaReport extends Report
{
    private static final String FONDO_GRIS = "#EEEEEE";
    private static final String FONDO_BLANCO = "#FFFFFF";

    private static FopPDFSerializer reportSerializer;

    private Table secciones;
    private TableRow entrada;
    private TableRow condiciones;

    private EntradaReport(ReportSerializer serializer, ReportStyle style) throws ReportSerializerInitException
    {
        super(serializer, style);
        creaSecciones();
    }

    private void creaSecciones()
    {
        secciones = withNewTable();
        TableBody seccionesBody = new TableBody();
        secciones.getTableBody().add(seccionesBody);

        Block entradaBlock = createSeccion(seccionesBody);
        creaSeccionEntrada(entradaBlock);

        Block condicionesBlock = createSeccion(seccionesBody);
        condicionesBlock.getContent().add("Condiciones");

        Block publicidadBlock = createSeccion(seccionesBody);
        publicidadBlock.getContent().add("Publicidad");
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
        entradaTable
                .withNewCell(
                        "*Aquesta és una entrada vàlida i et permet l’accès directe al recinte. Sense cues ni esperes en taquilla.",
                        "2");

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
        table.withNewCell("Universitat Jaume I", "2");

        table.withNewRow();
        table.withNewCell("Avinguda Sos Baynat S/N. 12071 Castelló de la Plana", "2");

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
        externalGraphic.setSrc(new File("/tmp/peli.jpg").getAbsolutePath());
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
        titulo.getContent().add("El muerto y ser feliz");
        titulo.setMarginBottom("0.2cm");

        table.withNewRow();
        table.withNewCell(titulo, "3");

        table.withNewRow();
        table.withNewCell("Fecha");
        table.withNewCell("Hora");
        table.withNewCell("Apertura de puertas");

        table.withNewRow();
        table.withNewCell("19/04/2013");
        table.withNewCell("20:00");
        table.withNewCell("19:30");

        table.withNewRow();
        TableCell cell = table.withNewCell("Zona", "3");
        cell.setPaddingTop("0.2cm");

        Block zona = new Block();
        zona.getContent().add("PLATEA NIVEL 2");
        zona.setFontSize("12pt");
        
        table.withNewRow();
        table.withNewCell(zona, "3");

        table.withNewRow();
        table.withNewCell("F:10 B:15", "3");

        block.getContent().add(table);

        return block;
    }

    private BaseTable createEntradaIzquierdaAbajo()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "9.2cm", "1cm");
        
        table.setMarginTop("0.2cm");

        table.withNewRow();
        table.withNewCell("CIF XXXXX");
        table.withNewCell("TOTAL");

        table.withNewRow();
        table.withNewCell("113540612587569562354154114   5455665466874");
        table.withNewCell("0,00 €");

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
        table.withNewCell("Universitat Jaume I\nAvinguda Sos Baynat S/N. 12071 Castelló de la Plana", "2");

        return table;
    }

    private Block createTextParanimf(String fontSize)
    {
        Block textParanimf = new Block();
        textParanimf.getContent().add("Paranimf");
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
        titulo.getContent().add("El muerto y ser feliz");
        titulo.setMarginBottom("0.2cm");

        table.withNewRow();
        TableCell tituloCell = table.withNewCell(titulo, "2");
        tituloCell.setPaddingTop("0.2cm");  

        table.withNewRow();
        table.withNewCell("Fecha");
        table.withNewCell("Hora");
        
        table.withNewRow();
        table.withNewCell("19/04/2013");
        table.withNewCell("20:00");
        
        table.withNewRow();
        TableCell aperturaCell = table.withNewCell("Apertura de puertas", "2");
        aperturaCell.setPaddingTop("0.2cm");

        table.withNewRow();
        table.withNewCell("19:30", "2");

        table.withNewRow();
        TableCell cell = table.withNewCell("Zona", "2");
        cell.setPaddingTop("0.2cm");

        Block zona = new Block();
        zona.getContent().add("PLATEA NIVEL 2");
        zona.setFontSize("12pt");
        
        table.withNewRow();
        table.withNewCell(zona, "2");

        table.withNewRow();
        TableCell butacaCell = table.withNewCell("F:10 B:15", "2");
        butacaCell.setPaddingBottom("0.2cm");

        return table;
    }

    private Table createEntradaDerechaAbajo()
    {
        BaseTable table = new BaseTable(new EntradaReportStyle(), 2, "4.5cm", "1cm");

        table.withNewRow();
        table.withNewCell("");
        table.withNewCell("Total");

        table.withNewRow();
        table.withNewCell("");
        table.withNewCell("0,00 €");

        return table;
    }

    private Block createSeccion(TableBody seccionesBody)
    {
        TableRow seccionRow = new TableRow();
        seccionesBody.getTableRow().add(seccionRow);
        seccionRow.setMinHeight("7.5cm");

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

    public static EntradaReport create()
    {
        try
        {
            initStatics();

            return new EntradaReport(reportSerializer, new ReportStyle());
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
}
