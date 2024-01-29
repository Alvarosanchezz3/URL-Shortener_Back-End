package com.alvaro.URLShortener.controller;

import com.alvaro.URLShortener.model.UrlInfo;
import com.alvaro.URLShortener.model.UrlShortenRequest;
import com.alvaro.URLShortener.repository.UrlInfoRepository;
import com.alvaro.URLShortener.repository.UserRepository;
import com.alvaro.URLShortener.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins= {"http://localhost:4200", "https://shortener-urls.netlify.app"})
@RestController
@RequestMapping("/api/url")
public class UrlShortenerController {

    @Autowired
    private UrlInfoRepository urlInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUrlsByUserEmail(@PathVariable String email) {
        // Obtener información del usuario por correo electrónico
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"No se encontró un usuario con el correo electrónico proporcionado\"}");
        }

        // Obtener todas las URL asociadas al usuario
        List<UrlInfo> userUrls = urlInfoRepository.findByUser(user);

        return ResponseEntity.ok(userUrls);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUrl(@RequestBody UrlShortenRequest urlShortenRequest) {
        // Obtener información del usuario por correo electrónico
        User user = userRepository.findByEmail(urlShortenRequest.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"No se encontró un usuario con el correo electrónico proporcionado\"}");
        }

        // Verificar si la URL larga ya está en la base de datos para este usuario
        UrlInfo existingUrlInfo = urlInfoRepository.findByLongUrlAndUser(urlShortenRequest.getLongUrl(), user);

        if (existingUrlInfo != null) {
            urlInfoRepository.delete(existingUrlInfo);
            return ResponseEntity.ok("{\"status\": \"La URL ha sido eliminada exitosamente\"}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"No se encontró una URL asociada al usuario con la URL proporcionada\"}");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> changeUrlnfo (@RequestBody UrlShortenRequest urlShortenRequest) {
        // Obtener información del usuario por correo electrónico
        User user = userRepository.findByEmail(urlShortenRequest.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario null");
        }

        // Verificar si la URL larga ya está en la base de datos para este usuario
        UrlInfo existingUrlInfo = urlInfoRepository.findByLongUrlAndUser(urlShortenRequest.getLongUrl(), user);

        if (existingUrlInfo != null) {
            existingUrlInfo.setDescription(urlShortenRequest.getDescription());
            urlInfoRepository.save(existingUrlInfo);

            return ResponseEntity.ok("{\"message\": \"URL updated successfully\"}");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody UrlShortenRequest urlShortenRequest) {
        // Obtener información del usuario por correo electrónico
        User user = userRepository.findByEmail(urlShortenRequest.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"No se encontró un usuario con el correo electrónico proporcionado\"}");
        }

        // Verificar si la URL larga ya está en la base de datos para este usuario
        UrlInfo existingUrlInfo = urlInfoRepository.findByLongUrlAndUser(urlShortenRequest.getLongUrl(), user);

        if (existingUrlInfo != null) {
            // La URL larga ya tiene una URL corta asociada para este usuario
            return ResponseEntity.ok("{\"status\": \"La URL larga ya tiene una URL corta asociada para este usuario\"}");
        } else {
            // Generar una nueva URL corta
            String shortUrl = generateShortUrl();

            // Crear una nueva entrada en la base de datos
            UrlInfo urlInfo = new UrlInfo();
            urlInfo.setLongUrl(urlShortenRequest.getLongUrl());
            urlInfo.setShortUrl(shortUrl);
            urlInfo.setDescription(urlShortenRequest.getDescription());
            urlInfo.setUser(user);

            urlInfoRepository.save(urlInfo);

            return ResponseEntity.ok("{\"shortUrl\": \"" + shortUrl + "\"}");
        }
    }

    @GetMapping("/redirect/{shortUrlID}")
    public ResponseEntity<?> redirectUrl(@PathVariable String shortUrlID) {
        String shortUrl = "http://localhost:4200/" + shortUrlID;
        
        // Buscar la URLInfo asociada a la URL corta
        UrlInfo urlInfo = urlInfoRepository.findByShortUrl(shortUrl);

        if (urlInfo != null) {
            // Devolver a la URL larga asociada

            return ResponseEntity.status(HttpStatus.OK)
                    .body(urlInfo.getLongUrl());
        } else {
            // No se encontró la URL corta
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se encontró la url corta");
        }
    }

    private String generateShortUrl() {
        return "http://localhost:4200/" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }

    @PostMapping("/register")
    public ResponseEntity<?> crearUser(@RequestBody User data) {
        String email = data.getEmail();

        // Verificar si ya existe un usuario con el mismo correo electrónico
        User existingUser = userRepository.findByEmail(email);

        if (existingUser != null && existingUser.getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("{\"Status\": \"Ya existe un usuario con este correo electrónico\"}");
        } else {
            // Si no existe, crear un nuevo usuario
            User newUser = new User();
            newUser.setEmail(email);

            // Guardar el nuevo usuario en el repositorio
            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("{\"Status\": \"Usuario creado correctamente\"}");
        }
    }
}

