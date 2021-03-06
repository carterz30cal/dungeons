package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;

public class AbilityTotem2 extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Ancient Power");
		d.add("Using this in a " + ChatColor.GOLD + "dig site");
		d.add("will summon the remains of");
		d.add("a mighty precursor");
		return d;
	}

}
