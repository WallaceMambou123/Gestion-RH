package com.example.gestionrh.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "conge")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_conge")
    private TypeConge typeConge;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "nb_jours", nullable = false)
    private Integer nbJours;

    @Column(length = 300)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutConge statut = StatutConge.DEMANDE;

    @Column(name = "approuve_par", length = 100)
    private String approuvePar;

    public enum TypeConge {
        ANNUEL, MALADIE, MATERNITE, PATERNITE, EXCEPTIONNEL
    }

    public enum StatutConge {
        DEMANDE, APPROUVE, REFUSE
    }
}
