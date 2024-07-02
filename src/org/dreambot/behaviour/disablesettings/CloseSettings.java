package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class CloseSettings extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
         return ClientSettings.isOpen();

    }

    @Override
    public int onLoop() {
        if (ClientSettings.closeSettingsInterface()){
            Logger.debug("Settings interface closed");
        } else {
            Logger.debug("Failed to close settings interface");
        }
        return Timing.loopReturn();
    }
}
