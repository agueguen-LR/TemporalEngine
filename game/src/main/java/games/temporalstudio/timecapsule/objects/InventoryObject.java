package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.Triggerable;

public interface InventoryObject extends TimeObject{
	public abstract Triggerable getTriggerable();
}
