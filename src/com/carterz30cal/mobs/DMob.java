package com.carterz30cal.mobs;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import com.carterz30cal.areas.AbsDungeonEvent;
import com.carterz30cal.areas.EventTicker;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.tasks.TaskArmourstand;
import com.carterz30cal.tasks.TaskSetEquipment;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.StringManipulator;

public class DMob
{
	public static final NamespacedKey identifier = new NamespacedKey(Dungeons.instance,"identifier");
	public ArrayList<Entity> entities;
	public ArmorStand display;
	
	public DMobType type;
	public DMobModifier modifier;
	public SpawnPosition owner;
	
	public int health;
	public int lastDamage;
	
	private UUID ident;
	private Random r;
	public int coins()
	{
		return (type.health / 25) + modifier.coins;
	}
	public int xp()
	{
		double bonus = 1;
		if (modifier != DMobModifier.base) bonus += 0.35;
		return (int)(((type.health / 50) + (type.damage / 25)) * bonus);
	}
	
	public int health()
	{
		return (int)(type.health * modifier.health);
	}
	
	public void update()
	{
		String nom = type.name + " " + ChatColor.RED + health + "/" + health();
		if (modifier != DMobModifier.base) nom = modifier.text + modifier.name + nom;
		display.setCustomName(nom);
	}
	public void remove()
	{
		for (Entity e : entities) e.remove();
		display.remove();
	}
	public void destroy(Player damager)
	{
		for (Entity e : entities) ((LivingEntity)e).setHealth(0);
		
		DMobManager.mobs.remove(ident);
		display.remove();
		
		if (owner != null) owner.mob = null;
		if (damager != null) rewards(damager);
	}
	
	private void rewards(Player damager)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(damager);
		int pl = d.perks.getLevel(type.perk);
		d.perks.add(type.perk);
		if (d.perks.getLevel(type.perk) > pl)
		{
			new SoundTask(damager.getLocation(),damager,Sound.BLOCK_GLASS_BREAK,2f,1).runTaskLater(Dungeons.instance,20);
		}
		if (d.skills.add("combat", xp())) d.skills.sendLevelMessage("combat", damager);
		for (AbsAbility a : d.stats.abilities) a.onKill(d,type);
		int coinreward = coins() + d.stats.bonuskillcoins;
		if (modifier != null) coinreward += 5;
		d.coins += coinreward;
		d.explorer.add(d.area.id,1);
		if (type.drops == null) return;
		for (MobDrop drop : type.drops)
		{
			if (r.nextDouble() <= drop.chance)
			{
				ItemStack item;
				if (drop.enchants != null) item = ItemBuilder.i.build(drop.item, d,drop.enchants);
				else item = ItemBuilder.i.build(drop.item, d);
				item.setAmount(r.nextInt((drop.maxAmount+1)-drop.minAmount)+drop.minAmount);
				
				new SoundTask(damager.getLocation(),damager,Sound.ENTITY_ITEM_PICKUP,1,1).runTaskLater(Dungeons.instance, 3);
				if (drop.chance*100 < 2.5) 
				{
					damager.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "RARE DROP! " + ChatColor.RESET + item.getItemMeta().getDisplayName());
					InventoryHandler.addItem(d, item);
				}
				else InventoryHandler.addItem(d, item,true);
			}
		}
	}
	
	
	public void damage(int damage)
	{
		damage(damage,null,false,null);
	}
	public void damage(int damage,Player damager)
	{
		damage(damage,damager,false,null);
	}
	public void damage(int damage,Player damager,boolean trueDamage)
	{
		damage(damage,damager,trueDamage,null);
	}
	public void damage(int damage,Player damager,ArrayList<AbsAbility> abilities)
	{
		damage(damage,damager,false,abilities);
	}
	public void damage(int damage,Player damager,boolean trueDamage,ArrayList<AbsAbility> abilities)
	{
		if (trueDamage)
		{
			health = -damage;
			lastDamage = damage;
		}
		else
		{
			int d = damage;
			d = (int)Math.round(d*(1-type.dmgresist));
			if (modifier != null) d = (int)(d * (1-modifier.dmgresist));
			if (abilities != null && damager != null) for (AbsAbility a : abilities) d = a.onAttack(DungeonsPlayerManager.i.get(damager), this, d);
			health -= d;
			lastDamage = d;
		}
		
		if (health > 0) update();
		else destroy(damager);
	}
	
	public DMob (DMobType type,SpawnPosition o,Location position,boolean modifiers)
	{
		if (type == null) return;
		if (o != null && o.mob != null) return;
		
		r = new Random();
		for (AbsDungeonEvent e : EventTicker.events) 
		{
			DMobType n = e.eventPreSpawn(DungeonManager.i.hash(position.getBlockZ()), type);
			if (n != null) 
			{
				type = n;
				break;
			}
		}
		
		entities = new ArrayList<Entity>();
		ident = null;
		for (int j = 0; j < type.entities.size();j++)
		{
			EntityType t = type.entities.get(j);
			LivingEntity entity = (LivingEntity)position.getWorld().spawnEntity(position, t);
			if (j == 0) ident = entity.getUniqueId();
			entity.getPersistentDataContainer().set(identifier, PersistentDataType.STRING, ident.toString());
			entity.setRemoveWhenFarAway(false);
			
			entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(new AttributeModifier("walkspeed",type.speed-1,Operation.MULTIPLY_SCALAR_1));
			if (entity.getVehicle() != null) entity.getVehicle().remove();
			if (entity instanceof Ageable)
			{
				if (StringManipulator.contains(type.entityData.get(j),"baby")) ((Ageable)entity).setBaby();
				else ((Ageable)entity).setAdult();
			}
			if (entity instanceof Slime)
			{
				((Slime)entity).setSize(Integer.parseInt(StringManipulator.get(type.entityData.get(j),"size")));
			}
			for (PotionEffect potion : entity.getActivePotionEffects()) entity.removePotionEffect(potion.getType());
			entities.add(entity);
			if (j > 0) entities.get(j-1).addPassenger(entity);
			
			entity.getEquipment().clear();
		}
		display = (ArmorStand)position.getWorld().spawnEntity(position, EntityType.ARMOR_STAND);
		display.setVisible(false);
		display.setMarker(true);
		display.setCustomNameVisible(true);
		display.setGravity(false);
		if (o != null) 
		{
			o.mob = this;
			owner = o;
		}
		this.type = type;
		modifier = DMobModifier.base;
		health = health();
		update();
		new TaskArmourstand(this);
		new TaskSetEquipment(this);
		/*
		MobModifier mod = null;
		if (Math.random() <= 0.1 && !type.boss) mod = modifiers.get((int)Math.round(Math.random()*(modifiers.size()-1)));
		DungeonMob n;
		if (type.type == EntityType.ELDER_GUARDIAN) n = new MobElderGuardian(type,entity,silverfish,armourstand,pos,mod);
		else n = new DungeonMob(type,entity,silverfish,armourstand,pos,mod);
		*/
		DMobManager.mobs.put(ident,this);
	}
}
