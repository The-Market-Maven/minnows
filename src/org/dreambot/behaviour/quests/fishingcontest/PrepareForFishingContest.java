package org.dreambot.behaviour.quests.fishingcontest;


import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.quest.book.PaidQuest;
import org.dreambot.framework.Leaf;
import org.dreambot.utilities.Loadouts;
import utilities.helpers.Timing;
import utilities.loadouts.Loadout;
import utilities.loadouts.LoadoutUtils;
import utilities.requirements.CheckRequirements;

public class PrepareForFishingContest extends Leaf<MinnowsAIO> {
    public static boolean completedLoadout = false;

    @Override
    public boolean isValid() {
        return!completedLoadout;
    }
    public int onLoop() {
        Loadout loadout = Loadouts.createFishingContestLoadout();
        completedLoadout =  LoadoutUtils.prepareLoadout(loadout, completedLoadout);
        return Timing.loopReturn();
    }
}
