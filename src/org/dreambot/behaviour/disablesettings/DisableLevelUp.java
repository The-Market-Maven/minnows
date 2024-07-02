package org.dreambot.behaviour.disablesettings;


import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;


public class DisableLevelUp extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return ClientSettings.isLevelUpInterfaceEnabled();
    }

    @Override
    public int onLoop() {
        if (ClientSettings.toggleLevelUpInterface(false)) {
            Logger.debug("Level up interface disabled");
        } else {
            Logger.debug("Failed to disable level up interface");
        }
        return Timing.loopReturn();
    }
}
