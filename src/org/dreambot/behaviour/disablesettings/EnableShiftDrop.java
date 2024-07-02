package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class EnableShiftDrop extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return !ClientSettings.isShiftClickDroppingEnabled();
    }

    @Override
    public int onLoop() {
        if (ClientSettings.	toggleShiftClickDropping(true)){
            Logger.debug("Shift click dropping enabled");
        } else {
            Logger.debug("Failed to enable shift click dropping");
        }
        return Timing.loopReturn();
    }
}
