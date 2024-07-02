package org.dreambot.behaviour.fishingtrawlerleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

import static org.dreambot.utilities.API.*;
import static utilities.helpers.WalkingHelper.walkTo;

public class JoinTrawlerGame extends Leaf<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return TRAWLER_AREA.contains(Players.getLocal()) && !TRAWLER_LOBBY.contains(Players.getLocal());
    }

    @Override
    public int onLoop() {
        if (Worlds.getCurrentWorld() != TRAWLER_WORLD) {
            WorldHopper.hopWorld(TRAWLER_WORLD);
            Sleep.sleepUntil(() -> Worlds.getCurrentWorld() == TRAWLER_WORLD, 10000);
            return Timing.loopReturn();
        }
        GameObject gangplank = GameObjects.getTopObjectOnTile(TRAWLER_START);
        Player myPlayer = Players.getLocal();
        if (gangplank != null && gangplank.canReach() && TRAWLER_AREA.contains(Players.getLocal())) {
            gangplank.interact("Cross");
            Sleep.sleepUntil(() -> TRAWLER_LOBBY.contains(Players.getLocal()), myPlayer::isMoving, 5000, 1000);
        } else {
            walkTo(TRAWLER_START, 0);
        }
        return Timing.loopReturn();
    }
}
