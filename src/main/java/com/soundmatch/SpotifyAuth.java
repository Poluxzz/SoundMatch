package com.soundmatch;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.net.URI;
import java.awt.Desktop;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class SpotifyAuth {

    private String clientID;
    private String clientSecret;
    private String redirectUri;
    private String scopes;

    
    public SpotifyAuth(String clientID, String clientSecret, String redirectUri, String scopes) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
    }

    
    public String login() throws Exception {

        
        final String[] tokenHolder = new String[1];

        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);

        server.createContext("/callback", (HttpExchange exchange) -> {

            String query = exchange.getRequestURI().getQuery();

            String resposta = "Pode fechar essa aba, deu tudo certo!";
            exchange.sendResponseHeaders(200, resposta.length());
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();

            try {
                String code = query.split("=")[1];
                String body = "grant_type=authorization_code"
                    + "&code=" + code
                    + "&redirect_uri=" + redirectUri
                    + "&client_id=" + clientID
                    + "&client_secret=" + clientSecret;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest tokenRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://accounts.spotify.com/api/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

                HttpResponse<String> tokenResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());

                JSONObject json = new JSONObject(tokenResponse.body());
                tokenHolder[0] = json.getString("access_token");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        server.start();

        String authUrl = "https://accounts.spotify.com/authorize"
            + "?client_id=" + clientID
            + "&response_type=code"
            + "&redirect_uri=" + redirectUri
            + "&scope=" + scopes.replace(" ", "%20");

        Desktop.getDesktop().browse(new URI(authUrl));

        while (tokenHolder[0] == null) {
            Thread.sleep(200);
        }

        server.stop(0);
        return tokenHolder[0];
    }
}