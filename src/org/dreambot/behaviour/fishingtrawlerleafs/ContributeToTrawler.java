package org.dreambot.behaviour.fishingtrawlerleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.framework.Leaf;
import org.dreambot.utilities.API;
import utilities.helpers.Timing;

import java.util.List;
import java.util.Random;

import static org.dreambot.api.utilities.Logger.log;

public class ContributeToTrawler extends Leaf<MinnowsAIO> {
    public static final int WIDGET_MAIN = 366;
    public static final int WIDGET_CHILD = 10;
    public static final int WIDGET_POPUP_SCREEN = 368;
    public static final int WIDGET_CHILD_CLOSE = 12;
    public static final int MIN_CONTRIBUTION_SIZE = 74;
    public static final int[] LEAK_IDS = {37353, 37354, 37355, 37356, 37357, 37358, 37359, 37360, 37361, 37362, 37363, 37364, 37365, 37366, 37367, 37368};
    public static int lastLeakId = -1;
    @Override
    public boolean isValid() {
        WidgetChild contributionWidget = Widgets.get(WIDGET_MAIN, WIDGET_CHILD, 0);
        return contributionWidget!= null && contributionWidget.isVisible();

    }

    @Override
    public int onLoop() {
        WidgetChild dontShowAgain = Widgets.get(WIDGET_POPUP_SCREEN, WIDGET_CHILD_CLOSE);
        WidgetChild closeButton = Widgets.get(WIDGET_POPUP_SCREEN, WIDGET_CHILD_CLOSE);
        if (dontShowAgain!=null && dontShowAgain.isVisible()) {
            log("Clicking 'Don't Show Again'...");
            dontShowAgain.interact();
            Sleep.sleepUntil(() -> !dontShowAgain.isVisible(), 5000);
        }
        if (closeButton!=null && closeButton.isVisible()) {
            log("Closing Trawler Popup...");
            closeButton.interact();
            Sleep.sleepUntil(() -> !closeButton.isVisible(), 5000);
        }
        WidgetChild contributionWidget = Widgets.get(WIDGET_MAIN, WIDGET_CHILD, 0);
        if (contributionWidget!=null && contributionWidget.isVisible() && getContributionPoints() < 50) {
            if (lastLeakId == -1) {
                lastLeakId = getDifferentLeakId();
            }
            GameObject leak = GameObjects.closest(lastLeakId);
            if (leak != null && leak.hasAction("Fill") && leak.interact("Fill")) {
                log("Repairing Leak...");
                Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 2000);
                Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 2000);
            } else if (leak != null && !leak.hasAction("Fill")) {
                log("Waiting...");
            }
        }
        if (contributionWidget != null && contributionWidget.isVisible() && getContributionPoints() >= 50) {
            API.needsToCollectFromTrawler = true;
        }
        return Timing.loopReturn();
    }
    private int getContributionPoints() {
        WidgetChild contributionWidget = Widgets.get(WIDGET_MAIN, WIDGET_CHILD, 0);
        if (contributionWidget!=null && contributionWidget.isVisible()) {
            int size = contributionWidget.getWidth();
            if (size >= MIN_CONTRIBUTION_SIZE) {
                return 50;
            } else {
                return (int) (size / (float) MIN_CONTRIBUTION_SIZE * 50);
            }
        }
        return 0;
    }
    private int getDifferentLeakId() {
        List<GameObject> leaks = GameObjects.all(gameObject -> {
            for (int id : LEAK_IDS) {
                if (gameObject.getID()==id) {
                    return true;
                }
            }
            return false;
        });
        GameObject currentLeak = GameObjects.closest(lastLeakId);
        leaks.remove(currentLeak);
        if (!leaks.isEmpty()) {
            Random rand = new Random();
            GameObject newLeak = leaks.get(rand.nextInt(leaks.size()));

            //log("Moving to a different leak: " + newLeak.getID());
            return newLeak.getID();
        } else {
            //log("No different leaks found, defaulting to first leak: " + LEAK_IDS[0]);
            return LEAK_IDS[0];
        }
    }
}
