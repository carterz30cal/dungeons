package com.carterz30cal.utility;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import com.carterz30cal.dungeons.Dungeons;

public class ArmourstandFunctions
{
	public static ArmorStand create(Location spawn)
	{
		ArmorStand display = (ArmorStand) Dungeons.w.spawnEntity(spawn, EntityType.ARMOR_STAND);
		display.setCustomNameVisible(true);
		display.setVisible(false);
		display.setInvulnerable(true);
		display.setGravity(false);
		display.setRemoveWhenFarAway(false);
		display.setSmall(true);
		return display;
	}
}
