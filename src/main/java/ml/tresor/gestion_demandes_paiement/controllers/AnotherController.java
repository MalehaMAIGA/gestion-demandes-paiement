package ml.tresor.gestion_demandes_paiement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class AnotherController {

    @GetMapping

    public String sayHello(){
        return "Bonjour";
    }
}
