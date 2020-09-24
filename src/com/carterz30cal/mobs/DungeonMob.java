package com.carterz30cal.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
	public boolean summon;
	
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
		
		if (damager != null) type.onKilled(damager,modifier);
		if (summon) DungeonMobCreator.summons--;
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
