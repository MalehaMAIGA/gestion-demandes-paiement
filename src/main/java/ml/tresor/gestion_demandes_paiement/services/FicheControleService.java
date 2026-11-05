
package ml.tresor.gestion_demandes_paiement.services;

import lombok.RequiredArgsConstructor;
import ml.tresor.gestion_demandes_paiement.dto.FicheControleRequest;
import ml.tresor.gestion_demandes_paiement.dto.FicheControleResponse;
import ml.tresor.gestion_demandes_paiement.entities.DemandePaiement;
import ml.tresor.gestion_demandes_paiement.entities.FicheControle;
import ml.tresor.gestion_demandes_paiement.enums.StatutDemande;
import ml.tresor.gestion_demandes_paiement.mappers.FicheControleMapper;
import ml.tresor.gestion_demandes_paiement.repositories.DemandePaiementRepository;
import ml.tresor.gestion_demandes_paiement.repositories.FicheControleRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class FicheControleService {

    private final FicheControleRepository ficheControleRepository;
    private final DemandePaiementRepository demandePaiementRepository;
    private final FicheControleMapper ficheControleMapper;

    // CREATION D'UNE FICHE DE CONTROLE
    public FicheControleResponse creer(
            Integer demandeId,
            FicheControleRequest request) {

        // Vérifier les informations reçues
        validerRequest(request);

        // Rechercher la demande de paiement
        DemandePaiement demande = obtenirDemande(demandeId);

        // Vérifier que la demande est encore en brouillon
        verifierBrouillon(demande);

        // Vérifier si la demande possède déjà une fiche
        if (ficheControleRepository.existsByDemandePaiement_id(demandeId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette demande possède déjà une fiche de contrôle"
            );
        }

        // Vérifier l'unicité du numéro
        if (ficheControleRepository.existsById(request.numero())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce numéro de fiche existe déjà"
            );
        }

        // Convertir le DTO en entité
        FicheControle fiche =
                ficheControleMapper.versEntite(request, demande);

        // Enregistrer la fiche
        FicheControle ficheEnregistree =
                ficheControleRepository.save(fiche);

        return ficheControleMapper.versResponse(ficheEnregistree);
    }

    // CONSULTATION D'UNE FICHE DE CONTROLE
    @Transactional(readOnly = true)
    public FicheControleResponse consulter(Integer demandeId) {

        // Vérifier que la demande existe
        obtenirDemande(demandeId);

        // Rechercher la fiche associée
        FicheControle fiche = ficheControleRepository
                .findByDemandePaiement_id(demandeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aucune fiche de contrôle trouvée pour cette demande"
                ));

        return ficheControleMapper.versResponse(fiche);
    }

    // MODIFICATION D'UNE FICHE DE CONTROLE
    public FicheControleResponse modifier(
            Integer demandeId,
            FicheControleRequest request) {

        validerRequest(request);

        // Rechercher la demande
        DemandePaiement demande = obtenirDemande(demandeId);

        // Vérifier que la demande est en brouillon
        verifierBrouillon(demande);

        // Rechercher la fiche existante
        FicheControle fiche = ficheControleRepository
                .findByDemandePaiement_id(demandeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fiche de contrôle introuvable"
                ));

        // Le numéro ne peut pas être modifié
        if (!fiche.getNumero().equals(request.numero())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Le numéro de la fiche ne peut pas être modifié"
            );
        }

        // Mettre à jour les informations modifiables
        fiche.setDateControle(request.dateControle());
        fiche.setCommentaire(request.commentaire());

        FicheControle ficheModifiee =
                ficheControleRepository.save(fiche);

        return ficheControleMapper.versResponse(ficheModifiee);
    }

    // SUPPRESSION D'UNE FICHE DE CONTROLE
    public void supprimer(Integer demandeId) {

        // Rechercher la demande
        DemandePaiement demande = obtenirDemande(demandeId);

        // Vérifier que la demande est en brouillon
        verifierBrouillon(demande);

        // Rechercher la fiche
        FicheControle fiche = ficheControleRepository
                .findByDemandePaiement_id(demandeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fiche de contrôle introuvable"
                ));

        // Supprimer la fiche
        ficheControleRepository.delete(fiche);
    }

    // RECHERCHER UNE DEMANDE DE PAIEMENT
    private DemandePaiement obtenirDemande(Integer demandeId) {

        return demandePaiementRepository.findById(demandeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Demande de paiement introuvable"
                ));
    }

    // VERIFIER LE STATUT DE LA DEMANDE
    private void verifierBrouillon(DemandePaiement demande) {

        if (demande.getStatut() != StatutDemande.BROUILLON) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Impossible de modifier cette fiche : la demande est déjà soumise"
            );
        }
    }

    // VALIDATION DES INFORMATIONS DE LA FICHE
    private void validerRequest(FicheControleRequest request) {

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Les informations de la fiche sont obligatoires"
            );
        }

        if (request.numero() == null || request.numero().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le numéro de la fiche est obligatoire"
            );
        }

        if (request.numero().length() > 20) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le numéro de la fiche ne doit pas dépasser 20 caractères"
            );
        }

        if (request.dateControle() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La date de contrôle est obligatoire"
            );
        }

        if (request.dateControle().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La date de contrôle ne peut pas être dans le futur"
            );
        }

        if (request.commentaire() == null
                || request.commentaire().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le commentaire est obligatoire"
            );
        }

        if (request.commentaire().length() > 255) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le commentaire ne doit pas dépasser 255 caractères"
            );
        }
    }
}
