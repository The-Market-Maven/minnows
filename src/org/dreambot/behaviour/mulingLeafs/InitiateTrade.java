package org.dreambot.behaviour.mulingLeafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.trade.Trade;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;

import static org.dreambot.api.utilities.Logger.log;

public class InitiateTrade extends Leaf<MinnowsAIO> {

	private final MuleRequestsManager muleManager;

	public InitiateTrade(MuleRequestsManager muleManager) {
		this.muleManager = muleManager;
	}



	@Override
	public boolean isValid() {

		ServerResponse request = muleManager.getServerResponse();
		if (request == null || request.getPlayerName()== null) {
			return false;
		}

		Player targetPlayer = Players.closest(request.getPlayerName());
		return targetPlayer != null
			&& targetPlayer.canReach()
			&& !Trade.isOpen();
	}

	@Override
	public int onLoop() {
		ServerResponse request = muleManager.getServerResponse();
		if (request != null && !Trade.isOpen() && Trade.tradeWithPlayer(request.getPlayerName())) {
			log("Attempting to trade with: " + request.getPlayerName());
			if (Sleep.sleepUntil(Trade::isOpen, 10000)) {
				Sleep.sleepTicks(2);
			}
		}
		return Timing.loopReturn();
	}
}
