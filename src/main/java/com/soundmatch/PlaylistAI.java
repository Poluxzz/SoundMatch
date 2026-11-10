package com.soundmatch;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlaylistAI {

    private String apiKey;

    public PlaylistAI(String apiKey) {
        this.apiKey = apiKey;
    }

    public String suggestPlaylist(List<Track> topTracks, Map<String, List<String>> artistGenres, String activity) throws Exception {

        StringBuilder prompt = new StringBuilder();
        prompt.append("O usuário tem estas músicas favoritas:\n");

        for (Track track : topTracks) {
            prompt.append("- ").append(track.name).append(" (").append(track.artist).append(")\n");
        }

        prompt.append("\nGêneros dos artistas favoritos:\n");
        for (String artist : artistGenres.keySet()) {
            prompt.append("- ").append(artist).append(": ").append(artistGenres.get(artist)).append("\n");
        }

        prompt.append("\nO usuário quer uma playlist para com essa quantidade de musicas: ").append(activity).append("\n");
        prompt.append("Crie a playlist com o numero que o usuario escolheu de musicas e tambem que faça sentido a atividade que ele vai fazer, faça apenas o top de musicas sem explicacao extra");

        JSONObject part = new JSONObject();
        part.put("text", prompt.toString());
        

        JSONArray parts = new JSONArray();
        parts.put(part);

        JSONObject content = new JSONObject();
        content.put("parts", parts);


        JSONArray contents = new JSONArray();
        contents.put(content);

        JSONObject body = new JSONObject();
        body.put("contents", contents);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        


       

        JSONObject responseJson = new JSONObject(response.body());
        JSONArray candidates = responseJson.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONObject responseContent = firstCandidate.getJSONObject("content");
        JSONArray responseParts = responseContent.getJSONArray("parts");
        String resultText = responseParts.getJSONObject(0).getString("text");


        return resultText;
        
    }
}