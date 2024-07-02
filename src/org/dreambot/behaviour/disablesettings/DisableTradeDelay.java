package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class DisableTradeDelay extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return ClientSettings.isTradeDelayEnabled();
    }

    @Override
    public int onLoop() {
        if (ClientSettings.toggleTradeDelay(false)){
            Logger.debug("Trade delay disabled");
        } else {
            Logger.debug("Failed to disable trade delay");
        }


        return Timing.loopReturn();
    }
}
