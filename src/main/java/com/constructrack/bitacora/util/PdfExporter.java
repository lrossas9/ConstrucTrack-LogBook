package com.constructrack.bitacora.util;

import com.constructrack.bitacora.model.Bitacora;
import com.constructrack.bitacora.model.Proyecto;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfExporter {
    private PdfExporter() {
    }

    public static void exportProyecto(Proyecto proyecto, List<Bitacora> bitacoras, String outputPath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();
        document.add(new Paragraph("Bitacora del proyecto: " + proyecto.getNombre()));
        document.add(new Paragraph("Ubicacion: " + proyecto.getUbicacion()));
        document.add(new Paragraph("Contrato: " + proyecto.getContrato()));
        document.add(new Paragraph("Constructora: " + proyecto.getConstructora()));
        document.add(new Paragraph("Interventoria: " + proyecto.getInterventoria()));
        document.add(new Paragraph(" "));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Bitacora b : bitacoras) {
            document.add(new Paragraph("Fecha: " + (b.getFecha() != null ? b.getFecha().format(fmt) : "")));
            document.add(new Paragraph("Clima: " + nullSafe(b.getClima())));
            document.add(new Paragraph("Jornada: " + nullSafe(b.getJornada())));
            document.add(new Paragraph("Actividades: " + nullSafe(b.getActividades())));
            document.add(new Paragraph("Personal: " + nullSafe(b.getPersonal())));
            document.add(new Paragraph("Equipos: " + nullSafe(b.getEquipos())));
            document.add(new Paragraph("Materiales: " + nullSafe(b.getMateriales())));
            document.add(new Paragraph("% Avance: " + b.getPorcentajeAvance()));
            document.add(new Paragraph("Observaciones: " + nullSafe(b.getObservaciones())));
            document.add(new Paragraph("Firma: " + nullSafe(b.getFirma())));
            document.add(new Paragraph(" "));
        }
        document.close();
    }

    private static String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
