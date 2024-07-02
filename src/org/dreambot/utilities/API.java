package org.dreambot.utilities;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import utilities.helpers.OwnedItems;

import static Constants.ItemID.*;

public class API {

    public static String currentBranch = "";
    public static String currentLeaf = "";
    public static int TRAWLER_WORLD = 370;
    public static final int TURN_IN_COUNT = Calculations.random(100_000,160_000);
    public static final int KYLIE_MINNOW_ID = 7735;
    public static boolean needsToCollectFromTrawler = false;
    public static final Area TRAWLER_LOBBY  =  new Area(2673, 3166, 2669, 3183, 1);
    public static final Area TRAWLER_AREA =  new Area(2641, 3138, 2686, 3175, 0);
    public static final Tile TRAWLER_START = new Tile(2675,3170,0);
    public static final Area KICKED_OUT_TRAWLER = new Area(2669, 3219, 2676, 3223, 0);
    public static final int[] ANGLER_PIECES = {ANGLER_HAT,ANGLER_TOP,ANGLER_WADERS,ANGLER_BOOTS};
    public static boolean hasFullAngler() {
        int anglerPieceCount = 0;
        for (int anglerPiece : ANGLER_PIECES) {
            if (OwnedItems.contains(anglerPiece)) {
                anglerPieceCount++;
            }
        }
        //log(anglerPieceCount + " Pieces of Angler");
        return anglerPieceCount==ANGLER_PIECES.length;
    }
    //varbit 5669 value 2 means minnows is unlocked
    public static boolean hasUnlockedMinnows() {
        return PlayerSettings.getBitValue(5669) == 2;

    }

}
