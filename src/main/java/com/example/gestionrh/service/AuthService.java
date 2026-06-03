package com.example.gestionrh.service;

import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.UtilisateurRepository;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

public class AuthService {
    private final UtilisateurRepository utilisateurRepository = new UtilisateurRepository();

    public Optional<Utilisateur> login(String username, String password) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (BCrypt.checkpw(password, user.getPassword()) && user.isActif()) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public void register(Utilisateur user) {
        // Hachage du mot de passe
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        utilisateurRepository.save(user);
    }
}
