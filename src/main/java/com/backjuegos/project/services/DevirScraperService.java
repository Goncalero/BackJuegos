package com.backjuegos.project.services;

import com.backjuegos.project.entities.Juego;
import com.backjuegos.project.repositories.JuegoRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class DevirScraperService {

    @Autowired
    private JuegoRepository juegoRepository;

    public void importarJuegosDeDevir() {
        try {
            String url = "https://devir.es/proximos-lanzamientos";
            
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();

            System.out.println("Página de próximos lanzamientos conectada con éxito.");

            Elements contenedores = doc.select("div, article, section");
            
            // Set para evitar guardar juegos repetidos en la misma ejecución
            Set<String> nombresProcesados = new HashSet<>();

            for (Element contenedor : contenedores) {
                String textoCompleto = contenedor.text();
                
                // Verificamos que sea un bloque de juego válido
                if (textoCompleto.contains("Autor:") && textoCompleto.contains("Precio:")) {
                    
                    // Buscamos el título del juego de forma precisa
                    String nombre = "";
                    Element tituloElem = contenedor.select("h2, h3, strong").first();
                    if (tituloElem != null) {
                        nombre = tituloElem.text().trim();
                    }

                    // Filtramos títulos no válidos, vacíos o ya procesados
                    if (nombre.isEmpty() || nombre.equalsIgnoreCase("NOVEDAD") || nombre.length() < 3) {
                        continue;
                    }

                    if (nombresProcesados.contains(nombre)) {
                        continue; // Si ya lo procesamos en un contenedor padre/hijo, lo saltamos
                    }

                    nombresProcesados.add(nombre);

                    // Buscamos una imagen que sea válida (que contenga http y formato de imagen)
                    String imagen = "";
                    Elements imges = contenedor.select("img");
                    for (Element img : imges) {
                        String src = img.attr("src");
                        if (src.contains("http") && (src.contains("amazonaws") || src.contains("media") || src.contains("jpg") || src.contains("png"))) {
                            imagen = src;
                            break; // Encontramos la imagen correcta del juego
                        }
                    }

                    // Extraemos los campos mediante las etiquetas de texto
                    String autor = extraerCampo(textoCompleto, "Autor:", "Ilustrador:");
                    String tipo = extraerCampo(textoCompleto, "Tipo:", "Edad:");
                    String jugadores = extraerCampo(textoCompleto, "Nº Jugadores:", "Tiempo de partida:");
                    String precio = extraerCampo(textoCompleto, "Precio:", null);

                    System.out.println("Guardando juego único: " + nombre);

                    // Creamos y guardamos el objeto limpio
                    Juego juego = new Juego();
                    juego.setNombre(nombre);
                    juego.setAutor(autor.isEmpty() ? "Devir" : autor);
                    juego.setTipo(tipo.isEmpty() ? "Juego de mesa" : tipo);
                    juego.setJugadores(jugadores.isEmpty() ? "N/D" : jugadores);
                    juego.setPrecio(precio.isEmpty() ? "0€" : precio);
                    juego.setImagen(imagen);

                    juegoRepository.save(juego);
                }
            }

            System.out.println("Proceso de importación finalizado con éxito. Juegos guardados: " + nombresProcesados.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extraerCampo(String texto, String inicio, String fin) {
        try {
            int startIndex = texto.indexOf(inicio);
            if (startIndex == -1) return "";
            startIndex += inicio.length();
            
            if (fin != null) {
                int endIndex = texto.indexOf(fin, startIndex);
                if (endIndex == -1) return texto.substring(startIndex).trim();
                return texto.substring(startIndex, endIndex).trim();
            } else {
                String sub = texto.substring(startIndex).trim();
                return sub.length() > 15 ? sub.substring(0, 15).trim() : sub;
            }
        } catch (Exception e) {
            return "";
        }
    }
}