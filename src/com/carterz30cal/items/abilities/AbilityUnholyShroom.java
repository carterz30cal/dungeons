package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityUnholyShroom extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Cursed");
		d.add("Also strikes you for 6% of the damage");
		d.add("Deals 33% more damage to mushroom enemies");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		if (dMob.type.tags.contains("mushroom")) damage = (int) (damage * 1.33d);
		d.damage((int)(damage * 0.06d), false);
		d.player.damage(1);
		return damage;
	}
}
