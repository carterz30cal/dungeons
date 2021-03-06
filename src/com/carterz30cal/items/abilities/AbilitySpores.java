package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.IndicatorTask;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.ListenerEntityDamage;
import com.carterz30cal.utility.RandomFunctions;

public class AbilitySpores extends AbsAbility
{
	public static final Map<DungeonsPlayer,Integer> spores = new HashMap<>();
	public static final Map<DungeonsPlayer,Integer> timers = new HashMap<>();
	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Spores");
		d.add("Adds 1 spore around you (stacks)");
		d.add("These spores will attack nearby enemies");
		d.add("for you, dealing 8% of your damage.");
		d.add(ChatColor.RED + "You don't get XP for spore kills");
		return d;
	}
	
	@Override
	public void onTick  (DungeonsPlayer d) 
	{
		int time = timers.getOrDefault(d, 0);
		timers.put(d, ++time);
		if (time < 20) return;
		timers.put(d, 0);
		
		//int sporec = spores.getOrDefault(d,0);
		
		int maxSpores = 0;
		for (AbsAbility a : d.stats.abilities) if (a.getClass() == AbilitySpores.class) maxSpores++;
		
		for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(), 5, 5, 5))
		{
			if (maxSpores == 0) break;
			DMob m = DMobManager.get(e);
			if (m != null)
			{
				Location ml = m.entities.get(0).getLocation();
				Dungeons.w.spawnParticle(Particle.CRIMSON_SPORE,ml, 5,0.2,0.2,0.2,0.0);
				double bonusxp = d.stats.miningXp;
				d.stats.miningXp = 0;
				m.damage((int) (d.stats.damage * 0.08), d.player);
				d.stats.miningXp = bonusxp;
				
				Location hitloc = RandomFunctions.offset(ml, 0.5).add(0,1,0);
				ArmorStand h = DMobManager.hit(m.entities.get(0), (int) (d.stats.damage * 0.08),ChatColor.LIGHT_PURPLE);
				
				IndicatorTask t = new IndicatorTask(h,hitloc);
				t.runTaskTimer(Dungeons.instance, 1,15);
				ListenerEntityDamage.indicators.add(t);
				maxSpores--;
			}
		}
		//spores.put(d, sporec);
	} 
	
	@Override
	public boolean isUnique()
	{
		return true;
	}

}
