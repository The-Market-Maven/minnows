package org.dreambot.behaviour.fishingtrawlerleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.framework.Leaf;
import org.dreambot.utilities.Loadouts;
import utilities.helpers.Timing;
import utilities.loadouts.Loadout;

public class PrepareForTrawler extends Leaf<MinnowsAIO> {

    @Override
    public boolean isValid() {
        Loadout loadout = Loadouts.createFishingTrawlerLoadout();
        return !loadout.sufficientlyFulfilled();
    }

    @Override
    public int onLoop() {
        Loadout loadout = Loadouts.createFishingTrawlerLoadout();
            if (!loadout.fulfilled()) {
                loadout.fulfill();
                return Timing.loopReturn();
            }
            if (!loadout.fulfilledWithGE()) {
                loadout.fulfillWithGE();
                return Timing.loopReturn();
            }

        return Timing.loopReturn();
    }
}
