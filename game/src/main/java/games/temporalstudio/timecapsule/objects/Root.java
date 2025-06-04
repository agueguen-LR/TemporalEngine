package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;

import javax.xml.namespace.QName;

public class Root {
    private GameObject gameObject;

    public Root(String name,float oX,float oY, boolean north, boolean south, boolean west, boolean east, int length) {
        gameObject = new GameObject(name );


    }
}
