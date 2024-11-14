package com.devoxx;

import io.github.cdimascio.dotenv.Dotenv;

public class BlueSkyConfig {
    private static final Dotenv dotenv = Dotenv.configure()
        .ignoreIfMissing()
        .load();

    private final String username;
    private final String appPassword;

    private static BlueSkyConfig instance;

    private BlueSkyConfig() {
        this.username = getRequiredEnvVariable("BLUESKY_USERNAME");
        this.appPassword = getRequiredEnvVariable("BLUESKY_APP_PASSWORD");
    }

    public static BlueSkyConfig getInstance() {
        if (instance == null) {
            instance = new BlueSkyConfig();
        }
        return instance;
    }

    private static String getRequiredEnvVariable(String name) {
        String value = dotenv.get(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                String.format("Required environment variable '%s' is not set. " +
                    "Please check your .env file or system environment variables.", name));
        }
        return value;
    }

    public String getUsername() {
        return username;
    }

    public String getAppPassword() {
        return appPassword;
    }
}
