package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.player.DungeonsPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class AbilityLeafSet extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Natural Power");
		d.add("Gain 25 health for every nearby " + ChatColor.GREEN + "player");
		d.add("Gain 2 damage for every nearby " + ChatColor.RED + "enemy");
		d.add("Radius of 11 blocks");
		return d;
	}

	public void stats(DungeonsPlayerStats s) 
	{
		for (Entity e : Dungeons.w.getNearbyEntities(s.p.getLocation(), 11, 11, 11))
		{
			if (DMobManager.get(e) != null) s.damage += 2;
			else if (e instanceof Player && e != s.p) s.health += 25;
		}
	}
}
