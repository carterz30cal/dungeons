package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilityUndeadFishRod extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Bounty");
		d.add("If you were going to fish up");
		d.add("an Undead Fisherman, fish up");
		d.add("100 coins instead.");
		return d;
	}

	public String onFishIn(DungeonsPlayer d,String current)
	{ 
		if (current != null && current.equals("undead_fisherman")) 
		{
			d.coins += 100;
			d.player.sendMessage(ChatColor.GOLD + "Fished 100 coins!");
			return "none";
		}
		else return current;
	}
}
