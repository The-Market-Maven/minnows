package org.dreambot.behaviour.quests.fishingcontest;

import Constants.NpcID;
import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.framework.Leaf;
import utilities.helpers.InteractionsHelper;
import utilities.helpers.Timing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static Constants.ItemID.RAW_GIANT_CARP;
import static Constants.ObjectID.GATE_48;
import static org.dreambot.api.utilities.Logger.debug;
import static org.dreambot.behaviour.quests.fishingcontest.FishingContestBranch.FISHING_CONTEST_CONFIG_ID;
import static org.dreambot.behaviour.quests.fishingcontest.FishingContestBranch.fishingContestState;
import static utilities.helpers.WalkingHelper.walkTo;

public class FishingContestStepTwo extends Leaf<MinnowsAIO> {
    private boolean hasTurnedInFish = false;
    private boolean waitingForContestResult = false;
    public boolean isValid() {
        //step 2 maps to value 3
        fishingContestState = PlayerSettings.getConfig(FISHING_CONTEST_CONFIG_ID);
        return fishingContestState == 3;
    }

    @Override
    public int onLoop() {
        winContest();
        return Timing.loopReturn();
    }


    private void winContest() {
        NPC fishingSpot = NPCs.closest(4080);
     Area AREA = new Area(
                new Tile(2642, 3444, 0),
                new Tile(2642, 3434, 0),
                new Tile(2637, 3434, 0),
                new Tile(2637, 3444, 0));
        if (!AREA.contains(Players.getLocal())) {
            walkTo(AREA.getCenter(),0);
            Queue<Integer> options = new LinkedList<>(Arrays.asList(-1));
            InteractionsHelper.talkTo("Morris",options);
            return;
        }

        if (!hasTurnedInFish) {
            if (!Inventory.contains(RAW_GIANT_CARP)) {
                if (fishingSpot.interact()) {
                    Sleep.sleepUntil(() -> Inventory.contains(RAW_GIANT_CARP), 3000);
                }
            } else {
                Queue<Integer> options = new LinkedList<>(Arrays.asList(1,-1));
                if (InteractionsHelper.talkTo("Bonzo", options)) {
                    Sleep.sleepUntil(this::hasWonFishingContest, 60_000);
                    Logger.log("Won fishing contest");
                }
            }
        }
    }


    private boolean hasWonFishingContest() {
        String expectedDialogue = "we have a new winner!";
        String npcDialogue = Dialogues.getNPCDialogue();
        if (npcDialogue != null && npcDialogue.contains(expectedDialogue)) {
            return true;
        }

        return false;
    }

}
