package com.devoxx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BlueSkyProfileImageFetcherTest {

    @Test
    void testGetProfileImage() throws IOException, InterruptedException {
        // Use a known BlueSky handle for testing
        String handle = "devoxx.bsky.social";
        String imageUrl = BlueSkyProfileImageFetcher.getProfileImage(handle);

        assertNotNull(imageUrl, "Profile image URL should not be null");
        assertTrue(imageUrl.startsWith("https://"), "Image URL should be HTTPS");
    }

    @Test
    void testGetProfileImageWithInvalidHandle() {
        String invalidHandle = "nonexistent123456789.bsky.social";

        Exception exception = assertThrows(IOException.class, () -> {
            BlueSkyProfileImageFetcher.getProfileImage(invalidHandle);
        });

        assertTrue(exception.getMessage().contains("Failed to resolve handle"));
    }
}
