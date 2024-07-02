package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class DisableWorldHopConfirmation extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return ClientSettings.isWorldHopConfirmationEnabled();
    }

    @Override
    public int onLoop() {
        if (ClientSettings.toggleWorldHopConfirmation(false)){
            Logger.debug("World hop confirmation disabled");
        } else {
            Logger.debug("Failed to disable world hop confirmation");
        }
        return Timing.loopReturn();
    }
}
