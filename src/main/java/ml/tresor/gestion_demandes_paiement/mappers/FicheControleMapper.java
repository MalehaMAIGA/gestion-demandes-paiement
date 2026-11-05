
package ml.tresor.gestion_demandes_paiement.mappers;

import ml.tresor.gestion_demandes_paiement.dto.FicheControleRequest;
import ml.tresor.gestion_demandes_paiement.dto.FicheControleResponse;
import ml.tresor.gestion_demandes_paiement.entities.DemandePaiement;
import ml.tresor.gestion_demandes_paiement.entities.FicheControle;
import org.springframework.stereotype.Component;

@Component
public class FicheControleMapper {

    // Conversion du DTO Request vers l'entité
    public FicheControle versEntite(
            FicheControleRequest request,
            DemandePaiement demandePaiement) {

        FicheControle fiche = new FicheControle();

        fiche.setNumero(request.numero());
        fiche.setDateControle(request.dateControle());
        fiche.setCommentaire(request.commentaire());
        fiche.setDemandePaiement(demandePaiement);

        return fiche;
    }

    // Conversion de l'entité vers le DTO Response
    public FicheControleResponse versResponse(FicheControle fiche) {

        return new FicheControleResponse(
                fiche.getNumero(),
                fiche.getDemandePaiement().getId(),
                fiche.getDateControle(),
                fiche.getCommentaire(),
                fiche.getDateCreation()
        );
    }
}
