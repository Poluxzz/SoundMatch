package com.soundmatch;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class TrackFetcher {

    private String accessToken;

    public TrackFetcher(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<Track> getTopTracks() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.spotify.com/v1/me/top/tracks"))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        JSONArray items = json.getJSONArray("items");

        List<Track> tracks = new ArrayList<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject track = items.getJSONObject(i);
            String trackName = track.getString("name");

            JSONArray artists = track.getJSONArray("artists");
            String artistName = artists.getJSONObject(0).getString("name");

            tracks.add(new Track(trackName, artistName));
        }

        return tracks;
    }

    public List<String> getTopArtists() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.spotify.com/v1/me/top/artists"))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        JSONArray items = json.getJSONArray("items");

        List<String> artists = new ArrayList<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject artist = items.getJSONObject(i);
            String artistName = artist.getString("name");

            JSONArray genres = artist.optJSONArray("genres");
            String genresText = (genres == null || genres.isEmpty()) ? "sem gênero listado" : genres.join(", ").replace("\"", "");

            artists.add(artistName + " (" + genresText + ")");
        }

        return artists;
    }

    public Map<String, List<String>> getArtistGenres() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.spotify.com/v1/me/top/artists"))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        JSONArray items = json.getJSONArray("items");

        Map<String, List<String>> genresByArtist = new HashMap<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject artist = items.getJSONObject(i);
            String artistName = artist.getString("name");

            JSONArray genresArray = artist.optJSONArray("genres");
            List<String> genresList = new ArrayList<>();

            if (genresArray != null) {
                for (int j = 0; j < genresArray.length(); j++) {
                    genresList.add(genresArray.getString(j));
                }
            }

            genresByArtist.put(artistName, genresList);
        }

        return genresByArtist;
    }
}