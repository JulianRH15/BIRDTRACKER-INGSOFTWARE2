package co.edu.unbosque.BirdTracker.adaptador.entrada.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String abrirDocumentacion() {
        return "redirect:/swagger-ui/index.html";
    }
}
