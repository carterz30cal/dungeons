package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;


public class AbilityReaperChestplate extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Container of Souls");
		d.add("You can have 3 more souls alive.");
		d.add("Your souls will each take 5%");
		d.add("of the damage that is");
		d.add("inflicted upon you.");
		return d;
	}
	
	@Override
	public void stats(DungeonsPlayerStats s)
	{
		s.maxsouls += 3;
	}

	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{ 
		if (mob == null || c != DamageCause.ENTITY_ATTACK) return 1;
		int taken = 0;
		for (Soul soul : AbilityReaperBlade.souls.getOrDefault(d, new ArrayList<Soul>()))
		{
			if (soul.summoned)
			{
				taken++;
				soul.health -= (int)dealt*0.05d;
			}
		}
		return Math.max(0, 1 - (0.05*taken));
	}
}
