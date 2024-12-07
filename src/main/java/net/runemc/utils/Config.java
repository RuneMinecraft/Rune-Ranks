package net.runemc.utils;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.file.Files;

public final class Config {
    private static final Yaml YAML;

    static {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true);
        YAML = new Yaml(new Constructor(new LoaderOptions()), representer, options);
    }

    private Config() {}

    public static <T> T load(@NotNull File file, @NotNull Class<T> clazz) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        try (Reader reader = new FileReader(file)) {
            return YAML.loadAs(reader, clazz);
        }
    }

    public static void save(@NotNull File file, @NotNull Object data) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.createDirectories(file.getParentFile().toPath());
        try (Writer writer = new FileWriter(file)) {
            YAML.dump(data, writer);
        }
    }
}