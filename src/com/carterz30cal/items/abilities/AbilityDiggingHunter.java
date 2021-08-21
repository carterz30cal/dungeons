package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityDiggingHunter extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{

		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "True Hunter");
		d.add("Reduce damage taken by 30%");
		d.add("from ancient mobs.");
		return d;
	}

	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{
		if (mob != null && mob.tags.contains("ancient")) return 0.7;
		return 1;
	} 

}
