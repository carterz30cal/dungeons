package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityWebSet extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Webbed");
		d.add("Take 30% less damage from spiders");
		d.add("in the infested caverns.");
		return d;
	}
	
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{ 
		if (mob != null && mob.tags.contains("spider") && mob.tags.contains("infested")) return 0.7;
		return 1; 
	}

}
