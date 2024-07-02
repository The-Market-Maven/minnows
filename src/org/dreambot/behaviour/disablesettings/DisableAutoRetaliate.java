package org.dreambot.behaviour.disablesettings;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

import static org.dreambot.api.utilities.Logger.debug;

public class DisableAutoRetaliate extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return Combat.isAutoRetaliateOn();
    }

    @Override
    public int onLoop() {
        if (Combat.toggleAutoRetaliate(false)){
            debug("Auto retaliate disabled");
        } else {
           debug("Failed to disable auto retaliate");
        }
        return Timing.loopReturn();
    }
}
