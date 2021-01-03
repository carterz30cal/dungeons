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

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerManager;

@Deprecated
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
	
	private int lastDamage;
	
	public DungeonMob (DungeonMobType t,Damageable e,Entity s,Entity a,SpawnPosition o,MobModifier m)
	{
		type = t;
		
		entity = e;
		silverfish = s;
		armourstand = a;
		modifier = m;
		owner = o;
		//owner.mob = this;
		tagged = new ArrayList<Player>();
		health = health();
		summonCaps = new HashMap<String,Integer>();
		mobs.put(entity.getUniqueId(), this);
		
		name();
	}
	public void removeFrom()
	{
		if (noremove) return;
		
		mobs.remove(entity.getUniqueId());
		if (silverfish != null) silverfi.remove(silverfish.getUniqueId());
		arm.remove(armourstand.getUniqueId());
	}
	public void destroy(Player damager)
	{
		health = 0;
		if (owner != null) owner.mob = null;

		if (noremove) entity.remove();
		else entity.setHealth(0);
		if (silverfish != null) silverfish.remove();
		armourstand.remove();
		
		removeFrom();
		
		if (damager != null) type.onKilled(damager,modifier);
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
	
	public int getLastDamage()
	{
		return lastDamage;
	}
	public void damageTrue(int damage,Player damager)
	{
		health -= damage;
		lastDamage = damage;
		if (health > 0) 
		{
			name();
			if (((float)health) / health() <= 0.3) ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,40,0,false,false));
		}
		else destroy(damager);
	}
	public void damage(int damage,Player damager)
	{
		damage(damage,damager,null);
	}
	public void damage(int damage,Player damager,ArrayList<AbsAbility> abilities)
	{
		/*
		int d = damage;
		d = (int)Math.round((d-type.armour)*(1-type.damageResist));
		if (modifier != null) d = (int)(d * (1-modifier.damageReduction));
		if (abilities != null && damager != null) for (AbsAbility a : abilities) d = a.onAttack(DungeonsPlayerManager.i.get(damager), this, d);
		health -= d;
		lastDamage = d;
		if (health > 0) 
		{
			name();
			if (((float)health) / health() <= 0.3) ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,40,0,false,false));
		}
		else destroy(damager);
		*/
	}
	
	public static DungeonMob getMob(UUID mob)
	{
		DungeonMob m = mobs.get(mob);
		if (m == null) m = silverfi.get(mob);
		if (m == null) m = arm.get(mob);
		return m;
	}
}
