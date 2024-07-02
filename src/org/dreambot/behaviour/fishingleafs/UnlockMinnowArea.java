package org.dreambot.behaviour.fishingleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.quest.book.PaidQuest;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.framework.Leaf;
import utilities.helpers.InteractionsHelper;
import utilities.helpers.Timing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.dreambot.utilities.API.hasFullAngler;
import static org.dreambot.utilities.API.hasUnlockedMinnows;
import static utilities.helpers.WalkingHelper.walkTo;

public class UnlockMinnowArea extends Leaf<MinnowsAIO>{

    @Override
    public boolean isValid() {
        return hasFullAngler() && PaidQuest.FISHING_CONTEST.isFinished() && Skills.getRealLevel(Skill.FISHING) >= 82 && !hasUnlockedMinnows();
    }

    @Override
    public int onLoop() {
        Tile t = new Tile(2599,3424,0);
        if(t.distance() >= 2){
            walkTo(t, 0);
            return Timing.loopReturn();
        }
        Queue<Integer> options = new LinkedList<>(Arrays.asList(1, -1));
        InteractionsHelper.talkTo("Kylie Minnow", options);
        return Timing.loopReturn();
    }
}
