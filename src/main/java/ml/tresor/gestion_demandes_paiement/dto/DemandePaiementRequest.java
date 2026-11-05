package ml.tresor.gestion_demandes_paiement.dto;



public record DemandePaiementRequest(

        String reference,
        String objet,
        Integer montant,
        Integer beneficiaireId

) {
}