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
		desc.add("Upon taking damage, regain 10 health");
		return desc;
	}

	@Override
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob)
	{
		d.heal(10);
		return 1;
	}
}
