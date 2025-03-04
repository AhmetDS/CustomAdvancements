package me.tippie.customadvancements.advancement.types;

import lombok.val;
import me.tippie.customadvancements.util.Lang;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DamageDealt extends AdvancementType {
	public DamageDealt() {
		super("damagedealt", Lang.ADVANCEMENT_TYPE_DAMAGEDEALT_UNIT.getString());
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			progress(event, Objects.requireNonNull(((Player) event.getDamager()).getPlayer()).getUniqueId());
		}
	}

	@Override protected void onProgress(final Object event, String value, final String path) {
		val entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
		val player = (Player) entityDamageByEntityEvent.getEntity();
		if (value == null || value.equalsIgnoreCase("any")) {
			progression(1, path, player.getUniqueId());
		} else {
			boolean not = false;
			if (value.startsWith("!")) {
				value = value.substring(1);
				not = true;
			}
			final List<EntityType> entities = new ArrayList<>();
			final String[] entityStrings = value.split(",");
			for (final String causeString : entityStrings)
				entities.add(EntityType.valueOf(causeString.toUpperCase()));
			if ((entities.contains(entityDamageByEntityEvent.getEntityType()) && !not) || (!entities.contains(entityDamageByEntityEvent.getEntityType()) && not)) {
				progression((int) entityDamageByEntityEvent.getDamage(), path, player.getUniqueId());
			}
		}
	}
}
