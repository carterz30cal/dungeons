package com.carterz30cal.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.tasks.TaskCombatTag;

public class DungeonMob
{
	public static boolean noremove = false;
	public static HashMap<UUID,DungeonMob> mobs;
	public static HashMap<UUID,DungeonMob> silverfi;
	public static HashMap<UUID,DungeonMob> arm;
	public Damageable entity;
	
	public int health;
	public MobModifier modifier;
	
	public Entity silverfish;
	public Entity armourstand;
	
	public SpawnPosition owner;
	public DungeonMobType type;
	
	public ArrayList<Player> tagged;
	public HashMap<String,Integer> summonCaps;
	
	public DungeonMob (DungeonMobType t,Damageable e,Entity s,Entity a,SpawnPosition o,MobModifier m)
	{
		type = t;
		
		entity = e;
		silverfish = s;
		armourstand = a;
		modifier = m;
		owner = o;
		owner.mob = this;
		tagged = new ArrayList<Player>();
		health = health();
		summonCaps = new HashMap<String,Integer>();
		mobs.put(entity.getUniqueId(), this);
		
		name();
	}
	public void destroy(Player damager)
	{
		if (owner != null) owner.mob = null;
		if (!noremove)
		{
			mobs.remove(entity.getUniqueId());
			silverfi.remove(silverfish.getUniqueId());
			arm.remove(armourstand.getUniqueId());
		}
		if (noremove) entity.remove();
		else entity.setHealth(0);
		silverfish.remove();
		armourstand.remove();
		
		if (damager != null) 
		{
			type.onKilled(damager,modifier);
			//new SoundTask(damager.getLocation(),damager,Sound.ENTITY_ZOMBIE_DEATH,0.2f,0.65f).runTask(Dungeons.instance);
		}
	}
	public void heal(int amount)
	{
		health = Math.min(health(), health + amount);
		name();
	}
	public int health()
	{
		if (modifier == null) return type.maxHealth;
		else return (int)Math.round(type.maxHealth * (1.0+modifier.health));
	}
	public void name()
	{
		if (modifier == null) armourstand.setCustomName(type.name + " " + ChatColor.RED + health + "/" + health());
		else armourstand.setCustomName(modifier.colour + modifier.name + " " + type.name + " " + ChatColor.RED + health + "/" + health());
	}
	public void damage(int damage,Player damager)
	{
		if (modifier == null) health -= (damage-type.armour);
		else health -= (damage-type.armour)*(1-modifier.damageReduction);
		if (health > 0) 
		{
			name();
			if (type.actions != null && !tagged.contains(damager)) 
			{
				new TaskCombatTag(damager,this).runTaskTimer(Dungeons.instance, 20,20);
				tagged.add(damager);
			}
			//new SoundTask(damager.getLocation(),damager,Sound.ENTITY_ZOMBIE_HURT,0.2f,((float)health*1.5f)/health()).runTask(Dungeons.instance);
			if (((float)health) / health() <= 0.3) ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,40,0,false,false));
		}
		else destroy(damager);
	}
	
	public static DungeonMob getMob(UUID mob)
	{
		DungeonMob m = mobs.get(mob);
		if (m == null) m = silverfi.get(mob);
		if (m == null) m = arm.get(mob);
		return m;
	}
}
