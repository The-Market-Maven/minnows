package org.dreambot.utilities;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.framework.Leaf;
import utilities.helpers.Timing;

import static org.dreambot.api.utilities.Logger.log;

public class BankOnceLeaf extends Leaf {

    private static boolean checkedBank = false;
    private final MinnowsAIO main;

    public BankOnceLeaf(MinnowsAIO main) {
        this.main = main;
    }

    @Override
    public boolean isValid() {
        return !checkedBank;
    }

    @Override
    public int onLoop() {
        if (!Bank.isOpen() && Walking.shouldWalk(6) && Bank.open(Bank.getClosestBankLocation())) {
            Logger.log("Opening Bank Once To cache Owned Items.");
            return Timing.loopReturn();
        }
        if (Bank.isOpen()) {
            Sleep.sleepTick();
            Bank.depositAllItems();
            checkedBank = true;
           main.setInitialSetupCompleted(true);
        }
        if (Bank.isOpen() && Bank.close()) {
            log("Closing Bank, Just Cached Owned Items.");
        }

        return Timing.loopReturn();
    }
}