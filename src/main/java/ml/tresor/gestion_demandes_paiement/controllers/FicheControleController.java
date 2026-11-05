
package ml.tresor.gestion_demandes_paiement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ml.tresor.gestion_demandes_paiement.dto.FicheControleRequest;
import ml.tresor.gestion_demandes_paiement.dto.FicheControleResponse;
import ml.tresor.gestion_demandes_paiement.services.FicheControleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Tag(
        name = "Geston de Fiche Controle ",
        description = "Toutes les operations relatives aux fiche de controle"
)
@RequestMapping("/demandes-paiement/{demandeId}/fiche")
@RequiredArgsConstructor
public class FicheControleController {

    private final FicheControleService ficheControleService;

    // CREATION D'UNE FICHE DE CONTROLE
    @PostMapping
    public ResponseEntity<FicheControleResponse> creer(
            @PathVariable Integer demandeId,
            @RequestBody FicheControleRequest request) {

        FicheControleResponse response =
                ficheControleService.creer(demandeId, request);

        URI location = URI.create(
                "/demandes-paiement/" + demandeId + "/fiche"
        );

        return ResponseEntity
                .created(location)
                .body(response);
    }

    // CONSULTATION D'UNE FICHE DE CONTROLE
    @GetMapping
    public ResponseEntity<FicheControleResponse> consulter(
            @PathVariable Integer demandeId) {

        return ResponseEntity.ok(
                ficheControleService.consulter(demandeId)
        );
    }

    // MODIFICATION D'UNE FICHE DE CONTROLE
    @PutMapping
    public ResponseEntity<FicheControleResponse> modifier(
            @PathVariable Integer demandeId,
            @RequestBody FicheControleRequest request) {

        return ResponseEntity.ok(
                ficheControleService.modifier(demandeId, request)
        );
    }

    // SUPPRESSION D'UNE FICHE DE CONTROLE
    @DeleteMapping
    public ResponseEntity<Void> supprimer(
            @PathVariable Integer demandeId) {

        ficheControleService.supprimer(demandeId);

        return ResponseEntity.noContent().build();
    }
}
