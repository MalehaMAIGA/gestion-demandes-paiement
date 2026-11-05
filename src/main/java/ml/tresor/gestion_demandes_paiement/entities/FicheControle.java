package ml.tresor.gestion_demandes_paiement.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="fiche_controle")
public class FicheControle {

    @Id
    @Column(nullable = false, length = 20, updatable = false)
    private String numero;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="demande_paiement_id",
            nullable = false,
            unique = true
    )
    private DemandePaiement demandePaiement;

    @PastOrPresent(message = "La date de contrôle ne peut pas être dans le futur")
    @Column(name="date_Controle", nullable = false)
    private LocalDate dateControle;

    @Column(name= " commentaire",nullable = false,length = 255)
    private String commentaire;

    @Column(name="date_Creation",nullable = false,updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected  void avantCreation(){
       this.dateCreation = LocalDateTime.now();
    }
}
