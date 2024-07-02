package org.dreambot;


import Constants.ItemID;
import org.dreambot.api.Client;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.randoms.RandomManager;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.widgets.message.Message;
import org.dreambot.api.wrappers.widgets.message.MessageType;
import org.dreambot.behaviour.FishingBranch;
import org.dreambot.behaviour.FishingTrawlerBranch;

import org.dreambot.behaviour.fishingleafs.*;
import org.dreambot.behaviour.fishingtrawlerleafs.*;


import org.dreambot.behaviour.quests.fishingcontest.*;
import org.dreambot.behaviour.bondLeafs.BondingBranch;
import org.dreambot.behaviour.bondLeafs.BuyBond;
import org.dreambot.behaviour.bondLeafs.RedeemBond;
import org.dreambot.behaviour.disablesettings.*;
import org.dreambot.framework.Tree;
import org.dreambot.behaviour.mulingLeafs.*;
import org.dreambot.paint.CustomPaint;
import org.dreambot.paint.PaintInfo;

import org.dreambot.utilities.BankOnceLeaf;
import org.dreambot.utilities.FishingBreakSolver;
import org.dreambot.utilities.FishingSpotEnum;
import org.dreambot.webnodes.MinnowNodes;
import org.dreambot.webnodes.RemoveFishingContestNodes;

import utilities.API;
import utilities.helpers.OwnedItems;
import utilities.helpers.QuantityFormatter;
import utilities.helpers.Timing;
import utilities.mule.MuleConfig;
import utilities.mule.MuleRequestsManager;
import utilities.mule.MuleWebSocketClientManager;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static Constants.ItemID.COINS;
import static org.dreambot.utilities.API.*;
import static utilities.helpers.WorldsHelper.wHopMember;
@ScriptManifest(category = Category.MISC, author = "Dumbrat691", name = "!MinnowsAIO", description = "u cant fuck with me nigga", version = 1.0)
public class MinnowsAIO extends AbstractScript implements PaintInfo, ChatListener {

    //MULE IMPORTS
    private MuleRequestsManager muleRequestsManager;
    private MuleWebSocketClientManager webSocketClientManager;
    private MuleConfig tradeConfig;
    private static final String SERVER_URI = "ws://localhost:52300";

    private final Tree<MinnowsAIO> tree = new Tree<>();
    private final CustomPaint CUSTOM_PAINT = new CustomPaint(this,
            CustomPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN,
            new Color[]{new Color(244, 164, 96)}, // Sandy Brown for font color
            "Calisto MT", // Font
            new Color[]{new Color(85, 107, 47)}, // Dark Olive Green for text background
            new Color[]{new Color(210, 105, 30)}, // Chocolate for text background border
            1, false, 5, 0, 0);
    private final RenderingHints aa = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


    private long startTime;
    private long pauseTime;
    private boolean paused;
    private boolean hasStartedTimer = false;
    private static final int FISHING_LEVEL_THRESHOLD = 0;
    private boolean initialSetupCompleted = false;
    public void setInitialSetupCompleted(boolean completed) {
        this.initialSetupCompleted = completed;
    }
    private static final HashMap<FishingSpotEnum, Area> selectedFishingSpots = new HashMap<>();
    private FishingBreakSolver breakSolver;
    RandomManager randomManager;

    public void selectFishingSpots() {
        Random rand = new Random();
        for (FishingSpotEnum spot : FishingSpotEnum.values()) {
            // Randomly select one of the areas assigned to each fishing type
            List<Area> areas = spot.getFishingAreas();
            Area selectedArea = areas.get(rand.nextInt(areas.size()));
            selectedFishingSpots.put(spot, selectedArea);
        }
    }

    public static Area getSelectedFishingArea(FishingSpotEnum spot) {
        return selectedFishingSpots.get(spot);
    }

