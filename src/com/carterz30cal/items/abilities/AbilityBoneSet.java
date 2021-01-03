package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityBoneSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Solid Bone");
		desc.add("Take 20% less damage from skeletons");
		return desc;
	}

	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{ 
		if (mob != null && mob.tags.contains("skeleton")) return 0.8;
		return 1;
	}
}
