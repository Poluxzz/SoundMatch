package soundmatch;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.awt.Desktop;


public class Main {
 public static void main(String[] args) throws Exception{

   HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);

   server.createContext ("/callback", (HttpExchange exchange) -> {

    String query = exchange.getRequestURI().getQuery();
    System.out.println("Recebido:" + query);

    String resposta = "Pode fechar essa aba, deu tudo certo!";
    exchange.sendResponseHeaders(200, resposta.length());

    OutputStream os = exchange.getResponseBody();
    os.write(resposta.getBytes());
    os.close();

  });

  server.start();
  System.out.println("Servidor rodando na porta 8888, esperando o Spotify...");

  String clientID = "5a7cf11c8f414a748498172acf1e8a4e";
  String redirectUri = "http://127.0.0.1:8888/callback";
  String scopes = "user-top-read user-read-recently-played playlist-modify-public";

  String authUr1 = "https://accounts.spotify.com/authorize"
  + "?client_id=" + clientID
  + "&response_type=code"
  + "&redirect_uri=" + redirectUri
  + "&scope=" + scopes.replace(" ", "%20");

  Desktop.getDesktop().browse(new URI(authUr1));


  }


  }


 









