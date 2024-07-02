package org.dreambot.behaviour;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.Client;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.framework.Branch;
import org.dreambot.framework.Leaf;
import org.dreambot.utilities.API;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;

public class FishingTrawlerBranch extends Branch<MinnowsAIO> {
    private final MuleRequestsManager muleManager;

    public FishingTrawlerBranch(MuleRequestsManager muleManager) {
        this.muleManager = muleManager;
    }
    @Override
    public boolean isValid() {
        ServerResponse request = muleManager.getServerResponse();

        return request == null && Client.isMembers()
                && !API.hasFullAngler()
                && Skills.getRealLevel(Skill.FISHING) >= 15;
    }
}
