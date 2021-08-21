package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.items.magic.ProjectileParticle;
import com.carterz30cal.player.DungeonsPlayer;

public class ModCryptGrimoire extends AbsAbility
{
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(modifier + "Crypt Mage's Grimoire");
		d.add("While in a crypt, your spells");
		d.add("deal 50% more damage,");
		d.add("move 20% faster and pierce 1 more enemy");
		return d;
	}
	@Override
	public void onMagic(DungeonsPlayer d,ProjectileParticle p) 
	{
		if (d.inCrypt)
		{
			p.damage = (int) (p.damage * 1.5);
			p.speed = p.speed * 1.2;
			p.pierces++;
		}
	}

}
