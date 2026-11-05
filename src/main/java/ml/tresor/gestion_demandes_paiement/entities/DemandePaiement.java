package ml.tresor.gestion_demandes_paiement.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ml.tresor.gestion_demandes_paiement.enums.StatutDemande;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="demandepaiement")
public class DemandePaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="reference", unique = true,updatable = false,nullable = false, length = 20)
    private String reference;

    @Column(name="objet",nullable = false,length = 150)
    private String objet;

   @Column(name="montant",nullable = false)
    private Integer montant;


   @ManyToOne(fetch = FetchType.LAZY,optional = false)
   @JoinColumn(name="beneficiaire_id",nullable = false)
   private Beneficiaire beneficiaire;

   @Enumerated(EnumType.STRING)
   @Column(name="statut",nullable = false)
   private StatutDemande statut = StatutDemande.BROUILLON;


   @Column(name="date_creation",nullable = false,updatable = false)
    private LocalDateTime dateCreation;

    @Column(name="date_modification")
    private LocalDateTime dateModification;

    @Column(name="date_soumission")
    private LocalDateTime dateSoumission;

    @PrePersist
    protected void avantCreation(){
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    @PreUpdate
    private void avantModification(){
        this.dateModification = LocalDateTime.now();
    }


}
