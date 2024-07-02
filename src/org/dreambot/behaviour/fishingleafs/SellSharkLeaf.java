package org.dreambot.behaviour.fishingleafs;

import Constants.ItemID;
import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.framework.Leaf;
import utilities.helpers.GEHelper;
import utilities.helpers.OwnedItems;
import utilities.helpers.Timing;

import java.util.ArrayList;
import java.util.List;

import static utilities.helpers.WalkingHelper.walkTo;

public class SellSharkLeaf extends Leaf<MinnowsAIO>{
    @Override
    public boolean isValid() {
        return OwnedItems.contains("Raw shark");
    }

    @Override
    public int onLoop() {
        if (BankLocation.GRAND_EXCHANGE.getTile().distance() >= 8){
            walkTo(BankLocation.GRAND_EXCHANGE.getTile(), 0);
            return Timing.loopReturn();
        }
        List<Item> items = new ArrayList<>();
        items.add(new Item(ItemID.RAW_SHARK, OwnedItems.count("Raw shark")));
        GEHelper.sellBankItemsToGE(items);
        return 0;
    }
}
