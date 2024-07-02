package org.dreambot.webnodes;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.quest.book.PaidQuest;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.pathfinding.impl.web.WebFinder;
import org.dreambot.api.methods.walking.web.node.CustomWebPath;
import org.dreambot.api.methods.walking.web.node.impl.BasicWebNode;
import org.dreambot.api.methods.walking.web.node.impl.EntranceWebNode;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

public class MinnowNodes extends Leaf<MinnowsAIO> {
    private boolean removed = false;

    @Override
    public boolean isValid() {
        return  PaidQuest.FISHING_CONTEST.isFinished() && !removed;
    }

    @Override
    public int onLoop() {
        WebFinder.getWebFinder().removeNode(7406);
        WebFinder.getWebFinder().removeNode(7405);
        EntranceWebNode minnowEntrance = new EntranceWebNode(2601,3425,0);
        minnowEntrance.setAction("Travel to platform");
        minnowEntrance.setEntityName("Row boat");
        minnowEntrance.setCondition(() -> PlayerSettings.getBitValue(5669) == 2);
        EntranceWebNode minnowExit = new EntranceWebNode(2614,3440,0);
        BasicWebNode minnowPlatform = new BasicWebNode(2617,3445,0);
        minnowExit.setAction("Leave platform");
        minnowExit.setEntityName("Row boat");
        minnowEntrance.addDualConnections(minnowExit,WebFinder.getWebFinder().get(3306));
        minnowExit.addDualConnections(minnowPlatform);
        WebFinder.getWebFinder().addWebNodes(minnowEntrance,minnowExit,minnowPlatform);

        removed = true;
        return Timing.loopReturn();
    }
}


