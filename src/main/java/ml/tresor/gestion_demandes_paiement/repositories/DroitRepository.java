package ml.tresor.gestion_demandes_paiement.repositories;

import ml.tresor.gestion_demandes_paiement.entities.Droit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroitRepository extends JpaRepository<Droit, Integer> {
    //Recherche le par son code
    Optional<Droit> findByCode(String code);

    //Vérifier si un droit existe par son code
    boolean existsByCode(String code);

    //Recherche un droit par son libellé
    Optional<Droit> findByLibelle(String libelle);

    //Vérifier si un libellé existe déja
    boolean existsByLibelle(String libelle);
}
