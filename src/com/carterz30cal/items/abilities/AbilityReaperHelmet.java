package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;


public class AbilityReaperHelmet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Spirit of Souls");
		d.add("For every soul alive, you");
		d.add("gain 35 mana.");
		return d;
	}

	public void stats(DungeonsPlayerStats s) 
	{
		int alive = 0;
		for (Soul soul : AbilityReaperBlade.souls.getOrDefault(s.o, new ArrayList<>())) if (soul.summoned) alive++;
		s.mana += alive*35;
	}
}
