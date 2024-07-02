package org.dreambot.behaviour.mulingLeafs;


import org.dreambot.MinnowsAIO;
import org.dreambot.framework.Branch;
import utilities.mule.MuleRequestsManager;
import utilities.mule.ServerResponse;

import static org.dreambot.api.utilities.Logger.debug;

public class MulingBranch extends Branch<MinnowsAIO> {

	private final MuleRequestsManager muleManager;

	public MulingBranch(MuleRequestsManager muleManager) {
		this.muleManager = muleManager;
	}

	@Override
	public boolean isValid() {
		ServerResponse request = muleManager.getServerResponse();
		if (request == null){
			debug("Server Response is Null");
			return false;
		}
		if(request.getPlayerName() == null) {
			debug("Sever Response Not Null But Name is Null.");

		}
		return true;
	}
}
