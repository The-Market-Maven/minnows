package org.dreambot.behaviour.fishingleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.framework.Leaf;
import org.dreambot.utilities.FishingSpotEnum;
import utilities.helpers.Timing;

import static org.dreambot.MinnowsAIO.getSelectedFishingArea;
import static org.dreambot.api.methods.walking.impl.Walking.walk;
import static utilities.helpers.WalkingHelper.walkTo;

public class WalkToFishingSpot extends Leaf<MinnowsAIO> {
    private FishingSpotEnum bestSpot;

    @Override
    public boolean isValid() {
        bestSpot = FishingSpotEnum.findHighestAccessibleFishingSpot(Skills.getRealLevel(Skill.FISHING));
        Area selectedArea = getSelectedFishingArea(bestSpot);
        return selectedArea != null && !selectedArea.contains(Players.getLocal());
    }

    @Override
    public int onLoop() {
        Area selectedArea = getSelectedFishingArea(bestSpot);
        if (selectedArea != null) {
            walkTo(selectedArea.getRandomTile(),0);
        }
        return Timing.loopReturn();
    }
}
