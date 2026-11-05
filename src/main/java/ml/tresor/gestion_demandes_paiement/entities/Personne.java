package ml.tresor.gestion_demandes_paiement.entities;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ml.tresor.gestion_demandes_paiement.enums.Genre;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public abstract class Personne {

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 150)
    private String prenom;

    @Column(name = "email",nullable = false, length = 75)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="genre",nullable = false)
    private Genre genre;

}
