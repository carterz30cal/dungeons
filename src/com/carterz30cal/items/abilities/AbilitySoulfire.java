package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.IndicatorTask;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.ListenerEntityDamage;
import com.carterz30cal.utility.Mafs;
import com.carterz30cal.utility.ParticleFunctions;
import com.carterz30cal.utility.RandomFunctions;

import org.bukkit.ChatColor;

public class AbilitySoulfire extends AbsAbility
{
	public static Map<DungeonsPlayer,List<Soulfire>> orbs = new HashMap<>();
	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Soulfire");
		d.add(ChatColor.GOLD + "Right click to use");
		d.add("Summon an orb of soulfire");
		d.add("It will attack a nearby enemy");
		d.add("dealing damage that scales");
		d.add("based on your maximum mana");
		d.add(ChatColor.BLUE + "Consumes 160 mana");
		d.add(ChatColor.BLUE + "or 75 in a crypt");
		return d;
	}
	public void onTick  (DungeonsPlayer d) 
	{
		if (!orbs.containsKey(d)) return;
		
		List<DMob> potential = new ArrayList<>();
		for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(), 6, 6, 6))
		{
			DMob m = DMobManager.get(e);
			if (m != null && !potential.contains(m)) potential.add(m);
		}
		for (Soulfire orb : orbs.get(d))
		{
			if (potential.size() > 0 && orb.target == null) 
			{
				orb.target = potential.get(0);
				potential.remove(0);
			}
			orb.show();
		}
		orbs.get(d).removeIf((Soulfire o) -> o.hit > 3);
	}
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
		
		int cost = 160;
		if (d.inCrypt) cost = 75;
		if (!d.useMana(cost)) return false;
		orbs.putIfAbsent(d, new ArrayList<>());
		
		Soulfire orb = new Soulfire();
		orb.location = d.player.getLocation().add(0,1,0);
		orb.owner = d;
		orb.heightOffset = RandomFunctions.random(0.3, 1.1);
		orb.radius = RandomFunctions.random(1.2d, 3d);
		orbs.get(d).add(orb);
		
		return false;
	}
}

class Soulfire
{
	// active information
	public Location location;
	public DMob target;
	
	// idle information
	public double rotation;
	public double heightOffset;
	public double radius;
	
	public DungeonsPlayer owner;
	public int hit;
	
	public void show()
	{
		if (target == null)
		{
			rotation += 0.25;
			if (rotation >= 360) rotation = 0;
			location = owner.player.getLocation().add(Mafs.getCircleX(rotation, radius),heightOffset,Mafs.getCircleZ(rotation, radius));
		}
		else
		{
			if (target.health < 1) 
			{
				target = null;
				return;
			}
			
			if (target.entities.get(0).getLocation().distance(location) <= 1) 
			{
				int damage = owner.stats.damage * (owner.stats.mana / 235);
				target.damage(damage, owner.player);
				
				Location hitloc = RandomFunctions.offset(target.entities.get(0).getLocation(), 0.6).add(0,1,0);
				ArmorStand h = DMobManager.hit(target.entities.get(0), damage,ChatColor.AQUA);
				
				IndicatorTask t = new IndicatorTask(h,hitloc);
				t.runTaskTimer(Dungeons.instance, 1,15);
				ListenerEntityDamage.indicators.add(t);
				
				hit++;
				target = null;
			}
			Vector t = target.entities.get(0).getLocation().add(0,0.6,0).subtract(location).toVector().normalize().multiply(0.5);
			location.add(t);
		}
		ParticleFunctions.stationary(location, Particle.REDSTONE, 2);
	}
}
