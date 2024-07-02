package org.dreambot.behaviour.bondLeafs;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.framework.Leaf;
import utilities.helpers.OwnedItems;
import utilities.helpers.Timing;

import static Constants.ItemID.OLD_SCHOOL_BOND_UNTRADEABLE;
import static org.dreambot.api.methods.bond.Bond.*;
import static org.dreambot.api.utilities.Logger.debug;
import static utilities.helpers.WalkingHelper.walkTo;
import static utilities.helpers.WorldsHelper.wHopMember;


public class RedeemBond extends Leaf {
	private boolean hasRedeemedBond = false;
	public boolean hasRedeemedBond() {
		return hasRedeemedBond;
	}
	@Override
	public boolean isValid() {
		return OwnedItems.contains(OLD_SCHOOL_BOND_UNTRADEABLE)
			|| Bank.contains(g -> g.getName().contains("Old school bond"))
			|| Inventory.contains(g -> g.getName().contains("Old school bond"))
			|| hasRedeemedBond;
	}

	@Override
	public int onLoop() {
		if (hasRedeemedBond){
			if(wHopMember()){
				hasRedeemedBond = false;
			}
			return Timing.loopReturn();
		}
		if (Bank.contains(g -> g.getName().contains("Old school bond")) && !Inventory.contains(OLD_SCHOOL_BOND_UNTRADEABLE)) {
			if (!Bank.isOpen()) {
				if (Bank.open()) {
					Sleep.sleepUntil(Bank::isOpen, 3000);
				} else {
					walkTo(BankLocation.getNearest().getTile(), 0);
				}
			}
			if (!Bank.isOpen()) {
				return Timing.loopReturn();
			}
			if (Bank.withdraw(OLD_SCHOOL_BOND_UNTRADEABLE, 1)) {
				Sleep.sleepUntil(() -> Inventory.contains(OLD_SCHOOL_BOND_UNTRADEABLE), 3000);
			}
		}
		if (Inventory.contains(OLD_SCHOOL_BOND_UNTRADEABLE)) {
			if(Bank.isOpen()){
				Bank.close();
				return Timing.loopReturn();
			}
			if (!isRedeemBondScreenOpen() && openRedeemBondScreen()) {
				debug("Opening Bond Interface");
				return Timing.loopReturn();
			}
			if (isRedeemBondScreenOpen() && redeem(1)) {
				debug("Redeemed Bond For Membership.");
				hasRedeemedBond = true;
				Sleep.sleep(10_000);

			}

		}
		return Timing.loopReturn();
	}
}

