package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityCryptMasterSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Master");
		d.add("Whilst in a crypt, double your");
		d.add("regen, triple your damage");
		d.add("stat and gain 125 armour.");
		return d;
	}

	public void stats(DungeonsPlayerStats s) 
	{
		if (s.o.inCrypt) 
		{
			s.armour += 125;
			s.damage *= 3;
			s.regen *= 2;
		}
	}
}
