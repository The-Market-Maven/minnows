package org.dreambot.utilities;

import org.dreambot.api.wrappers.items.Item;
import utilities.loadouts.EquipmentLoadout;
import utilities.loadouts.InventoryLoadout;
import utilities.loadouts.Loadout;
import utilities.loadouts.LoadoutItem;

import java.util.ArrayList;
import java.util.List;

import static Constants.ItemID.*;

public class Loadouts {
    public static Loadout createShrimpFishingLoadout() {
        EquipmentLoadout equipmentLoadout = null; // No equipment needed for shrimp fishing

        InventoryLoadout inventoryLoadout = new InventoryLoadout(
                new LoadoutItem(SMALL_FISHING_NET, 1));

        return new Loadout(equipmentLoadout, inventoryLoadout);
    }
    public static Loadout createTroutFishingLoadout() {
        InventoryLoadout inventoryLoadout = new InventoryLoadout(
                new LoadoutItem(FLY_FISHING_ROD, 1),
                new LoadoutItem(FEATHER, 30000));

        return new Loadout(null, inventoryLoadout);
    }
    public static Loadout createMinnowFishingLoadout() {
        EquipmentLoadout equipmentLoadout = new EquipmentLoadout(
                new LoadoutItem(ANGLER_HAT, 1),
                new LoadoutItem(ANGLER_TOP, 1),
                new LoadoutItem(ANGLER_WADERS, 1),
                new LoadoutItem(ANGLER_BOOTS, 1));

        InventoryLoadout inventoryLoadout = new InventoryLoadout(
                new LoadoutItem(SMALL_FISHING_NET, 1),
                new LoadoutItem(MINNOW,2147483647));

        return new Loadout(equipmentLoadout, inventoryLoadout);
    }
    public static Loadout createFishingTrawlerLoadout() {
        EquipmentLoadout equipmentLoadout = new EquipmentLoadout(
                new LoadoutItem(ANGLER_HAT, 1),
                new LoadoutItem(ANGLER_TOP, 1),
                new LoadoutItem(ANGLER_WADERS, 1),
                new LoadoutItem(ANGLER_BOOTS, 1));

        InventoryLoadout inventoryLoadout = new InventoryLoadout(
                new LoadoutItem(SWAMP_PASTE,2000));

        return new Loadout(equipmentLoadout, inventoryLoadout);
    }
    public static Loadout createFishingContestLoadout(){
        EquipmentLoadout equipmentLoadout = new EquipmentLoadout(
                new LoadoutItem(AMULET_OF_GLORY4, 1),
                new LoadoutItem(RING_OF_WEALTH_5, 1),
                new LoadoutItem(COMBAT_BRACELET6,1));

        InventoryLoadout inventoryLoadout = new InventoryLoadout(
                new LoadoutItem(CAMELOT_TELEPORT, 3),
                new LoadoutItem(STAMINA_POTION4, 3),
                new LoadoutItem(COINS, 1000),
                new LoadoutItem(SPADE, 1),
                new LoadoutItem(GARLIC, 1),
                new LoadoutItem(FISHING_ROD, 1),
                new LoadoutItem(FISHING_PASS,1),
                new LoadoutItem(RED_VINE_WORM,3),
                new LoadoutItem(RAW_GIANT_CARP,1));

        return new Loadout(equipmentLoadout, inventoryLoadout);
    }




}
