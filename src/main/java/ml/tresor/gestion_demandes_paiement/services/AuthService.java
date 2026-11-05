package ml.tresor.gestion_demandes_paiement.services;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ml.tresor.gestion_demandes_paiement.dto.RegisterRequest;
import ml.tresor.gestion_demandes_paiement.entities.UserRole;
import ml.tresor.gestion_demandes_paiement.entities.Utilisateur;
import ml.tresor.gestion_demandes_paiement.repositories.UserRoleRepository;
import ml.tresor.gestion_demandes_paiement.repositories.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    //Inscription d'un utilisateur
    @Transactional

    public String register(RegisterRequest request) {
        // 400 : vérifier que la requête existe

        if(request == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'adresse email est obligatoire"
            );

        }
        // Normalisation les données
        String email = request.email().trim();
        String telephone = request.telephone() == null
                || request.telephone().isBlank() ? null : request.telephone().trim();

        //Vérifier si l'email existe déja
        if(utilisateurRepository.existsByEmail(email)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette adresse email est déja utilisée"
            );
        }

        if (telephone != null
            && utilisateurRepository.existsByTelephone(telephone)){
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Ce numero du telephone est déja utilisé"
                );
            }
            // Recherche le role GUEST
            UserRole roleGuest = userRoleRepository.findByCode("GUEST")
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Le role Guest n'est pas configuré dans l'application"));

            //Créer le nouvel utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(email);
            utilisateur.setTelephone(telephone);

            //Encoder le mot de passe avec Bcrypt

            utilisateur.setPwd(passwordEncoder.encode(request.password())
            );

            // Attribuer uniquement le role GUEST
            utilisateur.setRole(List.of(roleGuest));

            // Enregister l'utilisateur
            utilisateurRepository.save(utilisateur);

            //Retourner le message de confirmation

                return  "Inscription effectuée avec succes";
        }
    }

