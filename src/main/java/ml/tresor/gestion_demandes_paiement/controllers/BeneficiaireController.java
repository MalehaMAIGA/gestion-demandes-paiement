
package ml.tresor.gestion_demandes_paiement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ml.tresor.gestion_demandes_paiement.dto.BeneficiaireRequest;
import ml.tresor.gestion_demandes_paiement.dto.BeneficiaireResponse;
import ml.tresor.gestion_demandes_paiement.services.BeneficiaireServices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(
        name = "Geston des bénéficiaire ",
        description = "Toutes les operations relatives aux Bénéficiaires"
)
@RequestMapping("/beneficiaires")
@RequiredArgsConstructor
public class BeneficiaireController {

    private final BeneficiaireServices beneficiaireServices;

    // CREATION
    @PostMapping
    @Operation(summary = "Créer un bénéficiaire")
    public ResponseEntity<BeneficiaireResponse> creer(
            @RequestBody BeneficiaireRequest request) {

        BeneficiaireResponse nouveau =
                beneficiaireServices.creerBeneficiaire(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nouveau.id())
                .toUri();

        return ResponseEntity.created(location).body(nouveau);
    }

    // LISTE DES BENEFICIAIRES
    @GetMapping
    @Operation(summary = "Lister les bénéficiaires")
    public ResponseEntity<List<BeneficiaireResponse>> lister() {

        return ResponseEntity.ok(
                beneficiaireServices.listerBeneficiaires()
        );
    }

    // RECHERCHE PAR ID
    @GetMapping("/{id}")
    @Operation(summary = "Rechercher un bénéficiaire par son ID")
    public ResponseEntity<BeneficiaireResponse> rechercherParId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                beneficiaireServices.rechercherBeneficiaireParId(id)
        );
    }

    // MODIFICATION
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un bénéficiaire")
    public ResponseEntity<BeneficiaireResponse> modifier(
            @PathVariable Integer id,
            @RequestBody BeneficiaireRequest request) {

        return ResponseEntity.ok(
                beneficiaireServices.modifierBeneficiaire(id, request)
        );
    }

    // SUPPRESSION
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un bénéficiaire")
    public ResponseEntity<Void> supprimer(
            @PathVariable Integer id) {

        beneficiaireServices.supprimerBeneficiaire(id);

        return ResponseEntity.noContent().build();
    }
}
