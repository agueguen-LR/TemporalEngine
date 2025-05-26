package games.temporalstudio.temporalengine.data;

import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.function.Supplier;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.serde.ObjectDeserializer;
import com.electronwill.nightconfig.core.serde.ObjectSerializer;
import com.electronwill.nightconfig.toml.TomlWriter;

import org.jetbrains.annotations.NotNull;

public final class SaveSystem {
    public static final String DATA_PATH = "./data/";
    public static final String MASTER_FILE = "saves.toml";

    /**
     *
     * @param filename
     * @param data
     */
    public static void save(@NotNull String filename, @NotNull Data data) {
        Path path = Paths.get(DATA_PATH);
        if (!Files.exists(path)) {
            try { Files.createDirectories(path); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
        path = path.resolve(filename);

        try (FileWriter writer = new FileWriter(path.toString())) {
            TomlWriter tWriter = new TomlWriter();
            Config config = Config.inMemory();
            ObjectSerializer.standard().serializeFields(data, config);
            tWriter.write(config, writer);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    /**
     *
     * @param filename
     * @return
     */
    public static <T extends Data> T load(@NotNull String filename, @NotNull Supplier<T> destinationSupplier) {
        Path path = Paths.get(SaveSystem.DATA_PATH);
        if (!Files.exists(path)) { return null; }
        path = path.resolve(filename);

        try (FileConfig config = FileConfig.of(path)) {
            config.load();
            return ObjectDeserializer.standard().deserializeFields(config, destinationSupplier);
        } catch (RuntimeException e) { throw new RuntimeException(e); }
    }
}
