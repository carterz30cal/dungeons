package com.carterz30cal.items.abilities.ruins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.StdUtils;

import net.md_5.bungee.api.ChatColor;

public class AbilityAbyssalBow extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Distort Reality");
		d.add("If an arrow lands on an enemy, deal magic damage.");
		d.add("If the arrow lands on the ground, teleport to the location");
		d.add("and consume all mana. All nearby enemies will be drained");
		d.add("of their health and slowed for 10 seconds.");
		return d;
	}
	
	public int onArrowLand(DungeonsPlayer d,DMob mob,int damage) 
	{ 
		mob.damage(damage, d, DamageType.MAGIC, true);
		return 0;
	}
	
	@Override
	public void arrowOnGround(DungeonsPlayer d,Entity arrow) 
	{
		if (d.getManaPercent() >= 0.99)
		{
			Location l = d.player.getLocation();
			d.useMana(d.getMana());
			Location tp = arrow.getLocation();
			tp.setYaw(l.getYaw());
			tp.setPitch(l.getPitch());
			d.player.teleport(tp);
			d.player.playSound(arrow.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
			StdUtils.drawLaser(l, arrow.getLocation(), 300, 1.7f, Color.PURPLE);
			
			Set<DMob> mobs = new HashSet<>();
			for (Entity e : Dungeons.w.getNearbyEntities(tp, 7, 7, 7)) if (DMobManager.get(e) != null) mobs.add(DMobManager.get(e));
			
			int health = d.stats.health - d.getHealth();
			if (mobs.size() == 0) return;
			health = health / mobs.size();
			int heal = 0;
			for (DMob m : mobs) 
			{
				if (m.health < health) heal += m.health;
				else heal += health;
				
				m.damage(health, d, DamageType.MAGIC, false);
				((LivingEntity)m.entities.get(0)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,0,true));
				StdUtils.drawLaser(tp, m.entities.get(0).getLocation(), 200, 1.5f, Color.PURPLE);
			}
			
			d.heal(heal);
			d.player.sendMessage(ChatColor.GREEN + "You siphoned " + heal + " health from nearby enemies!");
		}
		
		
	};

}
