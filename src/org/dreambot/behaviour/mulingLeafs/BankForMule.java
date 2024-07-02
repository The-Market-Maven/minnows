package org.dreambot.behaviour.mulingLeafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.trade.Trade;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;

import static Constants.ItemID.COINS;
import static org.dreambot.api.utilities.Logger.debug;

public class BankForMule extends Leaf<MinnowsAIO> {
	private final MuleRequestsManager muleManager;

	public BankForMule(MuleRequestsManager muleManager) {
		this.muleManager= muleManager;
	}

	@Override
	public boolean isValid() {
		ServerResponse request = muleManager.getServerResponse();
		if ((request==null || request.getPlayerName() ==null) && !Trade.isOpen()) {
			return false;
		}
		return Inventory.isFull() || Bank.contains(COINS) || Bank.isOpen();
	}

	@Override
	public int onLoop() {
		if (!Bank.isOpen() && Bank.open(Bank.getClosestBankLocation())){
			debug("Opening Bank For Mule Event.");
			return Timing.loopReturn();

		}
		if(Bank.isOpen() && !Inventory.onlyContains(COINS) && !Inventory.isEmpty()){
			if(Bank.depositAllExcept(COINS)){
				debug("Depositing all except coins");
				return Timing.loopReturn();
			}

		}
		if (Bank.contains(COINS) && Bank.withdrawAll(COINS)){
			debug("Withdrawing all coins");
			return Timing.loopReturn();
		}
		if (Bank.isOpen() && !Bank.contains(COINS) && Bank.close()) {
		}

		return Timing.loopReturn();
	}
}
