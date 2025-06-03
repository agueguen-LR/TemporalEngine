package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;

public class Chest implements TimeObject {
    private GameObject gameObject;
    private InventoryObject inventory;
    private boolean open;

    public Chest(){
        this.open = false;
    }

    public void openChest(){
        if (!open){
            System.out.println("Le coffre est ouvert.");
            open = true;
        }
    }

    public GameObject getGameObject(){
        return gameObject;
    }

}
