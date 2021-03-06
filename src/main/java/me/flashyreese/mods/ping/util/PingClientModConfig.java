package me.flashyreese.mods.ping.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;

public class PingClientModConfig {
    public final General GENERAL = new General();
    public final Visual VISUAL = new Visual();

    private File file;

    public static class General {
        public int pingAcceptDistance;
        public int pingDuration;
        public boolean sound;

        public General() {
            this.pingAcceptDistance = 64;
            this.pingDuration = 60;
            this.sound = true;
        }
    }

    public static class Visual {
        public int pingR;
        public int pingG;
        public int pingB;
        public boolean blockOverlay;

        public Visual() {
            this.pingR = 255;
            this.pingG = 0;
            this.pingB = 0;
            this.blockOverlay = true;
        }
    }

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    public static PingClientModConfig load(File file) {
        PingClientModConfig config;

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                config = gson.fromJson(reader, PingClientModConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Could not parse config", e);
            }
        } else {
            config = new PingClientModConfig();
        }

        config.file = file;
        config.writeChanges();

        return config;
    }

    public void writeChanges() {
        File dir = this.file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Could not create parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new RuntimeException("The parent file is not a directory");
        }

        try (FileWriter writer = new FileWriter(this.file)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException("Could not save configuration file", e);
        }
    }
}
