package me.tippie.customadvancements.advancement.types;

import me.tippie.customadvancements.util.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import java.util.ArrayList;
import java.util.List;

public class Statistic extends AdvancementType {
	public Statistic() {
		super("statistic", Lang.ADVANCEMENT_TYPE_STATISTIC_UNIT.getString());
	}

	@EventHandler
	public void onStatisticChange(final PlayerStatisticIncrementEvent event) {
		progress(event, event.getPlayer().getUniqueId());
	}

	@Override protected void onProgress(final Object event, String value, final String path) {
		final PlayerStatisticIncrementEvent playerStatisticIncrementEvent = (PlayerStatisticIncrementEvent) event;
		if (value == null || value.equalsIgnoreCase("any")) {
			progression(1, path, playerStatisticIncrementEvent.getPlayer().getUniqueId());
		} else {
			boolean not = false;
			if (value.startsWith("!")) {
				value = value.substring(1);
				not = true;
			}
			final List<org.bukkit.Statistic> statistics = new ArrayList<>();
			final String[] statisticStrings = value.split(",");
			for (final String statisticsString : statisticStrings)
				statistics.add(org.bukkit.Statistic.valueOf(statisticsString.toUpperCase()));
			if ((statistics.contains(playerStatisticIncrementEvent.getStatistic()) && !not)||(!statistics.contains(playerStatisticIncrementEvent.getStatistic()) && not)) {
				final int increment = playerStatisticIncrementEvent.getNewValue() - playerStatisticIncrementEvent.getPreviousValue();
				progression(increment, path, playerStatisticIncrementEvent.getPlayer().getUniqueId());
			}
		}
	}
}
