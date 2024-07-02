package org.dreambot.behaviour.fishingleafs;

import org.dreambot.MinnowsAIO;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.framework.Leaf;
import org.dreambot.utilities.API;


import static Constants.ItemID.MINNOW;
import static org.dreambot.utilities.API.KYLIE_MINNOW_ID;


public class TurnInMinnows extends Leaf<MinnowsAIO> {

	@Override
	public boolean isValid() {
		return Inventory.count(MINNOW) >= API.TURN_IN_COUNT;
	}

	@Override
	public int onLoop() {
		 NPC KYLIE_MINNOW = NPCs.closest(KYLIE_MINNOW_ID);
		if (!Dialogues.inDialogue() && KYLIE_MINNOW.canReach()) {
			KYLIE_MINNOW.interact("trade");
			Sleep.sleepUntil(Dialogues::inDialogue, 5000);
		}
		if (Dialogues.inDialogue() && Dialogues.canContinue()) {
			Dialogues.spaceToContinue();
		}
		if (Dialogues.canEnterInput()) {
			int amountToTrade = Inventory.count(MINNOW) / 40;
			Keyboard.type(amountToTrade,true);
		}
		return 100;
	}

}



