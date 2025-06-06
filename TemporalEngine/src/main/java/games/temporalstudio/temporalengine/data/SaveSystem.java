package games.temporalstudio.temporalengine.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.serde.ObjectDeserializer;
import com.electronwill.nightconfig.core.serde.ObjectSerializer;
import com.electronwill.nightconfig.toml.TomlWriter;

import org.jetbrains.annotations.NotNull;

// doc & lib used to support toml serialization & deserialization
// https://javadoc.io/doc/com.electronwill.night-config/core/latest/index.html
// https://github.com/TheElectronWill/night-config?tab=readme-ov-file

/**
 * @author Adnan FAIZE
 */
public final class SaveSystem {
//    /**
//     * Name of the file containing all info about the different game saves.
//     */
//    public static final String MASTER_FILE = String.valueOf("master".hashCode());

    private static final String DATA_PATH = "./data/";

    private static String resolvePath(String path) {
        Path filename = Paths.get(path).getFileName();
        Path savePath = Paths.get(DATA_PATH, path.replace(filename.toString(), ""));

        if (!Files.exists(savePath)) {
            try { Files.createDirectories(savePath); }
            catch (IOException e) { throw new RuntimeException(e); }
        }

        return savePath.resolve((filename + ".toml")).toString();
    }

    /**
     * Saves the data to the file as a TOML.
     * @param data data to save to file
     */
    public static void save(@NotNull DataObject data) {
        save(data.getDataPath(), data);
    }

    /**
     * Saves the data to the file as a TOML.
     * @param path path to the file needed to be written
     * @param data data to save to the file
     */
    public static void save(@NotNull String path, @NotNull DataObject data) {
        try (FileWriter writer = new FileWriter(resolvePath(path))) {
            TomlWriter tWriter = new TomlWriter();
            Config config = Config.inMemory();
            ObjectSerializer.standard().serializeFields(data, config);
            tWriter.write(config, writer);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    /**
     * Saves all the data to their corresponding file as a TOML.
     * @param data data array to save to the files
     */
    public static void saveAll(@NotNull DataObject[] data) {
        for (DataObject dataObject : data) {
            save(dataObject);
        }
    }

//    /**
//     * Saves all data to a same file using data path as key for the object saved.
//     * @param path path to the file needed to be written
//     * @param data data array to write to the file (can be different types of)
//     */
//    public static void saveAllToOneFile(@NotNull String path, @NotNull DataObject[] data) {
//        try (FileWriter writer = new FileWriter(resolvePath(path));
//             FileConfig endConfig = FileConfig.of(resolvePath(path))) {
//            TomlWriter tWriter = new TomlWriter();
//            for (DataObject dataObject : data) {
//                Config config = Config.inMemory();
//                ObjectSerializer.standard().serializeFields(dataObject, config);
//                endConfig.set(dataObject.getDataPath().replace("/","."), config);
//            }
//            tWriter.write(endConfig, writer);
//        } catch (NoFormatFoundException | IOException e) { throw new RuntimeException(e); }
//    }

    /**
     * Loads a TOML file to create a new object of the type provided
     * @param path path to the file needed to be read
     * @param destinationSupplier constructor for the class T (empty protected constructor needed)
     * @return new object of type T
     * @param <T> returned object type
     * @throws FileNotFoundException if the path provided shows no file
     */
    public static <T extends DataObject> T load(@NotNull String path, @NotNull Supplier<T> destinationSupplier) throws FileNotFoundException {
        Path savePath = Paths.get(SaveSystem.DATA_PATH, (path + ".toml"));
        if (!Files.exists(savePath)) { throw new FileNotFoundException(savePath.toString()); }

        try (FileConfig config = FileConfig.of(savePath)) {
            config.load();
            // TODO - WARNING : there is nothing to check if the type contained in the TOML actually is the same type as the supplier
            return ObjectDeserializer.standard().deserializeFields(config, destinationSupplier);
        } catch (RuntimeException e) { throw new RuntimeException("Wrong file format (must be toml : provided" + savePath.getFileName() + ") ; " +  e); }
    }

    /**
     * Loads all TOML files to create an array with new objects of the type provided. (only checks files saved to the default path)
     * @param type type of the data needed to be loaded
     * @param destinationSupplier constructor for the class T (empty protected constructor needed)
     * @return arraylist of type T with the data objects created
     * @param <T> returned object type
     * @throws FileNotFoundException if there is no data about the class stored
     */
    public static <T extends DataObject> ArrayList<T> load(@NotNull Class<T> type, @NotNull Supplier<T> destinationSupplier) throws FileNotFoundException {
        Path savesPath = Paths.get(SaveSystem.DATA_PATH, String.valueOf(type.hashCode()));
        if (!Files.exists(savesPath)) { throw new FileNotFoundException(savesPath.toString()); }

        ArrayList<T> data = new ArrayList<>();

        for (File file : Objects.requireNonNull(savesPath.toFile().listFiles())) {
            Path path = file.toPath();
            if (!path.toString().endsWith(".toml")) { continue; }
            try (FileConfig config = FileConfig.of(path)) {
                config.load();
                data.add(ObjectDeserializer.standard().deserializeFields(config, destinationSupplier));
            }
        }

        return data;
    }
}
