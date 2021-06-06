package com.carterz30cal.mobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.areas.AbsDungeonEvent;
import com.carterz30cal.areas.EventTicker;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.IndicatorTask;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.abilities.DMobAbility;
import com.carterz30cal.mobs.packet.EntitySkinned;
import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.ListenerEntityDamage;
import com.carterz30cal.player.skilltree.AbsSkill;
import com.carterz30cal.quests.TutorialManager;
import com.carterz30cal.quests.TutorialTrigger;
import com.carterz30cal.tasks.TaskArmourstand;
import com.carterz30cal.tasks.TaskMobAbilities;
import com.carterz30cal.tasks.TaskSetEquipment;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.RandomFunctions;
import com.carterz30cal.utility.Stats;
import com.carterz30cal.utility.StringManipulator;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

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
	
	public double nextHealMultiplier;
	public Set<String> effects = new HashSet<>();
	
	private UUID ident;
	private Random r;
	public int coins()
	{
		return (type.health / 25) + modifier.coins;
	}
	public long xp()
	{
		return CharacterSkill.requirement(type.level) / 50;
	}
	
	public int health()
	{
		return (int)(type.health * modifier.health);
	}
	
	public String update()
	{
		String nom = CharacterSkill.prettyText(type.level) + " " + type.name + " " + ChatColor.RED + health + "❤";
		if (type.level == 0) nom = type.name + " " + ChatColor.RED + health + "❤";
		if (modifier != DMobModifier.base) nom = modifier.text + modifier.name + nom;
		if (display != null) display.setCustomName(nom);
		
		return nom;
	}
	
	public void setStatic()
	{
		for (Entity e : entities) 
		{
			((LivingEntity)e).setAI(false);
		}
	}
	public void setInvulnerable()
	{
		for (Entity e : entities) 
		{
			((LivingEntity)e).setInvulnerable(true);
		}
	}
	public void setVulnerable()
	{
		for (Entity e : entities) 
		{
			((LivingEntity)e).setInvulnerable(false);
		}
	}
	public void setMoving()
	{
		for (Entity e : entities) 
		{
			((LivingEntity)e).setAI(true);
		}
	}
	
	
	
	public void remove()
	{
		health = -1;
		for (Entity e : entities) 
		{
			if (e instanceof Player) ((EntitySkinned)((CraftPlayer)((Player)e)).getHandle()).remove();
			else e.remove();
		}
		if (display != null) display.remove();
		
		for (DMobAbility a : type.abilities) 
		{
			a.killed(this);
			a.mobs.remove(this);
		}
	}
	public void destroy(Player damager)
	{
		if (display == null) return;
		for (Entity e : entities) 
		{
			if (e instanceof Player) ((EntitySkinned)((CraftPlayer)((Player)e)).getHandle()).kill();
			else ((LivingEntity)e).setHealth(0);
		}
		
		DMobManager.mobs.remove(ident);
		display.remove();
		display = null;
		
		health = 0;
		
		for (DMobAbility a : type.abilities) 
		{
			a.killed(this);
			a.mobs.remove(this);
		}
		Stats.mobskilled++;
		if (owner != null) owner.mob = null;
		if (damager != null) 
		{
			DungeonsPlayer d = DungeonsPlayerManager.i.get(damager);
			if (!type.id.equals("tutorial_dummy")) d.kills++;
			if (d.kills >= 2500 && !d.questProgress.containsKey("kills_2500")) 
			{
				// give sir grindalot
				InventoryHandler.addItem(d, ItemBuilder.i.build("sword_sirgrindalot", "midastouch,7", 1), false);
				d.player.sendMessage(ChatColor.GOLD + "Kill milestone of 2500! " + ChatColor.WHITE + "Obtained Sir Grindalot!");
				d.questProgress.put("kills_2500", "done");
			}
			else if (d.kills >= 10000 && !d.questProgress.containsKey("kills_10000")) 
			{
				// give sir grindalot
				InventoryHandler.addItem(d, ItemBuilder.i.build("armour_grindalot_boots", 1), false);
				d.player.sendMessage(ChatColor.GOLD + "Kill milestone of 10000! " + ChatColor.WHITE + "Obtained Grindalot's Boots!");
				d.questProgress.put("kills_10000", "done");
			}
			TutorialManager.fireEvent(d, TutorialTrigger.KILL_ENEMY, type.id);
			for (AbsEnchant e : d.stats.ench) e.onKill(d, this);
			rewards(damager);
		}
	}
	
	private void rewards(Player damager)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(damager);

		if (type.level != 0) d.level.give(xp());
		
		for (AbsAbility a : d.stats.abilities) a.onKill(d,type);
		int coinreward = coins() + d.stats.bonuskillcoins;
		if (modifier != null) coinreward += 5;
		d.coins += (int)((double)coinreward*type.coinmulti);
		d.explorer.add(d.area.id,1);
		if (type.drops == null) return;
		for (MobDrop drop : type.drops)
		{
			if (r.nextDouble() <= drop.chance)
			{
				ItemStack item;
				if (drop.enchants != null) item = ItemBuilder.i.build(drop.item, null,drop.enchants);
				else item = ItemBuilder.i.build(drop.item, null);
				item.setAmount(r.nextInt((drop.maxAmount+1)-drop.minAmount)+drop.minAmount);
				
				new SoundTask(damager.getLocation(),damager,Sound.ENTITY_ITEM_PICKUP,0.1f,1).runTaskLater(Dungeons.instance, 3);
				if (drop.chance*100 < 2.5 || drop.rareMessage) 
				{
					if (drop.chance*100 < 0.1)
					{
						damager.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "INCREDIBLE DROP! " + ChatColor.RESET + item.getItemMeta().getDisplayName());
					}
					else damager.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "RARE DROP! " + ChatColor.RESET + item.getItemMeta().getDisplayName());
					new SoundTask(damager.getLocation(),damager,Sound.BLOCK_NOTE_BLOCK_PLING,0.5f,0.8f).runTaskLater(Dungeons.instance, 1);
					new SoundTask(damager.getLocation(),damager,Sound.BLOCK_NOTE_BLOCK_PLING,0.5f,0.9f).runTaskLater(Dungeons.instance, 6);
					new SoundTask(damager.getLocation(),damager,Sound.BLOCK_NOTE_BLOCK_PLING,0.5f,1.1f).runTaskLater(Dungeons.instance, 11);
					InventoryHandler.addItem(d, item);
				}
				else InventoryHandler.addItem(d, item,true);
				
				TutorialManager.fireEvent(d, TutorialTrigger.DROP, drop.item);
			}
		}
	}
	
	public void spawnIndicator(DungeonsPlayer damager,String message)
	{
		Entity e = entities.get(0);
		Location hitLocation = e.getLocation().subtract(e.getLocation().subtract(damager.player.getLocation()).multiply(0.3))
				.add(0,1.25,0);
		hitLocation = hitLocation.add(RandomFunctions.random(-0.5d, 0.5d),RandomFunctions.random(-0.5d, 0.5d),RandomFunctions.random(-0.5d, 0.5d));
		ArmorStand h = DMobManager.hit(e, (int)0,ChatColor.BLACK);
		h.setCustomName(message);
		
		IndicatorTask t = new IndicatorTask(h,hitLocation);
		t.runTaskTimer(Dungeons.instance, 1,20);
		ListenerEntityDamage.indicators.add(t);
	}
	public void damage(int damage,DungeonsPlayer damager,DamageType dtype)
	{
		damage(damage,damager,dtype,true);
	}
	public void damage(int damage,DungeonsPlayer damager,DamageType dtype,boolean activateabs)
	{
		if (entities.get(0).isInvulnerable()) return;
		int damageFinal = 0;
		ChatColor indicatorColour = ChatColor.WHITE;
		if (damager.stats.abilities != null && damager != null && activateabs) for (AbsAbility a : damager.stats.abilities) damage = a.onAttack(damager, this, damage);
		if (damager != null && activateabs) for (String s : damager.skills.keySet()) damage = AbsSkill.skills.get(s).onAttack(damager.skills.get(s), damager, damage);
		for (DMobAbility mab : type.abilities) damage = mab.damaged(this, damager,damage);
		damageFinal = damage;
		if (dtype == DamageType.TRUE) 
		{
			indicatorColour = ChatColor.GOLD;
		}
		else if (dtype == DamageType.PERCENT) damageFinal = (int) (((double)damage/100) * health);
		else if (dtype == DamageType.MAGIC)
		{
			damageFinal *= 1-type.dmgresist;
			indicatorColour = ChatColor.AQUA;
		}
		else
		{
			damageFinal -= type.armour;
			damageFinal *= 1-type.dmgresist;
		}
		
		List<PacketPlayOutEntityStatus> hurts = new ArrayList<>();
		for (Entity e : entities) hurts.add(new PacketPlayOutEntityStatus (((CraftEntity)e).getHandle(),(byte)2));
		for (Player k : Bukkit.getOnlinePlayers())
		{
			CraftPlayer pk = (CraftPlayer)k;
			for (PacketPlayOutEntityStatus pckt : hurts) pk.getHandle().playerConnection.sendPacket(pckt);
		}
		damageFinal = Math.max(0,damageFinal);
		
		if (damageFinal > 0)
		{
			Entity e = entities.get(0);
			Location hitLocation = e.getLocation().subtract(e.getLocation().subtract(damager.player.getLocation()).multiply(0.3))
					.add(0,1.25,0);
			hitLocation = hitLocation.add(RandomFunctions.random(-0.5d, 0.5d),RandomFunctions.random(-0.75d, 0.25d),RandomFunctions.random(-0.5d, 0.5d));
			ArmorStand h = DMobManager.hit(e, (int)damageFinal,indicatorColour);
			
			IndicatorTask t = new IndicatorTask(h,hitLocation);
			t.runTaskTimer(Dungeons.instance, 1,20);
			ListenerEntityDamage.indicators.add(t);
		}
		
		
		lastDamage = damageFinal;
		health -= damageFinal;
		health = Math.max(0, health);
		
		
		
		if (health > 0) update();
		else destroy(damager.player);
	}
	
	
	
	public void heal(int healing)
	{
		health = (int) Math.min(health+Math.max(0, healing * nextHealMultiplier), health());
		nextHealMultiplier = 1;
		update();
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
		if (entities.get(0).isInvulnerable()) return;
		if (trueDamage)
		{
			health -= damage;
			lastDamage = damage;
		}
		else
		{
			int d = damage - type.armour;
			d = Math.max(0, d);
			d = (int)Math.round(d*(1-type.dmgresist));
			if (modifier != null) d = (int)(d * (1-modifier.dmgresist));
			if (abilities != null && damager != null) for (AbsAbility a : abilities) d = a.onAttack(DungeonsPlayerManager.i.get(damager), this, d);
			d = Math.max(0, d);
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
		
		this.type = type;
		modifier = DMobModifier.base;
		health = health();
		new TaskArmourstand(this);
		for (int j = 0; j < type.entities.size();j++)
		{
			EntityType t = type.entities.get(j);
			
			LivingEntity entity;
			if (type instanceof SkinnedType) entity = EntitySkinned.createNPC(Dungeons.w, position,this).getBukkitEntity().getPlayer();
			else if (type instanceof CustomType)
			{
				entity = (LivingEntity) ((CustomType) type).customTypes.get(j).spawnEntity(position);
			}
			else entity = (LivingEntity)position.getWorld().spawnEntity(position, t);
			
			if (j == 0) ident = entity.getUniqueId();
			entity.getPersistentDataContainer().set(identifier, PersistentDataType.STRING, ident.toString());
			if (!(type instanceof SkinnedType))
			{
				entity.setRemoveWhenFarAway(false);
				entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(new AttributeModifier("walkspeed",type.speed-1,Operation.MULTIPLY_SCALAR_1));
				if (entity.getVehicle() != null) entity.getVehicle().remove();
				for (Entity pas : entity.getPassengers()) pas.remove();
				if (entity instanceof Ageable)
				{
					if (StringManipulator.contains(type.entityData.get(j),"baby")) ((Ageable)entity).setBaby();
					else ((Ageable)entity).setAdult();
				}
				if (entity instanceof ArmorStand)
				{
					if (StringManipulator.contains(type.entityData.get(j),"small")) ((ArmorStand)entity).setSmall(true);
					if (StringManipulator.contains(type.entityData.get(j),"invisible")) ((ArmorStand)entity).setVisible(false);
					if (StringManipulator.contains(type.entityData.get(j),"offset"))
					{
						entity.setGravity(false);
						entity.teleport(entity.getLocation().subtract(0, entity.getHeight() - 0.3, 0));
					}
				}
				
				if (entity instanceof Slime)
				{
					((Slime)entity).setSize(Integer.parseInt(StringManipulator.get(type.entityData.get(j),"size")));
				}
				for (PotionEffect potion : entity.getActivePotionEffects()) entity.removePotionEffect(potion.getType());
				if (type.invisible || StringManipulator.contains(type.entityData.get(j),"invisible")) entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1000000,0,true));
				entity.setSilent(type.silent);
				entity.getEquipment().clear();
			}
			
			entities.add(entity);
			if (j > 0) entities.get(j-1).addPassenger(entity);
			
			
		}
		display = (ArmorStand)position.getWorld().spawnEntity(entities.get(entities.size()-1).getLocation(), EntityType.ARMOR_STAND);
		
		display.setVisible(false);
		display.setSmall(true);
		display.setMarker(true);
		display.setCustomNameVisible(true);
		display.setGravity(false);
		
		if (o != null) 
		{
			o.mob = this;
			owner = o;
		}
		
		update();
		
		new TaskSetEquipment(this);
		
		if (type.abilities.size() > 0) 
		{
			new TaskMobAbilities(this);
			for (DMobAbility a : type.abilities) a.add(this);
		}

		DMobManager.mobs.put(ident,this);
	}
}
