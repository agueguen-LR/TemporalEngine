package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;

import java.util.ArrayList;
import java.util.Collection;

public class Inventory{

    protected ArrayList<Object> content;
    private int LIMITE=10;


    public Inventory() {
        content = new ArrayList<>();
    }


    public void addOne(Object item){
        //if (item.isStockable()) {
        if(content.size()+1 <= LIMITE) {
            content.add(item);
        }
            //return true;}
        //return false;
    }
    public boolean removeOne(Object item){return content.remove(item);}

    public void removeAll(){content.clear();}

    public boolean addAll(ArrayList<Object> items){
        /*for (T item : items) {
            if(!item.isStockable())return false;}*/
        if(content.size()+items.size() <= LIMITE) {
            return content.addAll(items);}
        return false;
    }
}
