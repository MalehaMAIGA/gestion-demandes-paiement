package ml.tresor.gestion_demandes_paiement.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import ml.tresor.gestion_demandes_paiement.entities.Droit;
import ml.tresor.gestion_demandes_paiement.entities.UserRole;
import ml.tresor.gestion_demandes_paiement.entities.UserToken;
import ml.tresor.gestion_demandes_paiement.entities.Utilisateur;
import ml.tresor.gestion_demandes_paiement.repositories.UserTokenRepository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final UserTokenRepository userTokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Récupérer l'en-tête Authorization
        String authorization = request.getHeader("Authorization");

        // 2. Vérifier la présence du Bearer
        if (authorization != null
                && authorization.startsWith("Bearer ")) {

            String token = authorization.substring(7);

            // 3. Vérifier la validité du token
            LocalDateTime maintenant = LocalDateTime.now();

            UserToken userToken = userTokenRepository
                    .findByTokenAndNotBeforeLessThanEqualAndNotAfterGreaterThan(
                            token,
                            maintenant,
                            maintenant
                    )
                    .orElse(null);

            // 4. Identifier l'utilisateur
            if (userToken != null) {

                Utilisateur utilisateur = userToken.getUtilisateur();

                // 5. Récupérer les rôles et les droits
                Set<SimpleGrantedAuthority> authorities = new HashSet<>();

                for (UserRole role : utilisateur.getRole()) {

                    authorities.add(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + role.getCode()
                            )
                    );

                    for (Droit droit : role.getDroits()) {
                        authorities.add(
                                new SimpleGrantedAuthority(droit.getCode())
                        );
                    }
                }

                // 6. Créer l'authentification Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                utilisateur,
                                null,
                                authorities
                        );

                authentication.setDetails(
                        new org.springframework.security.web.authentication
                                .WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 7. Alimenter le contexte de sécurité
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // 8. Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}