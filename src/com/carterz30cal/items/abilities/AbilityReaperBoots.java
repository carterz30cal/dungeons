package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;


public class AbilityReaperBoots extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Force of Souls");
		d.add("For every soul alive, you");
		d.add("gain 25 health.");
		return d;
	}

	public void stats(DungeonsPlayerStats s) 
	{
		int alive = 0;
		for (Soul soul : AbilityReaperBlade.souls.getOrDefault(s.o, new ArrayList<>())) if (soul.summoned) alive++;
		s.health += alive*25;
	}
}
