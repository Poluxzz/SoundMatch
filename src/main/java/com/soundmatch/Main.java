package com.soundmatch;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        String clientID = "SPOTIFY_CLIENT_ID";
        String clientSecret = "SPOTIFY_CLIENT_SECRET";
        String redirectUri = "http://127.0.0.1:8888/callback";
        String scopes = "user-top-read user-read-recently-played playlist-modify-public";


        SpotifyAuth auth = new SpotifyAuth(clientID, clientSecret, redirectUri, scopes);
        String accessToken = auth.login();

        System.out.println("Login feito! Token: " + accessToken);

        TrackFetcher fetcher = new TrackFetcher(accessToken);
        List<String> topTracks = fetcher.getTopTracks();

    
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