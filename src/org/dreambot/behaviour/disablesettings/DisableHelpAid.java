package org.dreambot.behaviour.disablesettings;


import org.dreambot.MinnowsAIO;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

public class DisableHelpAid extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return ClientSettings.isAcceptAidEnabled();
    }

    @Override
    public int onLoop() {
       if(ClientSettings.toggleAcceptAid(false)){
           Logger.debug("Accept aid disabled");
       } else {
              Logger.debug("Failed to disable accept aid");
       }
        return Timing.loopReturn();
    }
}
