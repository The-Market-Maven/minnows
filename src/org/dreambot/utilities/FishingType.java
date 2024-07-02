package org.dreambot.utilities;

import Constants.ItemID;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public enum FishingType {
    SMALL_NET("Net", Arrays.asList(ItemID.SMALL_FISHING_NET)),
    FLY_FISHING("Lure", Arrays.asList(ItemID.FLY_FISHING_ROD, ItemID.FEATHER)),
    HARPOON("Harpoon", Arrays.asList(ItemID.HARPOON)),
    LOBSTER_POT("Cage", Arrays.asList(ItemID.LOBSTER_POT)),
    BARBARIAN_ROD("Barbarian", Arrays.asList(ItemID.BARBARIAN_ROD, ItemID.FEATHER)),
    // Add other fishing methods as required
    ;

    private final String method;
    private final List<Integer> requiredItems;

    /**
     * Constructor for defining fishing types.
     *
     * @param method The method of fishing.
     * @param requiredItems The list of items required for this type of fishing, including both primary and any secondary items.
     */
    FishingType(String method, List<Integer> requiredItems) {
        this.method = method;
        this.requiredItems = new ArrayList<>(requiredItems);
    }

    /**
     * Retrieves the method of fishing.
     *
     * @return A string representing the fishing method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Retrieves a list of items required for fishing.
     *
     * @return A list of integers representing item IDs required for the fishing method.
     */
    public List<Integer> getRequiredItems() {
        return requiredItems;
    }
}