    /**
     * @param args script quick launch arguments
     */
    @Override
    public void onStart(String... args) {

        randomManager = Client.getInstance().getRandomManager();
        randomManager.getBreakSolver().setForceDisable(true);
        breakSolver = new FishingBreakSolver();
        randomManager.registerSolver(breakSolver);
        // Initialize Mule Config
        tradeConfig = new MuleConfig(
            -1, // membershipDaysThreshold
            500000, // bufferAmount
            250000, // maxCoinsToKeep
            1000000, // minCoinsToGive
            100_000, // minCoinsBeforeRequesting
            150000 // requestAmountWhenLow
        );
        muleRequestsManager = new MuleRequestsManager(null, tradeConfig);
        webSocketClientManager = new MuleWebSocketClientManager(SERVER_URI, muleRequestsManager);
        muleRequestsManager.setWebSocketClientManager(webSocketClientManager);


        startTime = System.currentTimeMillis();
        selectFishingSpots();
        instantiateTree();
        Logger.log(TURN_IN_COUNT);
    }
    /**
     * On start from script launcher
     */
    @Override
    public void onStart() {

        randomManager = Client.getInstance().getRandomManager();
        randomManager.getBreakSolver().setForceDisable(true);
        breakSolver = new FishingBreakSolver();
        randomManager.registerSolver(breakSolver);
        Logger.log(TURN_IN_COUNT);
        // Initialize Mule Config
        tradeConfig = new MuleConfig(
                -1, // membershipDaysThreshold
                500000, // bufferAmount
                250000, // maxCoinsToKeep
                1000000, // minCoinsToGive
                100_000, // minCoinsBeforeRequesting
                150000 // requestAmountWhenLow
        );
        muleRequestsManager = new MuleRequestsManager(null, tradeConfig);
        webSocketClientManager = new MuleWebSocketClientManager(SERVER_URI, muleRequestsManager);
        muleRequestsManager.setWebSocketClientManager(webSocketClientManager);

        startTime = System.currentTimeMillis();
        selectFishingSpots();
        instantiateTree();
    }
    @Override
    public int onLoop() {
        startFISHINGSkillTracking();
        if (breakSolver.shouldExecute()){
            randomManager.disableSolver(RandomEvent.LOGIN);
            return breakSolver.onLoop();
        }  else {
            randomManager.enableSolver(RandomEvent.LOGIN);
        }

        if (Skills.getRealLevel(Skill.FISHING) == 82 && !Client.isMembers() && breakSolver.getLevelAchievedTime() == null) {
            breakSolver.setLevelAchievedTime(LocalDateTime.now());
            return Timing.loopReturn();
        }
        if (Skills.getRealLevel(Skill.FISHING) >= 82 && tradeConfig.getMembershipDaysThreshold() < 1){
            tradeConfig.setMembershipDaysThreshold(1);
            return Timing.loopReturn();
        }
        if (shouldHopToWorld()){
            wHopMember();
            return Timing.loopReturn();
        }
        if(initialSetupCompleted && !muleRequestsManager.isTradeRequestOnCooldown()) {
            if(muleRequestsManager.handleTradeRequests()) {
                Sleep.sleepUntil(() -> muleRequestsManager.getServerResponse()!=null, 60_000);
                return Timing.loopReturn();
            }
        }
        if(!webSocketClientManager.isConnected()){
            muleRequestsManager.resetServerResponse();
            debug("Resetting Server Response");
        } else {
            Logger.log("We Are Connected To Mule Server");
        }

       
        return this.tree.onLoop();
    }

    @Override
    public void onExit() {
        if (webSocketClientManager.isConnected()){
            log("Stopped Script, Disconnecting From Muling Server.");
            webSocketClientManager.disconnect();
            muleRequestsManager.resetServerResponse();
        }
    }
    private void instantiateTree() {
        tree.addBranches(new MinnowNodes(),new BankOnceLeaf(this),
            new DisableSettingsBranch().addLeafs(
                new EnableFixedScreen(), new DisableAutoRetaliate(), new ActivateSkullPrevention(),
                new EnableEscapeClosesInterfaces(), new AutoEquipAmmo(), new DisableBuyPriceWarning(),
                new DisableHelpAid(), new DisableLevelUp(), new DisableRoofs(), new DisableTradeDelay(),
                new DisableWorldHopConfirmation(), new EnableShiftDrop(), new CloseSettings()),
            new BankOnceLeaf(this),
            new MulingBranch(muleRequestsManager).addLeafs(new BankForMule(muleRequestsManager), new WalkToMulingLoc(muleRequestsManager),
                new InitiateTrade(muleRequestsManager), new ExecuteTrade(muleRequestsManager)),
            new BondingBranch(muleRequestsManager,new RedeemBond()).addLeafs(new BuyBond(muleRequestsManager), new RedeemBond()),
                new FishingContestBranch(muleRequestsManager).addLeafs().addLeafs(new RemoveFishingContestNodes(),new PrepareForFishingContest(),new StartFishingContest(),new FishingContestStepOne(),new FishingContestStepTwo()),
                new FishingTrawlerBranch(muleRequestsManager).addLeafs(new LootTrawler(),new PrepareForTrawler(),new JoinTrawlerGame(),new ContributeToTrawler(),new TravelToTrawler()),
                new FishingBranch(muleRequestsManager).addLeafs(new SellSharkLeaf(),new SetupEquipmentLeaf(),new UnlockMinnowArea(),new TurnInMinnows(),new WalkToFishingSpot(),new FishingLeaf())


        );
    }

