package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityCryptLordSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Lord");
		d.add("Double your armour whilst in a crypt.");
		d.add(ChatColor.RED + "You have no regen.");
		return d;
	}

	@Override
	public void finalStats(DungeonsPlayerStats s) 
	{
		if (s.o.inCrypt) 
		{
			s.armour *= 2;
			s.regen = 0;
		}
	}
}
