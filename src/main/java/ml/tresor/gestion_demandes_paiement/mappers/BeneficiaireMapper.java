package ml.tresor.gestion_demandes_paiement.mappers;

import ml.tresor.gestion_demandes_paiement.dto.BeneficiaireRequest;
import ml.tresor.gestion_demandes_paiement.dto.BeneficiaireResponse;
import ml.tresor.gestion_demandes_paiement.entities.Beneficiaire;
import org.springframework.stereotype.Component;

@Component
public class BeneficiaireMapper {
    //Transformer les données reçues en entite JPA
    public Beneficiaire versEntite(BeneficiaireRequest request) {

        Beneficiaire beneficiaire = new Beneficiaire();
        beneficiaire.setReference(request.reference());
        beneficiaire.setNom(request.nom());
        beneficiaire.setPrenom(request.prenom());
        beneficiaire.setEmail(request.email());
        beneficiaire.setAdresse(request.adresse());
        beneficiaire.setGenre(request.genre());
        beneficiaire.setCompteFictif(request.compteFictif());

        return beneficiaire;
    }

    //Transformer une entité JPA en reponse JSON
    public BeneficiaireResponse versResponse(Beneficiaire beneficiaire) {
        return new BeneficiaireResponse(
                beneficiaire.getId(),
                beneficiaire.getReference(),
                beneficiaire.getNom(),
                beneficiaire.getPrenom(),
                beneficiaire.getEmail(),
                beneficiaire.getAdresse(),
                beneficiaire.getGenre(),
                beneficiaire.getCompteFictif()
        );
    }


}
