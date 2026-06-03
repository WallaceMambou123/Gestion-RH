package com.example.gestionrh.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departement")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    private String responsable;

    @Column(name = "budget_masse_salariale", precision = 14, scale = 2)
    private BigDecimal budgetMasseSalariale;

    @OneToMany(mappedBy = "departement", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Employe> employes = new ArrayList<>();
}
