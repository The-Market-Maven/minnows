package org.dreambot.behaviour.fishingleafs;

import Constants.ItemID;
import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.framework.Leaf;
import org.dreambot.utilities.FishingSpotEnum;
import utilities.helpers.Timing;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static utilities.helpers.WorldsHelper.wHopMember;

public class FishingLeaf extends Leaf<MinnowsAIO> {

    private static final int MINNOW_ID = ItemID.MINNOW;

    private int lastMinnowCount = 0;
    private NPC lastFishingSpot = null;

    @Override
    public boolean isValid() {
        FishingSpotEnum bestSpot = FishingSpotEnum.findHighestAccessibleFishingSpot(Skills.getRealLevel(Skill.FISHING));
        boolean hasEquipment = hasRequiredEquipment(bestSpot);
        boolean atSpot = bestSpot.getFishingAreas().stream().anyMatch(area -> area.contains(Players.getLocal()));

        return hasEquipment && atSpot;
    }

    private boolean hasRequiredEquipment(FishingSpotEnum spot) {
        return spot.getFishingType().getRequiredItems().stream().allMatch(Inventory::contains);
    }

    @Override
    public int onLoop() {
        FishingSpotEnum bestSpot = FishingSpotEnum.findHighestAccessibleFishingSpot(Skills.getRealLevel(Skill.FISHING));
        if (Inventory.isFull()) {
            dropFish();
        }

        if (bestSpot == FishingSpotEnum.MINNOWS_SPOT) {
            if (Worlds.getCurrentWorld() == 370){
                wHopMember();
                return Timing.loopReturn();
            }
            handleMinnowFishing();
        } else {
            standardFishing(bestSpot);
        }
        return Timing.loopReturn();
    }

    private void standardFishing(FishingSpotEnum spot) {
        NPC fishingSpot = NPCs.closest(npc -> spot.getFishingSpotIDs().contains(npc.getID()));
        if (fishingSpot != null && fishingSpot.interact()) {
            Sleep.sleepUntil(Inventory::isFull,() ->Players.getLocal().isAnimating(), 2500, 50);
        }
    }

    private void handleMinnowFishing() {
        int currentMinnowCount = Inventory.count(MINNOW_ID);
        if (currentMinnowCount < lastMinnowCount || lastFishingSpot == null || !lastFishingSpot.exists()) {
            lastFishingSpot = selectNewFishingSpot();
        }
        if (lastFishingSpot != null && !Players.getLocal().isInteracting(lastFishingSpot) && lastFishingSpot.interact()) {
            Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 2500);
        }
        lastMinnowCount = currentMinnowCount;
    }

    private NPC selectNewFishingSpot() {
        List<Integer> fishingSpotIdList = FishingSpotEnum.MINNOWS_SPOT.getFishingSpotIDs();
        return NPCs.closest(npc -> fishingSpotIdList.contains(npc.getID()) && (!npc.equals(lastFishingSpot)));
    }


    private void dropFish() {
        Set<Integer> allFishTypeIDs = new HashSet<>();
        for (FishingSpotEnum spot : FishingSpotEnum.values()) {
            allFishTypeIDs.addAll(spot.getFishTypeIDs());
        }

        // Drop all types of fish across all spots, excluding minnows
        Inventory.all(item -> item != null &&
                        allFishTypeIDs.contains(item.getID()) &&
                        item.getID() != ItemID.MINNOW) // Ensure minnows are not dropped
                .forEach(item -> item.interact("Drop"));
    }
}
