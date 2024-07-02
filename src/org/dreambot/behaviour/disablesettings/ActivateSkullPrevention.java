package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class ActivateSkullPrevention extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return !ClientSettings.isSkullPreventionActive();
    }

    @Override
    public int onLoop() {
        if(ClientSettings.toggleSkullPrevention(true)){
            Logger.debug("Skull prevention enabled");
        } else {
            Logger.debug("Failed to enable skull prevention");
        }

        return Timing.loopReturn();
    }
}
