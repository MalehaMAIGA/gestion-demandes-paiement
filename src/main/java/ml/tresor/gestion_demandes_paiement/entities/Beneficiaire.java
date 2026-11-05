package ml.tresor.gestion_demandes_paiement.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="beneficiaires")
public class Beneficiaire extends Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="reference", nullable = false, unique = true,updatable = false, length = 20)
    private String reference;

    @Column(name="adresse", length = 255)
    private String adresse;

    @Column(name="compteFictif", nullable = false, length = 24)
    private String compteFictif;



}
