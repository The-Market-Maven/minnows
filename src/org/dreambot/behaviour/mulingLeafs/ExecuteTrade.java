package org.dreambot.behaviour.mulingLeafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.trade.Trade;
import org.dreambot.api.methods.trade.TradeUser;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;

import static Constants.ItemID.COINS;


public class ExecuteTrade extends Leaf<MinnowsAIO> {
	private final MuleRequestsManager muleManager;

	public ExecuteTrade(MuleRequestsManager muleManager) {
		this.muleManager = muleManager;
	}

	

	@Override
	public boolean isValid() {
		ServerResponse serverResponse = muleManager.getServerResponse();
		return serverResponse!=null && Trade.isOpen() && Trade.getTradingWith().equals(serverResponse.getPlayerName());
	}

	@Override
	public int onLoop() {

		ServerResponse serverResponse = muleManager.getServerResponse();
		if (serverResponse==null || !Trade.isOpen() || !Trade.getTradingWith().equals(serverResponse.getPlayerName())) {

			return Timing.loopReturn();
		}

		if ("give".equalsIgnoreCase(serverResponse.getAction())) {
			handleTrade(true);
		} else if ("receive".equalsIgnoreCase(serverResponse.getAction())) {
			handleTrade(false);
		}
		return Timing.loopReturn();
	}

	private void handleTrade(boolean sending) {
		if (Trade.isOpen(1)) {
			if (sending) {
				int amount = muleManager.getServerResponse().getAmount();
				if (!Trade.contains(true, COINS)) {
					if (Trade.addItem(COINS, amount)) {
						Sleep.sleepTicks(3);
					}
				}  else {
					if (Trade.acceptTrade(1)) {
						Sleep.sleepUntil(() -> !Trade.isOpen(1), 10000, 50);
					}
				}
			}
			if (!sending) {
				if (Trade.contains(false, COINS)) {
					Trade.acceptTrade(1);
				}
			}
		} else if (Trade.isOpen(2)) {
			if (!Trade.hasAcceptedTrade(TradeUser.US) && Trade.acceptTrade(2)) {
				Sleep.sleepUntil(() -> !Trade.isOpen(), 5000, 50);
			}
		}
	}
}

