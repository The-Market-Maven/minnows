package org.dreambot.webnodes;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.walking.pathfinding.impl.web.WebFinder;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

public class RemoveFishingContestNodes extends Leaf<MinnowsAIO> {
    private boolean removed = false;

    @Override
    public boolean isValid() {
        return !removed;
    }

    @Override
    public int onLoop() {
        WebFinder.getWebFinder().removeNode(3314);
        WebFinder.getWebFinder().removeNode(3313);

        removed = true;
        return Timing.loopReturn();
    }
}
