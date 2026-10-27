package com.backjuegos.project.controllers;
import com.backjuegos.project.services.DevirScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scraping")
public class DevirController {

    @Autowired
    private DevirScraperService devirScraperService;

    @GetMapping("/devir")
    public ResponseEntity<String> ejecutarScraping() {
        devirScraperService.importarJuegosDeDevir();
        return ResponseEntity.ok("Proceso de scraping de Devir ejecutado correctamente.");
    }
}