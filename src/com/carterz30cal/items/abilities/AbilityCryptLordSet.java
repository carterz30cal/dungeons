package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityCryptLordSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Lord");
		d.add("Gain 45% more armour while in a crypt.");
		return d;
	}

	@Override
	public void finalStats(DungeonsPlayerStats s) 
	{
		if (s.o.inCrypt) s.armour = (int)((double)s.armour * 1.45d);
	}
}
