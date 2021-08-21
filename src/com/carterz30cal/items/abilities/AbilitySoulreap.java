package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilitySoulreap extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Soul Reap");
		desc.add("You regain 10% of your max health");
		desc.add(" every time you kill an enemy");
		return desc;
	}

	@Override
	public void onKill (DungeonsPlayer d,DMobType mob)
	{
		d.heal(0.1d);
	}
}
