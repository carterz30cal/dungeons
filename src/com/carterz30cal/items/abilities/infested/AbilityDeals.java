package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityDeals extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Charismatic Trader");
		d.add("Get 3% off at all shops.");
		d.add("This stacks multiplicatively");
		d.add("with the charismatic skill.");
		return d;
	}
	
	public void stats(DungeonsPlayerStats s) 
	{
		s.shopDiscount *= 0.97;
	}

}
