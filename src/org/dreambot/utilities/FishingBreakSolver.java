package org.dreambot.utilities;


import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.randoms.BreakSolver;
import org.dreambot.api.utilities.Logger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class FishingBreakSolver extends BreakSolver {
	private LocalDateTime levelAchievedTime;
	private boolean isBreakScheduled = false;
	private LocalDateTime nextNoon;

	public void setLevelAchievedTime(LocalDateTime time) {
		this.levelAchievedTime = time;
		this.isBreakScheduled = false;
	}
	public LocalDateTime getLevelAchievedTime() {
		return levelAchievedTime;
	}

	@Override
	public boolean shouldExecute() {
		if (levelAchievedTime == null) {
			return false;
		}
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

		if (levelAchievedTime.toLocalTime().isBefore(LocalTime.of(12, 30))) {
			nextNoon = levelAchievedTime.withHour(12).withMinute(30).withSecond(0);
		} else {
			nextNoon = levelAchievedTime.plusDays(1).withHour(12).withMinute(30).withSecond(0);
		}
		return now.isBefore(nextNoon) && !isBreakScheduled;
	}


	@Override
	public int onLoop() {
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		long breakDurationMinutes = ChronoUnit.MINUTES.between(now, nextNoon);
		Logger.log("Taking a break for " + breakDurationMinutes + " minutes.");
		isBreakScheduled = true;
		Tabs.logout();
		return (int) breakDurationMinutes * 60000;
	}
}



