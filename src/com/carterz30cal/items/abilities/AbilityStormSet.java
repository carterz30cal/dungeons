package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;



public class AbilityStormSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> des = new ArrayList<String>();
		des.add(prefix + "Zeus Apprentice");
		des.add("During rain, you deal");
		des.add(" 11 damage to every enemy");
		des.add(" within 8 blocks every time");
		des.add(" you are hit");
		return des;
	}

	@Override
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob)
	{
		if (!(DungeonManager.i.hash(d.player.getLocation().getBlockZ()) == 1 && Dungeons.w.hasStorm())) return 1;
		if (c == DamageCause.ENTITY_ATTACK || c == DamageCause.PROJECTILE)
		{
			Collection<Entity> entities = d.player.getWorld().getNearbyEntities(d.player.getLocation(), 8, 8, 8);
			for (Entity e : entities) if (DMobManager.mobs.containsKey(e.getUniqueId())) DMobManager.mobs.get(e.getUniqueId()).damage(11,d.player);
		}
		return 1;
	}
}
