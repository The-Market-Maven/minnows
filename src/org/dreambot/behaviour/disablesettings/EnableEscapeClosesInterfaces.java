package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

public class EnableEscapeClosesInterfaces extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return !ClientSettings.isEscInterfaceClosingEnabled();
    }

    @Override
    public int onLoop() {
        if(ClientSettings.toggleEscInterfaceClosing(true)){
            Logger.debug("Enabled Escape Closes Interfaces");
        } else {
            Logger.debug("Failed to Enable Escape Closes Interfaces");
        }

        return Timing.loopReturn();
    }
}
