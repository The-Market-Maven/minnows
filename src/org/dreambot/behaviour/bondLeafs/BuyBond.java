package org.dreambot.behaviour.bondLeafs;

import org.dreambot.api.Client;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.framework.Leaf;
import utilities.helpers.GEHelper;
import utilities.helpers.OwnedItems;
import utilities.helpers.Timing;
import utilities.mule.MuleRequestsManager;

import java.util.ArrayList;
import java.util.List;

import static Constants.ItemID.*;



public class BuyBond extends Leaf {
	private final MuleRequestsManager muleManager;

	public BuyBond(MuleRequestsManager muleManager) {
		this.muleManager = muleManager;
	}

	@Override
	public boolean isValid() {
		return 	!Bank.contains(g -> g.getName().contains("Old school bond"))
				&& !Inventory.contains(g -> g.getName().contains("Old school bond"))
				&& !OwnedItems.contains(OLD_SCHOOL_BOND_UNTRADEABLE)
				&& OwnedItems.count(COINS) >= LivePrices.getHigh(OLD_SCHOOL_BOND)
				&& (!Client.isMembers() || muleManager.needsMembershipRenewal());
	}

	@Override
	public int onLoop() {
		List<Item> itemsToBuyList = new ArrayList<>();
		itemsToBuyList.add(new Item(OLD_SCHOOL_BOND,1));
		GEHelper.buyItemListFromGE(itemsToBuyList);
		return Timing.loopReturn();
	}
}
