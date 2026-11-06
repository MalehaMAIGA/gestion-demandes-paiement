
package ml.tresor.gestion_demandes_paiement.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ml.tresor.gestion_demandes_paiement.dto.AuthRequest;
import ml.tresor.gestion_demandes_paiement.dto.AuthResponse;
import ml.tresor.gestion_demandes_paiement.dto.RegisterRequest;
import ml.tresor.gestion_demandes_paiement.entities.UserRole;
import ml.tresor.gestion_demandes_paiement.entities.UserToken;
import ml.tresor.gestion_demandes_paiement.entities.Utilisateur;
import ml.tresor.gestion_demandes_paiement.repositories.UserRoleRepository;
import ml.tresor.gestion_demandes_paiement.repositories.UserTokenRepository;
import ml.tresor.gestion_demandes_paiement.repositories.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    //Les dépendances injectées
    private final UtilisateurRepository utilisateurRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenRepository userTokenRepository;

    //  Configuration de la durée du token
    @Value("${app.security.token-duration-hours:10}")
    private long tokenDurationHours;


    //Méthode register

    @Transactional
    public String register(RegisterRequest request) {

        // Vérifier que la requête existe
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Les informations d'inscription sont obligatoires"
            );
        }

        // Vérifier l'adresse email
        if (request.email() == null || request.email().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'adresse email est obligatoire"
            );
        }

        //  Vérifier le mot de passe
        if (request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le mot de passe est obligatoire"
            );
        }

        //  Normaliser les données
        String email = request.email().trim().toLowerCase();

        String telephone = request.telephone() == null
                || request.telephone().isBlank()
                ? null
                : request.telephone().trim();

        //  Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette adresse email est déjà utilisée"
            );
        }

        // Vérifier si le téléphone existe déjà
        if (telephone != null
                && utilisateurRepository.existsByTelephone(telephone)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce numéro de téléphone est déjà utilisé"
            );
        }

        //  Rechercher le rôle GUEST
        UserRole roleGuest = userRoleRepository.findByCode("GUEST")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Le rôle GUEST n'est pas configuré dans l'application"
                ));

        // Créer le nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setEmail(email);
        utilisateur.setTelephone(telephone);

        // Encoder le mot de passe avec BCrypt
        utilisateur.setPwd(
                passwordEncoder.encode(request.password())
        );

        // Attribuer uniquement le rôle GUEST
        utilisateur.setRole(List.of(roleGuest));

        //  Enregistrer l'utilisateur dans la base de données
        utilisateurRepository.save(utilisateur);

        //  Retourner le message de confirmation
        return "Inscription effectuée avec succès";
    }
// Ajouter la methode login
    @Transactional
    public AuthResponse login(AuthRequest request) {
        //Vérifier les données reçues
        if (request == null
                || request.login() == null
                || request.login().isBlank()
                || request.password() == null
                || request.password().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Identifiant est mot de passe est obligatoire"
            );
        }


        //  Rechercher l'utilisateur par email ou téléphone
        String identifiant = request.login().trim();

        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(identifiant.toLowerCase())
                .or(() -> utilisateurRepository.findByTelephone(identifiant))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Identifiants invalides"
                ));

        // 3. Vérifier le mot de passe avec BCrypt
        if (!passwordEncoder.matches(
                request.password(),
                utilisateur.getPwd())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Identifiants invalides"
            );
        }

        // 4. Générer un token UUID
        String token = UUID.randomUUID().toString();

        // 5. Définir sa période de validité
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime expiration =
                maintenant.plusHours(tokenDurationHours);

        // 6. Créer et enregistrer le token
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setUtilisateur(utilisateur);
        userToken.setNotBefore(maintenant);
        userToken.setNotAfter(expiration);

        userTokenRepository.save(userToken);

        // 7. Retourner la réponse
        return new AuthResponse(
                token,
                expiration,
                "Connexion réussie"
        );
    }
}
