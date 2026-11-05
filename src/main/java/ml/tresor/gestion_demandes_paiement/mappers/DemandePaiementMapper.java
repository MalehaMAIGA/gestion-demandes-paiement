package ml.tresor.gestion_demandes_paiement.mappers;

import ml.tresor.gestion_demandes_paiement.dto.DemandePaiementRequest;
import ml.tresor.gestion_demandes_paiement.dto.DemandePaiementResponse;
import ml.tresor.gestion_demandes_paiement.entities.Beneficiaire;
import ml.tresor.gestion_demandes_paiement.entities.DemandePaiement;
import org.springframework.stereotype.Component;

@Component
public class DemandePaiementMapper {

    // Transformer le DTO en entité
    public DemandePaiement versEntite(
            DemandePaiementRequest request,
            Beneficiaire beneficiaire) {

        DemandePaiement demande = new DemandePaiement();

        demande.setReference(request.reference());
        demande.setObjet(request.objet());
        demande.setMontant(request.montant());
        demande.setBeneficiaire(beneficiaire);

        return demande;
    }

    // Transformer l'entité en DTO de réponse
    public DemandePaiementResponse versResponse(
            DemandePaiement demande) {

        return new DemandePaiementResponse(
                demande.getId(),
                demande.getReference(),
                demande.getObjet(),
                demande.getMontant(),
                demande.getStatut(),
                demande.getBeneficiaire().getId(),
                demande.getDateCreation(),
                demande.getDateModification()
        );
    }
}