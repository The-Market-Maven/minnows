package org.dreambot.utilities;

import Constants.ItemID;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.utilities.FishingType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum FishingSpotEnum {
    SHRIMP_SPOT(
            FishingType.SMALL_NET,
            Arrays.asList(ItemID.RAW_SHRIMPS,ItemID.RAW_ANCHOVIES),
            Arrays.asList(new Area(3246, 3155, 3234, 3141)),
            1,
            Arrays.asList(1530)
    ),
    TROUT_SPOT(
            FishingType.FLY_FISHING,
            Arrays.asList(ItemID.RAW_TROUT, ItemID.RAW_SALMON),
            Arrays.asList(new Area(
            new Tile(3099, 3422, 0),
            new Tile(3104, 3422, 0),
            new Tile(3104, 3425, 0),
            new Tile(3109, 3431, 0),
            new Tile(3109, 3435, 0),
            new Tile(3100, 3434, 0))),
            20,
            Arrays.asList(1526)
    ),

    MINNOWS_SPOT(
            FishingType.SMALL_NET,
            Arrays.asList(ItemID.MINNOW),
            Arrays.asList(new Area(2622, 3446, 2607, 3441)),
            82,
            Arrays.asList(7730, 7731, 7732, 7733)
    );

    private final FishingType fishingType;
    private final List<Integer> fishTypeIDs;
    private final List<Area> fishingAreas;
    private final int requiredFishingLevel;
    private final List<Integer> fishingSpotIDs;

    FishingSpotEnum(FishingType fishingType, List<Integer> fishTypeIDs, List<Area> fishingAreas, int requiredFishingLevel, List<Integer> fishingSpotIDs) {
        this.fishingType = fishingType;
        this.fishTypeIDs = Collections.unmodifiableList(fishTypeIDs);
        this.fishingAreas = Collections.unmodifiableList(fishingAreas);
        this.requiredFishingLevel = requiredFishingLevel;
        this.fishingSpotIDs = fishingSpotIDs;
    }

    public FishingType getFishingType() {
        return fishingType;
    }

    public List<Integer> getFishTypeIDs() {
        return fishTypeIDs;
    }

    public List<Area> getFishingAreas() {
        return fishingAreas;
    }

    public int getRequiredFishingLevel() {
        return requiredFishingLevel;
    }

    public List<Integer> getFishingSpotIDs() {
        return fishingSpotIDs;
    }

    /**
     * Finds the highest level fishing spot that the player has the requirements to use.
     * @param playerFishingLevel The current fishing level of the player.
     * @return The fishing spot with the highest requirements that the player can access.
     */
    public static FishingSpotEnum findHighestAccessibleFishingSpot(int playerFishingLevel) {
        FishingSpotEnum bestSpot = null;
        for (FishingSpotEnum spot : values()) {
            if (spot.getRequiredFishingLevel() <= playerFishingLevel) {
                if (bestSpot == null || spot.getRequiredFishingLevel() > bestSpot.getRequiredFishingLevel()) {
                    bestSpot = spot;
                }
            }
        }
        return bestSpot;
    }
}
