package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.util.Vector;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class AbilityDiggingSight extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Detector");
		d.add("Shows the way to the next digging spot");
		d.add("+25% damage to ancient mobs");
		return d;
	}
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		if (dMob.type.tags.contains("ancient")) return (int) (damage * 1.25);
		return damage;
	} 
	public void onTick  (DungeonsPlayer d) 
	{
		Location b = AbilityDigging.targets.get(d);
		if (b == null) 
		{
			b = AbilityDigging.site();
			AbilityDigging.targets.put(d, b);
		}
		if (b.distance(d.player.getLocation()) < 30)
		{
			d.player.spawnParticle(Particle.REDSTONE, AbilityDigging.s(b), 10, 0.25, 0, 0.25,0,new DustOptions (Color.ORANGE,0.8f));
			
			if (RandomFunctions.random(1, 10) != 5) return;
			Location p = d.player.getEyeLocation().subtract(0, 0.6, 0);
			p.add(d.player.getLocation().getDirection().normalize());
			
			Vector v = AbilityDigging.s(b).subtract(p).toVector().normalize().multiply(0.75);
			while (p.distance(AbilityDigging.s(b)) > 1)
			{
				p = p.add(v);
				d.player.spawnParticle(Particle.REDSTONE, p, 3, 0, 0, 0,0,new DustOptions (Color.WHITE,0.6f));
			}
		}
	}
}
