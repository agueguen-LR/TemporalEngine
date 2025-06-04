package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;

public class Following implements Component{

    private Medusa medusa;
    private Player player;;

    public Following(Medusa medusa, Player player) {
        this.medusa = medusa;
        this.player = player;
    }

    public void XmoveCloser(Player letruc, Medusa medusa, double distance){
        float centrex = letruc.getTransform().getPosition().x() + 0.5f;
        double dist=medusa.getTransform().getPosition().x - centrex;
        if (Math.abs(dist) >= distance){
            if (dist < 0) medusa.moveRight(60);
            else medusa.moveLeft(60);
        }
    }

    public void YmoveCloser(Player letruc,Medusa medusa, double distance){
        float centrey = letruc.getTransform().getPosition().y() + 1f;
        double dist= medusa.getTransform().getPosition().y - centrey;
        if (Math.abs(dist) >= distance){
            if (dist < 0) medusa.moveUp(60);
            else medusa.moveDown(60);
        }
    }

    public double distance(Player letruc, Medusa medusa){
        double xdist=medusa.getTransform().getPosition().x - letruc.getTransform().getPosition().x;
        double ydist=medusa.getTransform().getPosition().y - letruc.getTransform().getPosition().y;
        return Math.sqrt(xdist*xdist+ydist*ydist);
    }

    @Override
    public void update(LifeCycleContext context, float delta) {
        if (distance(player, medusa) >= medusa.getMaxDistance()) {
            XmoveCloser(player,medusa, medusa.getMaxDistance());
            YmoveCloser(player, medusa,medusa.getMaxDistance());
        }
    }

    @Override
    public void init(LifeCycleContext context) {

    }

    @Override
    public void start(LifeCycleContext context) {

    }

    @Override
    public void destroy(LifeCycleContext context) {

    }
}
