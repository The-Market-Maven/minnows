package org.dreambot.behaviour.mulingLeafs;


import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.trade.Trade;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;

import static utilities.helpers.WalkingHelper.walkTo;

public class WalkToMulingLoc extends Leaf<MinnowsAIO> {

	private final MuleRequestsManager muleManager;

	public  WalkToMulingLoc(MuleRequestsManager muleManager) {
		this.muleManager = muleManager;
	}



	@Override
	public boolean isValid() {

		ServerResponse request = muleManager.getServerResponse();
		if (request == null || request.getPlayerName()== null) {
			return false;
		}

		return request.getLocation().distance() >= 12 && !Trade.isOpen();
	}

	@Override
	public int onLoop() {
		ServerResponse request = muleManager.getServerResponse();

		if (request != null && !Trade.isOpen()) {
		walkTo(request.getLocation(),0);
		}
		return Timing.loopReturn();
	}
}
