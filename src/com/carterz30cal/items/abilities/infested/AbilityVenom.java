package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

import net.md_5.bungee.api.ChatColor;

public class AbilityVenom extends AbsAbility 
{
	public static List<DMob> affected = new ArrayList<>();
	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Deadly Venom");
		d.add("Deals 45 true damage per second");
		d.add("for the next 10 seconds. Reduce");
		d.add("the enemy's next heal by 55%");
		d.add(ChatColor.RED + "Doesn't stack.");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		dMob.nextHealMultiplier *= 0.45;
		if (affected.contains(dMob)) return damage;
		new BukkitRunnable()
		{
			int count = 0;
			@Override
			public void run() {
				if (count == 10 || (count > 0 && dMob.health < 1)) 
				{
					affected.remove(dMob);
					cancel();
				}
				int p = 7;
				while (p > 0)
				{
					Dungeons.w.spawnParticle(Particle.REDSTONE, 
							dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.2, 0.2), 
									RandomFunctions.random(0d, dMob.entities.get(0).getHeight()),
									RandomFunctions.random(-0.2, 0.2)), 3
							,new Particle.DustOptions(Color.LIME,0.9f));
					p--;
				}
				dMob.damage(45, d, DamageType.TRUE, false);
				count++;
			}
			
		}.runTaskTimer(Dungeons.instance, 20, 20);
		affected.add(dMob);
		return damage;
	}
}
