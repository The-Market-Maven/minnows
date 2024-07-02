package org.dreambot.behaviour;

import org.dreambot.MinnowsAIO;
import org.dreambot.framework.Leaf;
import org.dreambot.framework.Root;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;

public class FishingBranch extends Root<MinnowsAIO> {


    private final MuleRequestsManager muleManager;

    public FishingBranch(MuleRequestsManager muleManager) {
        this.muleManager = muleManager;
    }
    @Override
    public boolean isValid() {
            ServerResponse request = muleManager.getServerResponse();

        return request == null && children.stream().anyMatch(Leaf::isValid);
    }

}
