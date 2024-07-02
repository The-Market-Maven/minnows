package org.dreambot.behaviour.fishingtrawlerleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.Client;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

import static org.dreambot.utilities.API.TRAWLER_AREA;
import static org.dreambot.utilities.API.TRAWLER_LOBBY;
import static utilities.helpers.WalkingHelper.walkTo;

public class TravelToTrawler extends Leaf<MinnowsAIO>{


    @Override
    public boolean isValid() {
        return !Client.isDynamicRegion() && !TRAWLER_LOBBY.contains(Players.getLocal()) && !TRAWLER_AREA.contains(Players.getLocal());
    }

    @Override
    public int onLoop() {
        walkTo(TRAWLER_AREA.getRandomTile(),0);
        return Timing.loopReturn();
    }
}
