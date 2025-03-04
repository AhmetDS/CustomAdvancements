package me.tippie.customadvancements.advancement.types;

import lombok.val;
import me.tippie.customadvancements.util.Lang;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class BreedEntity extends AdvancementType{
    public BreedEntity() {
        super("breedentity", Lang.ADVANCEMENT_TYPE_BREEDENTITY_UNIT.getString());
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event){
        if (event.getBreeder() instanceof Player){
            progress(event, event.getBreeder().getUniqueId());
        }
    }

    @Override protected void onProgress(final Object event, String value, final String path) {
        val entityBreedEvent = (EntityBreedEvent) event;
        val player = (Player) entityBreedEvent.getBreeder();
        assert player != null;
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
            if ((entities.contains(entityBreedEvent.getEntityType()) && !not) || (!entities.contains(entityBreedEvent.getEntityType()) && not)) {
                progression(1, path, player.getUniqueId());
            }
        }
    }
}
