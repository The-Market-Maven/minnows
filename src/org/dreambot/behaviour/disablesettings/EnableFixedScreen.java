package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class EnableFixedScreen extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return ClientSettings.isResizableActive();
    }

    @Override
    public int onLoop() {
        if(ClientSettings.toggleResizable(false)){
            Logger.debug("Fixed screen enabled");
        } else {
            Logger.debug("Failed to enable fixed screen");
        }
        return Timing.loopReturn();
    }
}
