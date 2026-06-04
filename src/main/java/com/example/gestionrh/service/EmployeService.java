package com.example.gestionrh.service;

import com.example.gestionrh.model.Employe;
import com.example.gestionrh.repository.DepartementRepository;
import com.example.gestionrh.repository.EmployeRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class EmployeService {
    private final EmployeRepository employeRepository = new EmployeRepository();
    private final DepartementRepository departementRepository = new DepartementRepository();

    public List<Employe> getAll() {
        return employeRepository.findAll();
    }

    public List<Employe> getAllPaginated(int page, int size) {
        return employeRepository.findAllPaginated(page, size);
    }

    public List<Employe> getByDepartement(Long deptId) {
        return employeRepository.findByDepartement(deptId);
    }

    public List<Employe> rechercherParNom(String query) {
        return employeRepository.findByNomOrPrenom(query);
    }

    public Employe getById(Long id) {
        return employeRepository.findById(id);
    }

    public long count() {
        return employeRepository.count();
    }

    public void save(Employe e) {
        if (e.getId() == null && (e.getMatricule() == null || e.getMatricule().isBlank())) {
            e.setMatricule(genererMatricule());
        }
        employeRepository.save(e);
    }

    public void delete(Long id) {
        employeRepository.delete(id);
    }

    public String exportToCSV(List<Employe> employes) {
        StringBuilder csv = new StringBuilder();
        csv.append("ID;Matricule;Nom;Prenom;Poste;Departement;Email;Telephone;Type Contrat;Salaire Base\n");
        for (Employe e : employes) {
            String dept = (e.getDepartement() != null) ? e.getDepartement().getNom() : "";
            csv.append(e.getId()).append(";")
               .append(e.getMatricule()).append(";")
               .append(e.getNom()).append(";")
               .append(e.getPrenom()).append(";")
               .append(e.getPoste()).append(";")
               .append(dept).append(";")
               .append(e.getEmail()).append(";")
               .append(e.getTelephone() != null ? e.getTelephone() : "").append(";")
               .append(e.getTypeContrat()).append(";")
               .append(e.getSalaireBase()).append("\n");
        }
        return csv.toString();
    }

    public String uploadPhoto(InputStream fileContent, String fileName, String uploadPath) throws Exception {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        File file = new File(uploadDir, uniqueFileName);
        try (OutputStream out = new FileOutputStream(file)) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = fileContent.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
        return uniqueFileName;
    }

    private String genererMatricule() {
        long count = employeRepository.count() + 1;
        return String.format("EMP-%04d", count);
    }
}
