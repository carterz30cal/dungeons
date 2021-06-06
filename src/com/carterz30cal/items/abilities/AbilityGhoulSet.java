package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityGhoulSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Ghoul Lord");
		desc.add("Take 12% less damage from ghouls");
		desc.add("This ability stacks.");
		return desc;
	}
	
	@Override
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob)
	{
		if (mob != null && mob.tags.contains("ghoul")) return 0.88;
		else return 1;
	}
}
