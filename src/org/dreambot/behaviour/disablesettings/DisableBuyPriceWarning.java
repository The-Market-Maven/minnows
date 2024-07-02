package org.dreambot.behaviour.disablesettings;


import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class DisableBuyPriceWarning extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return ClientSettings.isBuyPriceWarningEnabled();
    }

    @Override
    public int onLoop() {
        if(ClientSettings.toggleBuyPriceWarning(false)){
            Logger.debug("Buy price warning disabled");
        } else {
            Logger.debug("Failed to disable buy price warning");
        }

        return Timing.loopReturn();
    }
}
