package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityCryptRaiderSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Raider");
		d.add("Whilst in a crypt, double your");
		d.add("regen and damage stats, and gain");
		d.add("100 armour.");
		return d;
	}

	public void stats(DungeonsPlayerStats s) 
	{
		if (s.o.inCrypt) 
		{
			s.armour += 100;
			s.damage *= 2;
			s.regen *= 2;
		}
	}
}
