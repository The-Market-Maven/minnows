package org.dreambot.behaviour.quests.fishingcontest;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.Client;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.quest.book.PaidQuest;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.framework.Branch;
import org.dreambot.framework.Leaf;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;
import utilities.requirements.CheckRequirements;

public class FishingContestBranch extends Branch<MinnowsAIO> {
    public static final int FISHING_CONTEST_CONFIG_ID = 11;
    public static final Tile FISHING_CONTEST_START_TILE = new Tile(2821, 3486, 0);

    public static int fishingContestState = PlayerSettings.getConfig(FISHING_CONTEST_CONFIG_ID);

    private final MuleRequestsManager muleManager;

    public FishingContestBranch(MuleRequestsManager muleManager) {
        this.muleManager = muleManager;
    }

    @Override
    public boolean isValid() {
        ServerResponse request = muleManager.getServerResponse();
        return Client.isMembers()
                && CheckRequirements.checkRequirements(PaidQuest.FISHING_CONTEST)
                && children.stream().anyMatch(Leaf::isValid)
                && request == null;
    }
}

