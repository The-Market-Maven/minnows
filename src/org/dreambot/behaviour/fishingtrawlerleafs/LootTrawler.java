package org.dreambot.behaviour.fishingtrawlerleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.framework.Leaf;
import org.dreambot.utilities.API;
import utilities.helpers.Timing;

import java.util.Arrays;

import static org.dreambot.api.utilities.Logger.log;
import static org.dreambot.utilities.API.ANGLER_PIECES;
import static org.dreambot.utilities.API.KICKED_OUT_TRAWLER;

public class LootTrawler extends Leaf<MinnowsAIO> {
    public static final int WIDGET_POPUP_SCREEN = 367;
    @Override
    public boolean isValid() {
        return API.needsToCollectFromTrawler;
    }

    @Override
    public int onLoop() {
        for (int anglerPiece : ANGLER_PIECES) {
            if (Inventory.contains(anglerPiece)) {
                Inventory.interact(anglerPiece, "Wear");
                Sleep.sleepUntil(() -> Equipment.contains(anglerPiece), 5000);
            }
        }
        WidgetChild alreadyLootedMessage = Widgets.get(217,6);
        if (alreadyLootedMessage != null && alreadyLootedMessage.isVisible() && alreadyLootedMessage.getText().contains("I'd better not")) {
            log("Already looted Trawler net...");
            API.needsToCollectFromTrawler = false;
        }
        WidgetChild lootScreenWidget = Widgets.get(WIDGET_POPUP_SCREEN, 0);
        if (lootScreenWidget != null && lootScreenWidget.isVisible()) {
            log("Checking for Angler pieces in loot...");
            for (int i = 0; i <= 21; i++) {
                WidgetChild anglerPiece = Widgets.get(WIDGET_POPUP_SCREEN, 4, i);
                if (anglerPiece != null && Arrays.stream(ANGLER_PIECES).anyMatch(piece -> piece == anglerPiece.getItemId())) {
                    log("Found Angler piece: " + anglerPiece.getItemId());
                    anglerPiece.interact();
                    Sleep.sleepUntil(() -> Inventory.contains(anglerPiece.getItemId()), 2500);
                    return Timing.loopReturn();
                }
            }
        }
        WidgetChild lootButton = Widgets.get(367, 19);
        if (lootButton != null && lootButton.isVisible()) {
            if (lootButton.interact()) {
                log("Collecting loot from Trawler net...");
                API.needsToCollectFromTrawler = false;
                return Timing.loopReturn();
            }
        }
        GameObject trawlerNet = GameObjects.closest(2483);
        if (trawlerNet != null && trawlerNet.interact("Inspect")) {
            log("Inspecting Trawler net...");
            Sleep.sleepUntil(() -> lootScreenWidget != null,() -> Players.getLocal().isMoving(), 2500,50);
            return Timing.loopReturn();
        }



        return Timing.loopReturn();
    }
}

