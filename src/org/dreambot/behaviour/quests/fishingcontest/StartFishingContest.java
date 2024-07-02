package org.dreambot.behaviour.quests.fishingcontest;


import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.quest.book.PaidQuest;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;
import utilities.helpers.InteractionsHelper;
import utilities.helpers.Timing;
import utilities.requirements.CheckRequirements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.dreambot.behaviour.quests.fishingcontest.FishingContestBranch.*;
import static utilities.helpers.WalkingHelper.walkTo;

public class StartFishingContest extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        fishingContestState = PlayerSettings.getConfig(FISHING_CONTEST_CONFIG_ID);
        return fishingContestState == 0 || fishingContestState == 4;
    }

    @Override
    public int onLoop() {
        if (FISHING_CONTEST_START_TILE.distance() >= 6) {
            walkTo(FISHING_CONTEST_START_TILE, 3);
            return Timing.loopReturn();
        }
        Queue<Integer> options = new LinkedList<>(Arrays.asList(1, 2, 3, 1, 1));
        InteractionsHelper.talkTo("Vestri", options);
        return Timing.loopReturn();
    }
}
