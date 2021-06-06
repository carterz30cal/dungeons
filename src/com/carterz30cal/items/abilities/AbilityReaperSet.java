package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;


public class AbilityReaperSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Soulseeker");
		d.add("You can have 2 more souls alive.");
		d.add("Harvested souls are two levels higher.");
		d.add("Your souls don't lose health over time.");
		return d;
	}
	
	@Override
	public void stats(DungeonsPlayerStats s)
	{
		s.maxsouls += 2;
	}


}
