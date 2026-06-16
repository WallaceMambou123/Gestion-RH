package com.example.gestionrh.service;

import com.example.gestionrh.model.FichePaie;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfService {

    public byte[] genererFichePaiePdf(FichePaie fp) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // En-tête de l'entreprise
        document.add(new Paragraph("ENTREPRISE SA")
                .setFontSize(20).setBold().setFontColor(ColorConstants.BLUE));
        document.add(new Paragraph("Gestion des Ressources Humaines")
                .setFontSize(10).setItalic());
        document.add(new Paragraph("\n"));

        // Titre
        document.add(new Paragraph("BULLETIN DE PAIE - " + fp.getMois())
                .setTextAlignment(TextAlignment.CENTER).setFontSize(16).setUnderline());
        document.add(new Paragraph("\n"));

        // Bloc Informations Employé
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth();
        infoTable.addCell(new Cell().add(new Paragraph("Matricule : " + fp.getEmploye().getMatricule())).setBorder(null));
        infoTable.addCell(new Cell().add(new Paragraph("Période : " + fp.getMois())).setBorder(null));
        infoTable.addCell(new Cell().add(new Paragraph("Employé : " + fp.getEmploye().getPrenom() + " " + fp.getEmploye().getNom())).setBorder(null));
        infoTable.addCell(new Cell().add(new Paragraph("Poste : " + fp.getEmploye().getPoste())).setBorder(null));
        document.add(infoTable);
        document.add(new Paragraph("\n"));

        // Tableau des calculs
        Table table = new Table(UnitValue.createPercentArray(new float[]{60, 40})).useAllAvailableWidth();
        
        // Headers
        table.addHeaderCell(new Cell().add(new Paragraph("Désignation")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Montant (FCFA)")).setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.RIGHT));

        // Rows
        table.addCell("Salaire de base");
        table.addCell(new Cell().add(new Paragraph(String.format("%.0f", fp.getSalaireBase()))).setTextAlignment(TextAlignment.RIGHT));
        
        table.addCell("Heures Supplémentaires");
        table.addCell(new Cell().add(new Paragraph(String.format("%.0f", fp.getHeuresSup()))).setTextAlignment(TextAlignment.RIGHT));
        
        table.addCell("Primes");
        table.addCell(new Cell().add(new Paragraph(String.format("%.0f", fp.getPrimes()))).setTextAlignment(TextAlignment.RIGHT));
        
        table.addCell(new Cell().add(new Paragraph("SALAIRE BRUT")).setBold());
        table.addCell(new Cell().add(new Paragraph(String.format("%.0f", fp.getSalaireBrut()))).setBold().setTextAlignment(TextAlignment.RIGHT));

        table.addCell(new Cell().add(new Paragraph("Retenues (CNPS / IRPP / Autres)")).setFontColor(ColorConstants.RED));
        table.addCell(new Cell().add(new Paragraph(String.format("- %.0f", fp.getRetenues()))).setFontColor(ColorConstants.RED).setTextAlignment(TextAlignment.RIGHT));

        table.addCell(new Cell().add(new Paragraph("NET À PAYER")).setBold().setFontSize(14).setBackgroundColor(ColorConstants.CYAN));
        table.addCell(new Cell().add(new Paragraph(String.format("%.0f", fp.getSalaireNet()))).setBold().setFontSize(14).setBackgroundColor(ColorConstants.CYAN).setTextAlignment(TextAlignment.RIGHT));

        document.add(table);

        // Pied de page
        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("Fait le : " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        document.add(new Paragraph("Signature Employeur")
                .setTextAlignment(TextAlignment.RIGHT).setBold().setMarginRight(30));

        document.close();
        return baos.toByteArray();
    }
}
