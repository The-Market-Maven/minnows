package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class AutoEquipAmmo extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return !ClientSettings.isAmmoAutoEquipping();
    }

    @Override
    public int onLoop() {
        if (ClientSettings.toggleAmmoAutoEquipping(true)){
            Logger.debug("Ammo auto equip enabled");
        } else {
            Logger.debug("Failed to enable ammo auto equip");
        }
        return Timing.loopReturn();
    }
}
