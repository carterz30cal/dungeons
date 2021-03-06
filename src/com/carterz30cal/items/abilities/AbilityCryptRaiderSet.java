package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityCryptRaiderSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Raider");
		d.add("Gain 100 armour and 30 damage while in a crypt");
		return d;
	}

	public void stats(DungeonsPlayerStats s) 
	{
		if (s.o.inCrypt) 
		{
			s.armour += 100;
			s.damage += 30;
		}
	}
}