    /**
     * paint for the script
     */
    @Override
    public void onPaint(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        gg.setRenderingHints(aa);
        gg.setFont(new Font("Calisto MT", Font.PLAIN, 12));
        CUSTOM_PAINT.paint(gg);
    }
    @Override
    public void onPause() {
        if (!paused) {
            pauseTime = System.currentTimeMillis();
            paused = true;
        }
    }
    @Override
    public void onResume() {
        if (paused) {
            long pauseDuration = System.currentTimeMillis() - pauseTime;
            startTime += pauseDuration;
            paused = false;
        }
    }
    @Override
    public String[] getPaintInfo() {
        List<String> paintInfo = new ArrayList<String>();
        int currentLevel = Skills.getRealLevel(Skill.FISHING);
        long elapsedSeconds = paused ? (pauseTime - startTime) / 1000:(System.currentTimeMillis() - startTime) / 1000;
        double elapsedHours = elapsedSeconds / 3600.0; // Convert seconds to hours
        long xpGained = SkillTracker.getGainedExperience(Skill.FISHING);
        double xpPerHour = xpGained / elapsedHours;
        String runtimeInfo = "Runtime: " + formatTime(elapsedSeconds);
        paintInfo.add(getManifest().name() + " V" + getManifest().version());
        paintInfo.add(runtimeInfo);
        paintInfo.add("Current Branch: " + currentBranch);
        paintInfo.add("Current Leaf: " + currentLeaf);
        paintInfo.add("Fishing Level: " + currentLevel + "(+" + SkillTracker.getGainedLevels(Skill.FISHING) + ")" + " XP Gained: " + xpGained + " (" + (int) xpPerHour + "/Hr)");
        paintInfo.add("Total Coins: " + QuantityFormatter.quantityToRSDecimalStack(OwnedItems.count(COINS), true));
        if (Skills.getRealLevel(Skill.FISHING) >= 82 && hasFullAngler()) {
            paintInfo.add("Minnows: " + QuantityFormatter.quantityToRSDecimalStack(OwnedItems.count(ItemID.MINNOW), true));
            paintInfo.add("Can Turn In For :" + QuantityFormatter.quantityToRSDecimalStack(OwnedItems.count(ItemID.MINNOW) / 40, true) + " Sharks");
        }
        return paintInfo.toArray(new String[0]);
    }
    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    @Override
    public void onMessage(Message msg) {
        String gameMessage = msg.getMessage();
        debug("Received message of type: " + msg.getType().name() + " with content: " + msg.getMessage());

        if (msg.getType() == MessageType.TRADE_COMPLETE && gameMessage.contains("Accepted trade.")) {
            log("Disconnecting From Server.");
            muleRequestsManager.resetServerResponse();
            webSocketClientManager.disconnect();
        }
        switch (msg.getType()) {
            case TIMEOUT_MESSAGE:
            case GAME:
            case ENGINE:
            case WELCOME:
            case FEEDBACK:
            case BROADCAST:
            case NPC_EXAMINE:
            case ITEM_EXAMINE:
            case OBJECT_EXAMINE:
            case FILTERED:
            case AUTO:
            case CHAT_CHANNEL:
            case CLAN_CHAT:
            case CLAN_CREATION_INVITE:
            case CLAN_GUEST_CHAT:
            case CLAN_GUEST_MESSAGE:
            case CLAN_IRON_MAN_FORM_GROUP:
            case CLAN_IRON_MAN_GROUP_WITH:
            case CLAN_MESSAGE:
            case CLAN_WARS_CHALLENGE:
            case DIALOG:
            case DUEL:
            case FRIENDLY_DUEL:
            case FRIENDS_NOTIFICATION:
            case MOD_AUTO:
            case MOD_CHAT:
            case MOD_PRIVATE_CHAT:
            case NEW_FRIEND_NOTIFICATION:
            case NEW_IGNORE_NOTIFICATION:
            case PLAYER:
            case PRIVATE_INFO:
            case PRIVATE_RECV:
            case PRIVATE_SENT:
            case TRADE:
            case TRADE_COMPLETE:
            case TRADE_SENT:
                API.lastGameMessage = msg.getMessage();
                API.lastGameMessageTile = Players.getLocal().getTile();
                break;
        }
    }
    private void startFISHINGSkillTracking() {
        if (!hasStartedTimer && Client.isLoggedIn() && Skills.getRealLevel(Skill.FISHING) > FISHING_LEVEL_THRESHOLD) {
            SkillTracker.resetAll();
            SkillTracker.start(Skill.FISHING, true);
            hasStartedTimer = true;
        }
    }
    private boolean shouldHopToWorld() {
        return Client.isMembers() && Worlds.getCurrent().isF2P();
    }


}



