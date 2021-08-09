package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class AbilityCorruption extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Total Corruption");
		d.add("Convert any health over 100 to damage.");
		d.add(ChatColor.RED + "-25% armour.");
		return d;
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		int conversion = Math.max(0, s.health - 100);
		s.damage += conversion;
		
		if (conversion > 0) s.health = 100;
		s.armour *= 0.75;
	}

}
