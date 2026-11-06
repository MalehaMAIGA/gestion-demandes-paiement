
package ml.tresor.gestion_demandes_paiement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ml.tresor.gestion_demandes_paiement.dto.AuthRequest;
import ml.tresor.gestion_demandes_paiement.dto.AuthResponse;
import ml.tresor.gestion_demandes_paiement.dto.RegisterRequest;
import ml.tresor.gestion_demandes_paiement.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


     //Inscription d'un nouvel utilisateur.

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        String message = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request){
         AuthResponse response = authService.login(request);

         return ResponseEntity.ok(response);
    }

}
