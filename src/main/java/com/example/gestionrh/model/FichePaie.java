package com.example.gestionrh.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fiche_paie", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employe_id", "mois"})
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FichePaie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;

    @Column(nullable = false, length = 7) // Format AAAA-MM
    private String mois;

    @Column(name = "salaire_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal salaireBase;

    @Column(name = "heures_sup", precision = 6, scale = 2)
    @Builder.Default
    private BigDecimal heuresSup = BigDecimal.ZERO;

    @Column(name = "montant_heures_sup", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal montantHeuresSup = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal primes = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal retenues = BigDecimal.ZERO;

    @Column(name = "salaire_brut", nullable = false, precision = 10, scale = 2)
    private BigDecimal salaireBrut;

    @Column(name = "salaire_net", nullable = false, precision = 10, scale = 2)
    private BigDecimal salaireNet;
}
