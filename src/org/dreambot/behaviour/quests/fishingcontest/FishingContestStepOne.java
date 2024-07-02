package org.dreambot.behaviour.quests.fishingcontest;

import Constants.NpcID;
import Constants.ObjectID;
import org.dreambot.MinnowsAIO;
import org.dreambot.api.Client;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
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
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.framework.Leaf;
import utilities.helpers.InteractionsHelper;
import utilities.helpers.Timing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static Constants.ItemID.GARLIC;
import static Constants.ItemID.RED_VINE_WORM;
import static Constants.ObjectID.GATE_48;
import static org.dreambot.api.utilities.Logger.debug;
import static org.dreambot.api.utilities.Logger.log;
import static org.dreambot.behaviour.quests.fishingcontest.FishingContestBranch.FISHING_CONTEST_CONFIG_ID;
import static org.dreambot.behaviour.quests.fishingcontest.FishingContestBranch.fishingContestState;
import static utilities.helpers.WalkingHelper.walkTo;

public class FishingContestStepOne extends Leaf<MinnowsAIO>{

    public static final Tile RED_WORM_TILE = new Tile(2634, 3491, 0);
    private boolean enteredContestArea = false;
    @Override
    public boolean isValid() {
        fishingContestState = PlayerSettings.getConfig(FISHING_CONTEST_CONFIG_ID);
        return fishingContestState == 1 || fishingContestState == 2;
    }

    @Override
    public int onLoop() {
        if (Inventory.count(RED_VINE_WORM) < 3) {
            getRedVineWorms();
            return Timing.loopReturn();
        }
       Area AREA = new Area(2624, 3506, 2649, 3470, 0);
        if (AREA.contains(Players.getLocal())){
            walkTo(BankLocation.getNearest().getTile(), 0);
            return Timing.loopReturn();
        }
        Logger.log("Entered Contest Area : " + enteredContestArea);
        if (!enteredContestArea){
            if (Players.getLocal().distance(new Tile(2642, 3441, 0)) > 5) {
                walkTo(new Tile(2642, 3441, 0), 0);
                return Timing.loopReturn();
            }

            GameObject gate = GameObjects.closest(GATE_48);
            if (gate != null && gate.interact("Open")) {
                debug("Opening Gate...");
                Queue<Integer> options = new LinkedList<>(Arrays.asList(-1));
                InteractionsHelper.talkTo("Morris",options);
                enteredContestArea = true;
                return Timing.loopReturn();
            }
        }

        if (enteredContestArea && PlayerSettings.getBitValue(2054) == 0){
            Tile t = new Tile(2638, 3445, 0);
            if (Players.getLocal().distance(t) > 2) {
                walkTo(t, 0);
                return Timing.loopReturn();
            }
            GameObject wallPipe = GameObjects.closest(ObjectID.WALL_PIPE);
            Item garlic = Inventory.get(GARLIC);
            if (wallPipe != null && garlic != null){
                if(garlic.useOn(wallPipe)){
                    Logger.debug("Using Garlic on Wall Pipe...");
                    if(Sleep.sleepUntil(Dialogues::inDialogue,() -> Players.getLocal().isMoving(), 2500, 50)){
                        if(Dialogues.canContinue()){
                            Dialogues.spaceToContinue();
                            return Timing.loopReturn();
                        }
                    }
                }
            }
        }
        if (enteredContestArea && PlayerSettings.getBitValue(2054) == 1) {
            Queue<Integer> options = new LinkedList<>(Arrays.asList(1, -1));
            if (InteractionsHelper.talkTo("Bonzo", options)) {
                Logger.log("Conversation With Bonzo.");
            }
        }
        return Timing.loopReturn();
    }
    private void getRedVineWorms() {
        if (Players.getLocal().distance(RED_WORM_TILE) >= 8) {
            debug("Walking To Red Vine Worms..");
            walkTo(RED_WORM_TILE, 0);
            return;
        }
        GameObject Vine = GameObjects.closest("Vine");
        if (Vine!=null && Vine.canReach() && Vine.hasAction("Check") && !Players.getLocal().isAnimating() && Vine.interact("Check")) {
            debug("Digging Up Worms...");
        }
    }

}
