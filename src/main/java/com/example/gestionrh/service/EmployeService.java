package com.example.gestionrh.service;

import com.example.gestionrh.model.Employe;
import com.example.gestionrh.repository.EmployeRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class EmployeService {
    private final EmployeRepository employeRepository = new EmployeRepository();

    public List<Employe> getAll() {
        return employeRepository.findAll();
    }

    public Employe getById(Long id) {
        return employeRepository.findById(id);
    }

    public void save(Employe e) {
        employeRepository.save(e);
    }

    public String exportToCSV(List<Employe> employes) {
        StringBuilder csv = new StringBuilder();
        csv.append("ID;Matricule;Nom;Prenom;Poste;Departement;Email;Telephone;Type Contrat\n");
        for (Employe e : employes) {
            csv.append(e.getId()).append(";")
               .append(e.getMatricule()).append(";")
               .append(e.getNom()).append(";")
               .append(e.getPrenom()).append(";")
               .append(e.getPoste()).append(";")
               .append(e.getDepartement().getNom()).append(";")
               .append(e.getEmail()).append(";")
               .append(e.getTelephone()).append(";")
               .append(e.getTypeContrat()).append("\n");
        }
        return csv.toString();
    }

    public String uploadPhoto(InputStream fileContent, String fileName, String uploadPath) throws Exception {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        File file = new File(uploadDir, uniqueFileName);

        try (OutputStream out = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = fileContent.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
        return uniqueFileName;
    }
}
