package com.example.gestionrh.service;

import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.FichePaie;
import com.example.gestionrh.repository.FichePaieRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaieService {
    private final FichePaieRepository fichePaieRepository = new FichePaieRepository();

    public FichePaie calculerFichePaie(Employe e, String mois, BigDecimal heuresSup, BigDecimal primes, BigDecimal retenues) {
        BigDecimal salaireBase = e.getSalaireBase();
        
        // montant_heures_sup = heures_sup * (salaire_base / 173) * 1.25
        BigDecimal tauxHoraire = salaireBase.divide(new BigDecimal("173"), 2, RoundingMode.HALF_UP);
        BigDecimal montantHeuresSup = heuresSup.multiply(tauxHoraire).multiply(new BigDecimal("1.25")).setScale(2, RoundingMode.HALF_UP);
        
        // salaire_brut = salaire_base + montant_heures_sup + primes
        BigDecimal salaireBrut = salaireBase.add(montantHeuresSup).add(primes);
        
        // salaire_net = salaire_brut - retenues
        BigDecimal salaireNet = salaireBrut.subtract(retenues);

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
        envoyerSMSSimulation(e.getTelephone(), "Votre fiche de paie de " + mois + " est disponible. Net : " + salaireNet + " FCFA");
        
        return fp;
    }

    public byte[] genererFichePaiePDF(FichePaie fp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("FICHE DE PAIE - " + fp.getMois()).setBold().setFontSize(18));
            document.add(new Paragraph("Employé: " + fp.getEmploye().getPrenom() + " " + fp.getEmploye().getNom()));
            document.add(new Paragraph("Matricule: " + fp.getEmploye().getMatricule()));
            document.add(new Paragraph("Poste: " + fp.getEmploye().getPoste()));

            Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth();
            table.addCell("Libellé");
            table.addCell("Montant");

            table.addCell("Salaire de Base");
            table.addCell(fp.getSalaireBase().toString());

            table.addCell("Heures Supplémentaires (" + fp.getHeuresSup() + "h)");
            table.addCell(fp.getMontantHeuresSup().toString());

            table.addCell("Primes");
            table.addCell(fp.getPrimes().toString());

            table.addCell("RÉTRIBUTIONS BRUTES");
            table.addCell(fp.getSalaireBrut().toString());

            table.addCell("Retenues (Impositions/Social)");
            table.addCell(fp.getRetenues().toString());

            table.addCell("NET À PAYER");
            table.addCell(fp.getSalaireNet().toString());

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private void envoyerSMSSimulation(String telephone, String message) {
        System.out.println("--------------------------------------------------");
        System.out.println("SMS SIMULATION to " + telephone);
        System.out.println("MESSAGE: " + message);
        System.out.println("--------------------------------------------------");
    }
}
