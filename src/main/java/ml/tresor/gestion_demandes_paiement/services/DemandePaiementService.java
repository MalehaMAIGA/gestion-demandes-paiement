
package ml.tresor.gestion_demandes_paiement.services;

import lombok.RequiredArgsConstructor;
import ml.tresor.gestion_demandes_paiement.dto.DemandePaiementRequest;
import ml.tresor.gestion_demandes_paiement.dto.DemandePaiementResponse;
import ml.tresor.gestion_demandes_paiement.entities.Beneficiaire;
import ml.tresor.gestion_demandes_paiement.entities.DemandePaiement;
import ml.tresor.gestion_demandes_paiement.enums.StatutDemande;
import ml.tresor.gestion_demandes_paiement.mappers.DemandePaiementMapper;
import ml.tresor.gestion_demandes_paiement.repositories.BeneficiaireRepository;
import ml.tresor.gestion_demandes_paiement.repositories.DemandePaiementRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DemandePaiementService {

    private final DemandePaiementRepository demandePaiementRepository;
    private final BeneficiaireRepository beneficiaireRepository;
    private final DemandePaiementMapper demandePaiementMapper;

    @Value("${tresor.demande-paiement.plafond:10000000}")
    private Integer plafond;

    // Créer une demande de paiement
    public DemandePaiementResponse creerDemandePaiement(
            DemandePaiementRequest request) {

        validerDemande(request);

        if (demandePaiementRepository.existsByReference(request.reference())) {
            throw new ApiError(409, "Cette référence existe déjà.");
        }

        Beneficiaire beneficiaire = beneficiaireRepository
                .findById(request.beneficiaireId())
                .orElseThrow(() ->
                        new ApiError(404, "Bénéficiaire introuvable."));

        DemandePaiement demande =
                demandePaiementMapper.versEntite(request, beneficiaire);

        demande.setStatut(StatutDemande.BROUILLON);

        DemandePaiement demandeEnregistree =
                demandePaiementRepository.save(demande);

        return demandePaiementMapper.versResponse(demandeEnregistree);
    }

    // Lister toutes les demandes
    @Transactional(readOnly = true)
    public List<DemandePaiementResponse> listerDemandesPaiement() {

        return demandePaiementRepository.findAll()
                .stream()
                .map(demandePaiementMapper::versResponse)
                .toList();
    }

    // Consulter une demande par son identifiant
    @Transactional(readOnly = true)
    public DemandePaiementResponse trouverDemandePaiement(Integer id) {

        DemandePaiement demande = obtenirDemande(id);

        return demandePaiementMapper.versResponse(demande);
    }

    // Modifier une demande
    public DemandePaiementResponse modifierDemandePaiement(
            Integer id,
            DemandePaiementRequest request) {

        validerDemande(request);

        DemandePaiement demande = obtenirDemande(id);

        verifierBrouillon(demande);

        if (!demande.getReference().equals(request.reference())) {
            throw new ApiError(
                    400,
                    "La référence de la demande ne peut pas être modifiée."
            );
        }

        Beneficiaire beneficiaire = beneficiaireRepository
                .findById(request.beneficiaireId())
                .orElseThrow(() ->
                        new ApiError(404, "Bénéficiaire introuvable."));

        demande.setObjet(request.objet());
        demande.setMontant(request.montant());
        demande.setBeneficiaire(beneficiaire);

        return demandePaiementMapper.versResponse(
                demandePaiementRepository.save(demande)
        );
    }

    // Supprimer une demande
    public void supprimerDemandePaiement(Integer id) {

        DemandePaiement demande = obtenirDemande(id);

        verifierBrouillon(demande);

        // Une demande ayant une fiche ne peut pas être supprimée
        // sans suppression préalable de sa fiche.
        // À compléter avec FicheControleRepository.

        demandePaiementRepository.delete(demande);
    }

    // Soumettre une demande
    public DemandePaiementResponse soumettreDemandePaiement(Integer id) {

        DemandePaiement demande = obtenirDemande(id);

        verifierBrouillon(demande);

        // À compléter : vérifier l'existence de la fiche de contrôle.
        // Une demande ne peut être soumise sans fiche de contrôle.

        demande.setStatut(StatutDemande.SOUMISE);

        return demandePaiementMapper.versResponse(
                demandePaiementRepository.save(demande)
        );
    }

    // Rechercher par référence ou objet
    @Transactional(readOnly = true)
    public List<DemandePaiementResponse> rechercherDemandes(String q) {

        if (q == null || q.isBlank() || q.length() > 100) {
            throw new ApiError(400, "Le terme de recherche est invalide.");
        }

        // À compléter avec la méthode JPQL du Repository.
        throw new UnsupportedOperationException(
                "La recherche sera ajoutée avec le Repository."
        );
    }

    // Récupérer une demande ou retourner 404
    private DemandePaiement obtenirDemande(Integer id) {

        return demandePaiementRepository.findById(id)
                .orElseThrow(() ->
                        new ApiError(404, "Demande de paiement introuvable."));
    }

    // Vérifier que la demande est encore en brouillon
    private void verifierBrouillon(DemandePaiement demande) {

        if (demande.getStatut() != StatutDemande.BROUILLON) {
            throw new ApiError(
                    409,
                    "Cette opération est interdite : la demande est déjà soumise."
            );
        }
    }

    // Vérifier les données reçues
    private void validerDemande(DemandePaiementRequest request) {

        if (request == null) {
            throw new ApiError(400, "Les données de la demande sont obligatoires.");
        }

        if (request.reference() == null
                || request.reference().isBlank()
                || request.reference().length() > 20) {
            throw new ApiError(400, "La référence est invalide.");
        }

        if (request.objet() == null
                || request.objet().isBlank()
                || request.objet().length() > 150) {
            throw new ApiError(400, "L'objet est invalide.");
        }

        if (request.montant() == null
                || request.montant() < 1
                || request.montant() > plafond) {
            throw new ApiError(400, "Le montant est invalide ou dépasse le plafond autorisé.");
        }

        if (request.beneficiaireId() == null
                || request.beneficiaireId() <= 0) {
            throw new ApiError(400, "L'identifiant du bénéficiaire est invalide.");
        }
    }
}
