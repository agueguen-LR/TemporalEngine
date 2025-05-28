package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;

import java.util.ArrayList;
import java.util.Collection;

public class Inventory<T> implements Component{

    protected ArrayList<T> content;


    public Inventory() {
        content = new ArrayList<>();
    }


    public void addOne(T item){
        //if (item.isStockable()) {
            content.add(item);
            //return true;}
        //return false;
    }
    public boolean removeOne(T item){return content.remove(item);}

    public void removeAll(){content.clear();}

    public boolean addAll(ArrayList<T> items){
        /*for (T item : items) {
            if(!item.isStockable())return false;}*/
        return content.addAll(items);
    }


    @Override
    public void update(LifeCycleContext context, float delta) {

    }

    @Override
    public void init(LifeCycleContext context) {
        this.removeAll();
    }

    @Override
    public void start(LifeCycleContext context) {

    }

    @Override
    public void destroy(LifeCycleContext context) {

    }
}
