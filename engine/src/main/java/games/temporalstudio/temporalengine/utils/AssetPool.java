

package games.temporalstudio.temporalengine.utils;

import java.io.File;

import java.util.HashMap;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author Adnan FAIZE
 */
public class AssetPool {
    private static HashMap<String, AssetPoolObject> objects = new HashMap<>();

    /**
     * Add an object with a name as its identifier.
     * @param name name of the object to add to the asset pool
     * @param object object to add to the asset pool
     */
    public static void addObject(String name, AssetPoolObject object) {
        File file = new File(name);
        if (!AssetPool.objects.containsKey(file.getAbsolutePath())) {
            AssetPool.objects.put(file.getAbsolutePath(), object);
        }
    }

    /**
     * Removes the mapping for the specified object from the asset pool if present.
     * @param name name of the object to remove
     * @return true if object was present ; false if not
     */
    public static boolean removeObject(String name) {
        if (AssetPool.objects.containsKey(name)) {
            AssetPool.objects.remove(name);
            return true;
        }

        return false;
    }

    /**
     * Get an object from the asset pool if present.
     * @param name name of the object to retrieve
     * @param type type of the objects to retrieve
     * @return object corresponding in the asset pool
     * @param <T> returned object type
     * @throws NoSuchElementException thrown if the object is not present in the asset pool
     */
    public static <T extends AssetPoolObject> T getObject(String name, Class<T> type) throws NoSuchElementException {
        File file = new File(name);
        if (!AssetPool.objects.containsKey(file.getAbsolutePath())) {
            throw new NoSuchElementException(file.getAbsolutePath());
        }
        return type.cast(AssetPool.objects.get(file.getAbsolutePath()));
    }

    /**
     * Get all objects of the set type present in the asset pool
     * @param type type of the objects to retrieve
     * @return a collection of objects
     */
    public static <T extends AssetPoolObject> Collection<T> getObjectsOfType(Class<T> type) {
        return AssetPool.objects.values().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }
}
