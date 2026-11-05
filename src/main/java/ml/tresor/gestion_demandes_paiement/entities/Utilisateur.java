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
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true,length = 100)
    private String email;

    @Column(unique = true,length = 20)
    private String telephone;

    @Column(nullable = false,length = 100)
    private String pwd;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name= "Utilisateur_roles",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_code")
    )
    private List<ml.tresor.gestion_demandes_paiement.entities.UserRole> role;


}
