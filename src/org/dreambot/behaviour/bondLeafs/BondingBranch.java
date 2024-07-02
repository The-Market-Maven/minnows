package org.dreambot.behaviour.bondLeafs;

import Constants.ItemID;
import org.dreambot.MinnowsAIO;
import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.framework.Branch;
import utilities.helpers.OwnedItems;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;

import static Constants.ItemID.COINS;
import static Constants.ItemID.OLD_SCHOOL_BOND;


public class BondingBranch extends Branch<MinnowsAIO> {

	private final MuleRequestsManager muleManager;
	private final RedeemBond redeemBond;

	public BondingBranch(MuleRequestsManager muleManager, RedeemBond redeemBond) {
		this.muleManager = muleManager;
		this.redeemBond = redeemBond;
	}

	@Override
	public boolean isValid() {
		ServerResponse request = muleManager.getServerResponse();
		if (request == null && Client.isLoggedIn() && Client.getGameState() == GameState.LOGGED_IN) {
			if (redeemBond.hasRedeemedBond()) {
				return true;
			}
			if (OwnedItems.contains(OLD_SCHOOL_BOND) || OwnedItems.contains(ItemID.OLD_SCHOOL_BOND_UNTRADEABLE) || OwnedItems.count(COINS) >= LivePrices.getHigh(OLD_SCHOOL_BOND)) {
				return !Client.isMembers() || muleManager.needsMembershipRenewal();
			} else {
				return false;
			}
		}
		return false;
	}
}

