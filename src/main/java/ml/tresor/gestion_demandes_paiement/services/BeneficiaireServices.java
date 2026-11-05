
package ml.tresor.gestion_demandes_paiement.services;

import lombok.RequiredArgsConstructor;

import ml.tresor.gestion_demandes_paiement.dto.BeneficiaireRequest;
import ml.tresor.gestion_demandes_paiement.dto.BeneficiaireResponse;
import ml.tresor.gestion_demandes_paiement.entities.Beneficiaire;
import ml.tresor.gestion_demandes_paiement.enums.StatutDemande;
import ml.tresor.gestion_demandes_paiement.mappers.BeneficiaireMapper;
import ml.tresor.gestion_demandes_paiement.repositories.BeneficiaireRepository;
import ml.tresor.gestion_demandes_paiement.repositories.DemandePaiementRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BeneficiaireServices {

    private final BeneficiaireRepository beneficiaireRepository;
    private final DemandePaiementRepository demandePaiementRepository;
    private final BeneficiaireMapper beneficiaireMapper;

    // 1. Création d'un bénéficiaire
    public BeneficiaireResponse creerBeneficiaire(
            BeneficiaireRequest request
    ) {

        if (request == null) {
            throw new ApiError(400, "Les données du bénéficiaire sont obligatoires.");
        }

        Beneficiaire beneficiaire = beneficiaireMapper.versEntite(request);

        verifierBeneficiaire(beneficiaire);

        String reference = verifierReference(request.reference());

        if (beneficiaireRepository.existsByReference(reference)) {
            throw new ApiError(409, "Cette référence de bénéficiaire existe déjà.");
        }

        beneficiaire.setReference(reference);

        Beneficiaire enregistre =
                beneficiaireRepository.save(beneficiaire);

        return beneficiaireMapper.versResponse(enregistre);
    }

    // 2. Vérification des champs obligatoires et des longueurs
    private void verifierBeneficiaire(Beneficiaire beneficiaire) {

        if (beneficiaire.getNom() == null
                || beneficiaire.getNom().isBlank()) {
            throw new ApiError(400, "Le nom est obligatoire.");
        }

        if (beneficiaire.getNom().trim().length() > 100) {
            throw new ApiError(400, "Le nom ne doit pas dépasser 100 caractères.");
        }

        if (beneficiaire.getPrenom() == null
                || beneficiaire.getPrenom().isBlank()) {
            throw new ApiError(400, "Le prénom est obligatoire.");
        }

        if (beneficiaire.getPrenom().trim().length() > 150) {
            throw new ApiError(400, "Le prénom ne doit pas dépasser 150 caractères.");
        }

        if (beneficiaire.getEmail() == null
                || beneficiaire.getEmail().isBlank()) {
            throw new ApiError(400, "L'email est obligatoire.");
        }

        if (beneficiaire.getEmail().trim().length() > 75) {
            throw new ApiError(400, "L'email ne doit pas dépasser 75 caractères.");
        }

        if (beneficiaire.getGenre() == null) {
            throw new ApiError(400, "Le genre est obligatoire.");
        }

        if (beneficiaire.getCompteFictif() == null
                || beneficiaire.getCompteFictif().isBlank()) {
            throw new ApiError(400, "Le compte fictif est obligatoire.");
        }

        if (beneficiaire.getCompteFictif().trim().length() > 24) {
            throw new ApiError(400, "Le compte fictif ne doit pas dépasser 24 caractères.");
        }

        if (beneficiaire.getAdresse() != null
                && beneficiaire.getAdresse().length() > 255) {
            throw new ApiError(400, "L'adresse ne doit pas dépasser 255 caractères.");
        }
    }

    // 3. Vérification de la référence
    private String verifierReference(String reference) {

        if (reference == null || reference.isBlank()) {
            throw new ApiError(400, "La référence est obligatoire.");
        }

        String referenceVerifiee = reference.trim();

        if (referenceVerifiee.length() > 20) {
            throw new ApiError(400, "La référence ne doit pas dépasser 20 caractères.");
        }

        return referenceVerifiee;
    }

    // 4. Recherche par identifiant
    @Transactional(readOnly = true)
    public BeneficiaireResponse rechercherBeneficiaireParId(Integer id) {

        if (id == null || id <= 0) {
            throw new ApiError(400, "L'identifiant doit être positif.");
        }

        Beneficiaire beneficiaire = beneficiaireRepository.findById(id)
                .orElseThrow(() ->
                        new ApiError(404, "Le bénéficiaire demandé est introuvable.")
                );

        return beneficiaireMapper.versResponse(beneficiaire);
    }

    // 5. Liste des bénéficiaires
    @Transactional(readOnly = true)
    public List<BeneficiaireResponse> listerBeneficiaires() {

        return beneficiaireRepository.findAll()
                .stream()
                .map(beneficiaireMapper::versResponse)
                .toList();
    }

    // 6. Modification d'un bénéficiaire
    public BeneficiaireResponse modifierBeneficiaire(
            Integer id,
            BeneficiaireRequest request
    ) {

        if (request == null) {
            throw new ApiError(400, "Les nouvelles données sont obligatoires.");
        }

        if (id == null || id <= 0) {
            throw new ApiError(400, "L'identifiant doit être positif.");
        }

        Beneficiaire beneficiaireExistant =
                beneficiaireRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiError(404, "Le bénéficiaire demandé est introuvable.")
                        );

        // R06 : vérifier si une demande SOUMISE utilise ce bénéficiaire
        boolean demandeSoumise =
                demandePaiementRepository.existsByBeneficiaire_IdAndStatut(
                        id,
                        StatutDemande.SOUMISE
                );

        if (demandeSoumise) {
            throw new ApiError(
                    409,
                    "Ce bénéficiaire est associé à une demande soumise et ne peut plus être modifié."
            );
        }

        // La référence est immuable
        String nouvelleReference = verifierReference(request.reference());

        if (!beneficiaireExistant.getReference().equals(nouvelleReference)) {
            throw new ApiError(
                    400,
                    "La référence du bénéficiaire ne peut pas être modifiée."
            );
        }

        Beneficiaire nouvellesDonnees =
                beneficiaireMapper.versEntite(request);

        verifierBeneficiaire(nouvellesDonnees);

        beneficiaireExistant.setNom(nouvellesDonnees.getNom().trim());
        beneficiaireExistant.setPrenom(nouvellesDonnees.getPrenom().trim());
        beneficiaireExistant.setEmail(nouvellesDonnees.getEmail().trim());
        beneficiaireExistant.setGenre(nouvellesDonnees.getGenre());
        beneficiaireExistant.setAdresse(nouvellesDonnees.getAdresse());
        beneficiaireExistant.setCompteFictif(
                nouvellesDonnees.getCompteFictif().trim()
        );

        Beneficiaire modifie =
                beneficiaireRepository.save(beneficiaireExistant);

        return beneficiaireMapper.versResponse(modifie);
    }

    // 7. Suppression d'un bénéficiaire
    public void supprimerBeneficiaire(Integer id) {

        if (id == null || id <= 0) {
            throw new ApiError(400, "L'identifiant doit être positif.");
        }

        Beneficiaire beneficiaire = beneficiaireRepository.findById(id)
                .orElseThrow(() ->
                        new ApiError(404, "Le bénéficiaire demandé est introuvable.")
                );

        // R07 : aucune demande ne doit être associée au bénéficiaire
        boolean possedeUneDemande =
                demandePaiementRepository.existsByBeneficiaire_Id(id);

        if (possedeUneDemande) {
            throw new ApiError(
                    409,
                    "Ce bénéficiaire ne peut pas être supprimé car il possède des demandes de paiement."
            );
        }

        beneficiaireRepository.delete(beneficiaire);
        beneficiaireRepository.flush();
    }
}
