package ml.tresor.gestion_demandes_paiement.dto;

import ml.tresor.gestion_demandes_paiement.enums.StatutDemande;

import java.time.LocalDateTime;

public record DemandePaiementResponse(

        Integer id,
        String reference,
        String objet,
        Integer montant,
        StatutDemande statut,
        Integer beneficiaireId,
        LocalDateTime dateCreation,
        LocalDateTime dateModification

) {
}