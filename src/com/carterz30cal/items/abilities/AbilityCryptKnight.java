package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityCryptKnight extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Knight");
		d.add("While in a crypt, double your Knight skill");
		return d;
	}
	
	@Override
	public void stats(DungeonsPlayerStats s)
	{
		if (!s.o.inCrypt) return;
		CharacterSkill l = s.o.level;
		
		s.damage += l.get("damage");
	}
}
