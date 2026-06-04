package com.example.gestionrh.service;

import com.example.gestionrh.model.ContratEmploye;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.FichePaie;
import com.example.gestionrh.repository.FichePaieRepository;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.borders.Border;
import java.math.BigDecimal;

public class PaieService {
    private final FichePaieRepository fichePaieRepository = new FichePaieRepository();

    /**
     * Calcule et sauvegarde une fiche de paie.
     * montantHS = heuresSup × (salaireBase / 173) × 1.25
     * salaireBrut = salaireBase + montantHS + primes
     * salaireNet  = salaireBrut - retenues
     */
    public FichePaie calculerFichePaie(Employe e, String mois,
                                        BigDecimal heuresSup,
                                        BigDecimal primes,
                                        BigDecimal retenues) {

        BigDecimal salaireBase    = e.getSalaireBase();
        BigDecimal tauxHoraire    = salaireBase.divide(new BigDecimal("173"), 4, RoundingMode.HALF_UP);
        BigDecimal montantHeuresSup = heuresSup.multiply(tauxHoraire)
                                               .multiply(new BigDecimal("1.25"))
                                               .setScale(2, RoundingMode.HALF_UP);
        BigDecimal salaireBrut    = salaireBase.add(montantHeuresSup).add(primes);
        BigDecimal salaireNet     = salaireBrut.subtract(retenues);

        FichePaie fp = FichePaie.builder()
                .employe(e)
                .mois(mois)
                .salaireBase(salaireBase)
                .heuresSup(heuresSup)
                .montantHeuresSup(montantHeuresSup)
                .primes(primes)
                .retenues(retenues)
                .salaireBrut(salaireBrut)
                .salaireNet(salaireNet)
                .build();

        fichePaieRepository.save(fp);
        logSMS(e.getTelephone(),
               "Fiche de paie " + mois + " disponible. Net : " + salaireNet.setScale(0, RoundingMode.HALF_UP) + " FCFA");
        return fp;
    }

    /**
     * Génère le PDF d'une fiche de paie (iText 8).
     */
    public byte[] genererFichePaiePDF(FichePaie fp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
            Document doc    = new Document(pdf);

            // En-tête
            doc.add(new Paragraph("BULLETIN DE PAIE")
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("Période : " + fp.getMois())
                    .setFontSize(12).setTextAlignment(TextAlignment.CENTER).setMarginBottom(10));

            // Informations employé
            doc.add(new Paragraph("Employé   : " + fp.getEmploye().getPrenom() + " " + fp.getEmploye().getNom()));
            doc.add(new Paragraph("Matricule : " + fp.getEmploye().getMatricule()));
            doc.add(new Paragraph("Poste     : " + fp.getEmploye().getPoste()).setMarginBottom(15));

            // Tableau des éléments de paie
            Table table = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                          .useAllAvailableWidth();

            addRow(table, "Salaire de Base",              fmt(fp.getSalaireBase()), false);
            addRow(table, "Heures Supplémentaires (" + fp.getHeuresSup() + "h)",
                                                           fmt(fp.getMontantHeuresSup()), false);
            addRow(table, "Primes",                       fmt(fp.getPrimes()), false);
            addRow(table, "SALAIRE BRUT",                 fmt(fp.getSalaireBrut()), true);
            addRow(table, "Retenues (Cotisations / IRPP)", "- " + fmt(fp.getRetenues()), false);
            addRow(table, "NET À PAYER",                  fmt(fp.getSalaireNet()), true);
            doc.add(table);

            doc.add(new Paragraph("\n\nDocument généré automatiquement par le système Gestion-RH.")
                    .setFontSize(8).setItalic().setTextAlignment(TextAlignment.RIGHT));
            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * Génère le PDF d'un contrat de travail (iText 8).
     */
    public byte[] genererContratPDF(ContratEmploye c) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
            Document doc    = new Document(pdf, PageSize.A4);
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // En-tête
            doc.add(new Paragraph("CONTRAT DE TRAVAIL")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("Entreprise SA — RH Manager")
                    .setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            
            doc.add(new LineSeparator(new SolidLine()));
            doc.add(new Paragraph("\n"));

            // Bloc employé
            doc.add(new Paragraph("INFORMATIONS DE L'EMPLOYÉ").setBold().setFontSize(12));
            doc.add(new Paragraph("Nom complet : " + c.getEmploye().getNom() + " " + c.getEmploye().getPrenom()));
            doc.add(new Paragraph("Poste       : " + c.getEmploye().getPoste()));
            doc.add(new Paragraph("Département : " + (c.getEmploye().getDepartement() != null ? c.getEmploye().getDepartement().getNom() : "N/A")));
            doc.add(new Paragraph("Matricule   : " + c.getEmploye().getMatricule()).setMarginBottom(10));

            // Bloc contrat
            doc.add(new Paragraph("DÉTAILS DU CONTRAT").setBold().setFontSize(12));
            doc.add(new Paragraph("Type        : " + c.getTypeContrat()));
            doc.add(new Paragraph("Date début  : " + c.getDateDebut().format(dtf)));
            doc.add(new Paragraph("Date fin    : " + (c.getDateFin() != null ? c.getDateFin().format(dtf) : "Indéterminée")));
            doc.add(new Paragraph("Salaire base: " + fmt(c.getSalaire()) + " FCFA brut/mois"));
            doc.add(new Paragraph("Avantages   : " + (c.getAvantages() != null && !c.getAvantages().isBlank() ? c.getAvantages() : "Néant")).setMarginBottom(10));

            // Section clauses
            doc.add(new Paragraph("CLAUSES CONTRACTUELLES").setBold().setFontSize(12));
            doc.add(new Paragraph("Article 1 — Le salarié s'engage à respecter les horaires et le règlement intérieur de l'entreprise."));
            doc.add(new Paragraph("Article 2 — La période d'essai est fixée conformément à la législation en vigueur pour ce type de contrat."));
            doc.add(new Paragraph("Article 3 — Le présent contrat est soumis au code du travail et à la convention collective applicable."));

            // Footer
            doc.add(new Paragraph("\n\nFait à Douala, le " + LocalDate.now().format(dtf))
                    .setTextAlignment(TextAlignment.RIGHT));

            // Zones signature
            Table sigTable = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth().setMarginTop(30);
            sigTable.addCell(new Cell().add(new Paragraph("L'Employeur")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            sigTable.addCell(new Cell().add(new Paragraph("L'Employé(e)")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            sigTable.addCell(new Cell().add(new Paragraph("\n\n________________________")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            sigTable.addCell(new Cell().add(new Paragraph("\n\n________________________")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            doc.add(sigTable);

            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return baos.toByteArray();
    }

    // ------------ Helpers ------------------------------------------------

    private void addRow(Table table, String label, String valeur, boolean bold) {
        Cell c1 = new Cell().add(new Paragraph(label));
        Cell c2 = new Cell().add(new Paragraph(valeur).setTextAlignment(TextAlignment.RIGHT));
        if (bold) {
            c1.setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY);
            c2.setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY);
        }
        table.addCell(c1);
        table.addCell(c2);
    }

    private String fmt(BigDecimal v) {
        if (v == null) return "0";
        return String.format("%,.0f", v.doubleValue());
    }

    private void logSMS(String telephone, String message) {
        System.out.println("--------------------------------------------------");
        System.out.println("SMS → " + telephone);
        System.out.println("MSG : " + message);
        System.out.println("--------------------------------------------------");
    }
}
