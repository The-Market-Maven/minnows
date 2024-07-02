package org.dreambot.behaviour.disablesettings;


import org.dreambot.MinnowsAIO;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

import static org.dreambot.api.methods.magic.Magic.isSpellFilteringEnabled;
import static org.dreambot.api.methods.magic.Magic.setSpellFilteringEnabled;

public class DisableSpellFiltering extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return isSpellFilteringEnabled();
    }

    @Override
    public int onLoop() {
        if (setSpellFilteringEnabled(false)){
            Logger.debug("Spell filtering disabled");
        } else {
            Logger.debug("Failed to disable spell filtering");
        }
        return Timing.loopReturn();
    }
}