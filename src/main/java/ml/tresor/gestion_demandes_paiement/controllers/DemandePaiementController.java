
package ml.tresor.gestion_demandes_paiement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import ml.tresor.gestion_demandes_paiement.dto.DemandePaiementRequest;
import ml.tresor.gestion_demandes_paiement.dto.DemandePaiementResponse;
import ml.tresor.gestion_demandes_paiement.services.DemandePaiementService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Tag(
        name = "Geston des demande des paiements ",
        description = "Toutes les operations relatives a la gestion des paiements"
)
@RequestMapping("/demandes-paiement")
@RequiredArgsConstructor
public class DemandePaiementController {

    private final DemandePaiementService demandePaiementService;

    // Créer une demande
    @PostMapping
    public ResponseEntity<DemandePaiementResponse> creerDemandePaiement(
            @Valid @RequestBody DemandePaiementRequest request) {

        DemandePaiementResponse response =
                demandePaiementService.creerDemandePaiement(request);

        URI location = URI.create(
                "/demandes-paiement/" + response.id()
        );

        return ResponseEntity
                .created(location)
                .body(response);
    }

    // Lister toutes les demandes
    @GetMapping
    public ResponseEntity<List<DemandePaiementResponse>> listerDemandesPaiement() {

        return ResponseEntity.ok(
                demandePaiementService.listerDemandesPaiement()
        );
    }

    // Consulter une demande par ID
    @GetMapping("/{id}")
    public ResponseEntity<DemandePaiementResponse> trouverDemandePaiement(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                demandePaiementService.trouverDemandePaiement(id)
        );
    }

    // Modifier une demande
    @PutMapping("/{id}")
    public ResponseEntity<DemandePaiementResponse> modifierDemandePaiement(
            @PathVariable Integer id,
            @Valid @RequestBody DemandePaiementRequest request) {

        return ResponseEntity.ok(
                demandePaiementService.modifierDemandePaiement(id, request)
        );
    }

    // Supprimer une demande
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerDemandePaiement(
            @PathVariable Integer id) {

        demandePaiementService.supprimerDemandePaiement(id);

        return ResponseEntity.noContent().build();
    }

    // Soumettre une demande
    @PostMapping("/{id}/soumettre")
    public ResponseEntity<DemandePaiementResponse> soumettreDemandePaiement(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                demandePaiementService.soumettreDemandePaiement(id)
        );
    }

    // Rechercher une demande
    @GetMapping("/search")
    public ResponseEntity<List<DemandePaiementResponse>> rechercherDemandes(
            @RequestParam String q) {

        return ResponseEntity.ok(
                demandePaiementService.rechercherDemandes(q)
        );
    }
}
