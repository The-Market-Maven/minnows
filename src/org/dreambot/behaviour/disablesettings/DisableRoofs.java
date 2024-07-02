package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class DisableRoofs extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return !ClientSettings.areRoofsHidden();
    }

    @Override
    public int onLoop() {
        if (ClientSettings.toggleRoofs(false)){
            Logger.debug("Roofs disabled");
        } else {
            Logger.debug("Failed to disable roofs");
        }
        return Timing.loopReturn();
    }
}
