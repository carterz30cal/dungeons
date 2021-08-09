package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class AbilitySpiritSword extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Blade of the Spirit");
		d.add("Your spirits also heal you for 3❤ every");
		d.add("time they attack but use 2 more mana per attack.");
		d.add("For every spirit alive, this sword deals 35 more damage.");
		d.add(ChatColor.LIGHT_PURPLE + "+10% mana.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		int s = AbilitySpiritHarvester.spirits.getOrDefault(d,new ArrayList<>()).size() * 35;
		return damage + s;
	} 
	
	@Override
	public void finalStats(DungeonsPlayerStats s)
	{
		s.mana *= 1.1;
	}

}
