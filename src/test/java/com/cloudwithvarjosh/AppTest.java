package com.java-maven;

import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    @Test
    void readsUsernameFromInput() {
        ByteArrayInputStream input =
                new ByteArrayInputStream("josh\n".getBytes(StandardCharsets.UTF_8));

        assertEquals("josh", App.readUsername(input));
    }

    @Test
    void usesGuestWhenInputIsEmpty() {
        assertEquals("guest", App.readUsername(new ByteArrayInputStream(new byte[0])));
    }

    @Test
    void greetsAdminAndOtherUsers() {
        ByteArrayInputStream input =
                new ByteArrayInputStream("admin\n".getBytes(StandardCharsets.UTF_8));

        assertEquals("Welcome, admin!", App.greeting(App.readUsername(input)));
        assertEquals("Hello, josh", App.greeting("josh"));
    }

    @Test
    void servesBrandHomepage() throws Exception {
        HttpServer server = App.start(
                new ByteArrayInputStream("admin\n".getBytes(StandardCharsets.UTF_8)), 0);
        try {
            URI uri = URI.create("http://localhost:" + server.getAddress().getPort() + "/");
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            assertTrue(response.headers().firstValue("Content-Type")
                    .orElse("").startsWith("text/html; charset=UTF-8"));
            assertEquals(
                    "<h1>Cloud With VarJosh</h1><p>Simple DevSecOps Demo App</p>",
                    response.body());
        } finally {
            server.stop(0);
        }
    }
}
