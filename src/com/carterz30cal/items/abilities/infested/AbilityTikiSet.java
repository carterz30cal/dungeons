package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.player.DungeonsPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class AbilityTikiSet extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Tiki Totem");
		d.add("Gain 6 mana and 4 armour for");
		d.add("every nearby " + ChatColor.RED + "enemy");
		d.add("Radius of 8 blocks");
		return d;
	}

	public void stats(DungeonsPlayerStats s) 
	{
		List<DMob> found = new ArrayList<>();
		for (Entity e : Dungeons.w.getNearbyEntities(s.p.getLocation(), 8, 8, 8))
		{
			DMob m = DMobManager.get(e);
			if (m != null && !found.contains(m)) 
			{
				s.mana += 6;
				s.armour += 4;
				found.add(m);
			}
		}
	}

}
