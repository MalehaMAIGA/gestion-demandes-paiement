package ml.tresor.gestion_demandes_paiement.repositories;

import ml.tresor.gestion_demandes_paiement.entities.Beneficiaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeneficiaireRepository extends JpaRepository<Beneficiaire,Integer> {

    Optional<Beneficiaire> findByReference(String reference);

    boolean existsByReference(String reference);




}
