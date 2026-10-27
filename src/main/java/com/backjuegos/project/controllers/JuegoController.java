package com.backjuegos.project.controllers;

import com.backjuegos.project.entities.Juego;
import com.backjuegos.project.services.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @GetMapping
    public List<Juego> obtenerJuegos() {
        return juegoService.listarJuegos();
    }

    @PostMapping
    public Juego guardarJuego(@RequestBody Juego juego) {
        return juegoService.guardarJuego(juego);
    }
}