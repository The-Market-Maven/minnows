package org.dreambot.behaviour.fishingleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.framework.Leaf;

import org.dreambot.utilities.API;
import org.dreambot.utilities.Loadouts;
import utilities.helpers.Timing;
import utilities.loadouts.Loadout;

public class SetupEquipmentLeaf extends Leaf<MinnowsAIO> {

    @Override
    public boolean isValid() {
        Loadout loadout = determineLoadout();
        if (loadout != null){
            return !loadout.sufficientlyFulfilled();
        }
        return false;
    }

    @Override
    public int onLoop() {
        Loadout loadout = determineLoadout();
        if (loadout != null) {
            if (!loadout.fulfilled()) {
                loadout.fulfill();
            }
            if (!loadout.fulfilledWithGE()) {
                loadout.fulfillWithGE();
                return Timing.loopReturn();
            }

        }
        return Timing.loopReturn();

    }

    private Loadout determineLoadout() {
        int fishingLevel = Skills.getRealLevel(Skill.FISHING);
        if (fishingLevel >= 82) {
            if (!API.hasFullAngler()){
                return Loadouts.createFishingTrawlerLoadout();
            } else {
                return Loadouts.createMinnowFishingLoadout();
            }
        } else if (fishingLevel >= 20) {
            return Loadouts.createTroutFishingLoadout();
        } else if (fishingLevel >= 1) {
            return Loadouts.createShrimpFishingLoadout();
        }
        return null; // No valid fishing level found
    }
}
