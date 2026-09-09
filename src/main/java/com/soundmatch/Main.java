package com.soundmatch;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        String clientID = System.getenv( "SPOTIFY_CLIENT_ID");
        String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");
        String redirectUri = "http://127.0.0.1:8888/callback";
        String scopes = "user-top-read user-read-recently-played playlist-modify-public";


        SpotifyAuth auth = new SpotifyAuth(clientID, clientSecret, redirectUri, scopes);
        String accessToken = auth.login();

        System.out.println("Login feito!");

        TrackFetcher fetcher = new TrackFetcher(accessToken);
        List<Track> topTracks = fetcher.getTopTracks();
        Map<String, List<String>> artistGenres = fetcher.getArtistGenres();

    
        System.out.println("\nDados carregados!("+topTracks.size() + " musicas," + artistGenres.size() + "artistas)");

        Scanner scanner = new Scanner (System.in);

        System.out.print("\nPara qual atividade voce quer uma playlist? e com quantas musicas tera essa playlist?:");
        String activity = scanner.nextLine();

        String apiKey = System.getenv("GEMINI_API_KEY");
        PlaylistAI ai = new PlaylistAI(apiKey);

        System.out.println("\nGerando sugestao...\n");
        String suggestion = ai.suggestPlaylist(topTracks, artistGenres, activity);


        System.out.println("=========== PLAYLIST SUGERIDA =============");
        System.out.println(suggestion);




        System.out.println("\n===== SUAS TOP TRACKS =====");
        for (int i = 0; i < topTracks.size(); i++) {
            System.out.println((i + 1) + ". " + topTracks.get(i));
        }



        List<String> topArtists = fetcher.getTopArtists();

        System.out.println("=====================SEUS TOP ARTISTAS==================");
        for (int i = 0; i<topArtists.size(); i++) {
        System.out.println((i+1) + ". " + topArtists.get(i));
        



        }
    }
}