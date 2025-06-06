package games.temporalstudio.temporalengine.data;

/**
 * @author Adnan FAIZE
 */
public interface DataObject {
    String getDataId();

    default String getDataPath() {
        return this.getClass().hashCode() + "/" + this.getDataId().hashCode();
    }
}
