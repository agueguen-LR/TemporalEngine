package games.temporalstudio.temporalengine.data;

public interface DataObject {
    String getDataId();

    default String getDataPath() {
        return this.getClass().hashCode() + "/" + this.getDataId().hashCode();
    }
}
