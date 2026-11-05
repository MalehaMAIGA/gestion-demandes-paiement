package ml.tresor.gestion_demandes_paiement.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="droits")
public class Droit {
    @Id
    @Column(nullable = false, length = 30)
    private String code;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    @Column(length = 255)
    private String description;

}