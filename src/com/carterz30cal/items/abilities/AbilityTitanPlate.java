package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityTitanPlate extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Titan");
		desc.add("Upon being hit, regain 5 health");
		return desc;
	}

	@Override
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob)
	{
		if (c == DamageCause.ENTITY_ATTACK || c == DamageCause.PROJECTILE) d.heal(5);
		return 1;
	}
}
