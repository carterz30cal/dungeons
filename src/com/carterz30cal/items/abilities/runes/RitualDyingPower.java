package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

import org.bukkit.ChatColor;

public class RitualDyingPower extends AbsAbility
{
	public static Map<DungeonsPlayer,Integer> cd = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(ritual + "Dying Power");
		d.add(ChatColor.RED + "+ 1.25x mana");
		d.add(ChatColor.RED + "- Your health is capped at your mana percent");
		return d;
	}

	public void finalStats(DungeonsPlayerStats s) 
	{
		s.mana *= 1.25;
		s.o.setHealth(Math.min(s.o.getHealthPercent(),s.o.getManaPercent()));
	}
	
	public boolean isRitual() {return true;}
	
}
