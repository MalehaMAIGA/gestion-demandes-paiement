package ml.tresor.gestion_demandes_paiement.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="roles")
public class UserRole {

    @Id
    @Column(length = 10)
    private String code;

    @Column(nullable = false,unique = true,length = 20)
    private String libelle;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_droits",
            joinColumns = @JoinColumn(name="role_code"),
            inverseJoinColumns = @JoinColumn(name="droit_code")
    )
    private List<Droit> droits;
}
