package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityStormbreaker extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Storm Titan");
		desc.add("Deals double damage while raining");
		return desc;
	}

	
	@Override
	public int onAttack(DungeonsPlayer d,DMob mob,int damage)
	{
		if (DungeonManager.i.hash(d.player.getLocation().getBlockZ()) == 1 && Dungeons.w.hasStorm()) return damage * 2;
		else return damage;
	}
}
