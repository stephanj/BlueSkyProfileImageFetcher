package com.devoxx;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.json.JSONObject;

public class BlueSkyProfileImageFetcher {
    private static final String API_BASE_URL = "https://bsky.social/xrpc/";
    private static final String DEFAULT_DOMAIN = "bsky.social";

    private static final BlueSkyConfig config = BlueSkyConfig.getInstance();

    private static String accessJwt = null;

    // Create a reusable HttpClient
    private static final HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    /**
     * Authenticate with BlueSky and get an access token
     */
    private static void authenticate() throws IOException, InterruptedException {
        URI uri = URI.create(API_BASE_URL + "com.atproto.server.createSession");

        // Create authentication payload
        JSONObject authData = new JSONObject()
            .put("identifier", config.getUsername())
            .put("password", config.getAppPassword());

        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(authData.toString()))
            .build();

        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Authentication failed. HTTP error code: " +
                response.statusCode());
        }

        // Parse access token
        JSONObject jsonResponse = new JSONObject(response.body());
        accessJwt = jsonResponse.getString("accessJwt");
    }

    public static String getProfileImage(String handle) throws IOException, InterruptedException {
        if (handle == null || handle.trim().isEmpty()) {
            throw new IllegalArgumentException("Handle cannot be null or empty");
        }

        // Authenticate if we don't have an access token
        if (accessJwt == null) {
            authenticate();
        }

        // Format and encode handle
        String formattedHandle = formatHandle(handle.trim());
        String encodedHandle = URLEncoder.encode(formattedHandle, StandardCharsets.UTF_8);

        // First request to resolve handle
        URI resolveUri = URI.create(API_BASE_URL +
            "com.atproto.identity.resolveHandle?handle=" + encodedHandle);

        HttpRequest resolveRequest = HttpRequest.newBuilder()
            .uri(resolveUri)
            .header("Authorization", "Bearer " + accessJwt)
            .header("Accept", "application/json")
            .GET()
            .build();

        HttpResponse<String> resolveResponse = httpClient.send(resolveRequest,
            HttpResponse.BodyHandlers.ofString());

        if (resolveResponse.statusCode() != 200) {
            throw new IOException("Failed to resolve handle. HTTP error code: " +
                resolveResponse.statusCode());
        }

        // Parse DID
        JSONObject jsonResponse = new JSONObject(resolveResponse.body());
        String did = jsonResponse.getString("did");

        // Second request to get profile
        URI profileUri = URI.create(API_BASE_URL +
            "app.bsky.actor.getProfile?actor=" + did);

        HttpRequest profileRequest = HttpRequest.newBuilder()
            .uri(profileUri)
            .header("Authorization", "Bearer " + accessJwt)
            .header("Accept", "application/json")
            .GET()
            .build();

        HttpResponse<String> profileResponse = httpClient.send(profileRequest,
            HttpResponse.BodyHandlers.ofString());

        if (profileResponse.statusCode() != 200) {
            throw new IOException("Failed to get profile. HTTP error code: " +
                profileResponse.statusCode());
        }

        // Parse and return avatar URL
        JSONObject profile = new JSONObject(profileResponse.body());
        return profile.has("avatar") ? profile.getString("avatar") : null;
    }

    private static String formatHandle(String handle) {
        if (!handle.contains(".")) {
            return handle + "." + DEFAULT_DOMAIN;
        }
        return handle;
    }

    /**
     * Example usage with better error handling and resource management
     */
    public static void main(String[] args) {
        try {
            String handle = "devoxx.bsky.social";
            String imageUrl = getProfileImage(handle);

            if (imageUrl != null) {
                System.out.println("Profile image URL: " + imageUrl);

                // Optional: Verify the image URL is accessible
                HttpRequest imageRequest = HttpRequest.newBuilder()
                    .uri(URI.create(imageUrl))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

                HttpResponse<Void> imageResponse = httpClient.send(imageRequest,
                    HttpResponse.BodyHandlers.discarding());

                if (imageResponse.statusCode() == 200) {
                    System.out.println("Image URL is accessible");
                } else {
                    System.out.println("Image URL returned status code: " +
                        imageResponse.statusCode());
                }
            } else {
                System.out.println("No profile image found for handle: " + handle);
            }
        } catch (IOException e) {
            System.err.println("Error fetching profile image: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Request interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
