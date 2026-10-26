package com.cloudwithvarjosh;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final int PORT = 8080;
    private static final String BRAND_HTML =
            "<h1>Cloud With VarJosh</h1><p>Simple DevSecOps Demo App</p>";

    public static void main(String[] args) throws IOException {
        start(System.in, PORT);
    }

    static HttpServer start(InputStream input, int port) throws IOException {
        LOGGER.info("Starting CVWJ DevSecOps Demo...");
        LOGGER.info("Enter username:");
        String username = readUsername(input);
        LOGGER.info(greeting(username));

        HttpServer server = createServer(port);
        LOGGER.info("app running on port " + server.getAddress().getPort());
        server.start();
        return server;
    }

    static String readUsername(InputStream input) {
        try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8)) {
            return scanner.hasNextLine() ? scanner.nextLine() : "guest";
        }
    }

    static String greeting(String username) {
        return "admin".equals(username) ? "Welcome, admin!" : "Hello, " + username;
    }

    private static HttpServer createServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> {
            byte[] body = BRAND_HTML.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(body);
            }
        });
        return server;
    }
}
