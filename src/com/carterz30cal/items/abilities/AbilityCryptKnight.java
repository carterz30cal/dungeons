package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityCryptKnight extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Knight");
		d.add("While in a crypt, gain 65% damage.");
		return d;
	}
	
	@Override
	public void stats(DungeonsPlayerStats s)
	{
		if (!s.o.inCrypt) return;
		s.damagemod += 0.65;
	}
}
