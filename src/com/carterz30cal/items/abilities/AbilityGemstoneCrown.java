package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityGemstoneCrown extends AbsAbility
{
	public static final Map<DungeonsPlayer,Integer> timers = new HashMap<>();
	public static final Map<DungeonsPlayer,Boolean> enabled = new HashMap<>();
	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Gem Focus");
		d.add("Fire a laser at the closest enemy consuming");
		d.add("10% of your current mana");
		d.add("Deal triple that as damage");
		d.add(ChatColor.RED + "Crouch to toggle");
		d.add(ChatColor.RED + "Deactivates if out of mana");
		return d;
	}
	@Override
	public void onSneak(DungeonsPlayer d)
	{
		enabled.put(d, !enabled.getOrDefault(d, true));
	}
	@Override
	public void onTick  (DungeonsPlayer d) 
	{
		if (d.getMana() < 15) enabled.put(d, false);
		if (!d.playerHasMana() || d.getMana() < 15 || !enabled.getOrDefault(d, true)) return;
		int time = timers.getOrDefault(d, 0);
		timers.put(d, ++time);
		if (time < 10) return;
		timers.put(d, 0);
		
		DMob closest = null;
		for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(), 5, 5, 5))
		{
			
			DMob m = DMobManager.get(e);
			if (m != null)
			{
				if (closest == null) closest = m;
				else if (closest.entities.get(0).getLocation().distance(d.player.getLocation())
						> m.entities.get(0).getLocation().distance(d.player.getLocation())) closest = m;
			}
		}
		if (closest == null) return;
		
		int damage = (int) (d.getMana() * 0.2);
		d.useMana(damage);
		
		closest.damage((int) (damage * 2.5), d, DamageType.MAGIC);
		
		Location laser = d.player.getEyeLocation();
		Location l = ((LivingEntity)closest.entities.get(0)).getEyeLocation();
		double dx = (l.getX() - laser.getX()) / 50;
		double dy = (l.getY() - laser.getY()) / 50;
		double dz = (l.getZ() - laser.getZ()) / 50;
		for (int x = 0; x < 50; x++) 
		{
			laser.add(dx, dy, dz);
			Dungeons.w.spawnParticle(Particle.REDSTONE, laser, 2, 0d, 0d, 0d, new DustOptions(Color.BLUE,0.5f));
		}
	} 
}
