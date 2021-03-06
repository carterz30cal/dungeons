package com.carterz30cal.player;

import java.util.ArrayList;
import java.util.List;


import com.carterz30cal.mobs.packet.EntitySkinned;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftTrident;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.IndicatorTask;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.abilities.AbilityManager;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.abilities.DMobAbility;
import com.carterz30cal.tasks.TaskDamageKnockback;
import com.carterz30cal.tasks.TaskRemoveProjectile;
import com.carterz30cal.utility.StringManipulator;

import net.minecraft.server.v1_16_R3.EntityThrownTrident;

public class ListenerEntityDamage implements Listener
{
	public static boolean pvp = false;
	public static List<IndicatorTask> indicators = new ArrayList<IndicatorTask>();
	
	@EventHandler(priority=EventPriority.LOW)
	public void onEntityDamageEntity(EntityDamageByEntityEvent e)
	{
		e.setCancelled(false);
		e.setDamage(0);
		if (e.getEntity() instanceof Player && DMobManager.get(e.getEntity()) == null)
		{
			DMob mob = DMobManager.get(e.getDamager());
			if (e.getDamager().getType() == EntityType.ARROW
					|| e.getDamager().getType() == EntityType.TRIDENT) 
			{
				mob = DMobManager.get((Entity)((AbstractArrow)e.getDamager()).getShooter());
			}
			double damage = 0;
			
			if (mob == null) 
			{
				boolean show = true;
				Player damager = null;
				
				// check for custom player's husks
				if (e.getDamager().getType() == EntityType.HUSK 
						&& DMobManager.getId(e.getDamager()).split("_")[0].equals("NAV"))
				{
					e.setCancelled(true);
					EntitySkinned.alive.get(Integer.parseInt(DMobManager.getId(e.getDamager()).split("_")[1])).attack(DungeonsPlayerManager.i.get((Player) e.getEntity()));
				}
				
				
				if (e.getDamager().getType() == EntityType.ARROW
						|| e.getDamager().getType() == EntityType.TRIDENT) 
				{
					damage = e.getDamager().getPersistentDataContainer().getOrDefault(DMobManager.arrowDamage,PersistentDataType.INTEGER,0);
					AbstractArrow arrow = ((AbstractArrow)e.getDamager());
					if (arrow.getShooter() instanceof Player && pvp) damager = (Player)arrow.getShooter();
					
					e.setCancelled(!pvp);
					
				}
				else if (e.getDamager() instanceof Player)
				{
					e.setCancelled(!pvp);
					if (!pvp) return;
					damager = (Player)e.getDamager();
					DungeonsPlayer d = DungeonsPlayerManager.i.get(damager);
					damage = d.stats.damage;
					if (damager.getInventory().getItemInMainHand().getType() == Material.BOW) damage = 1;
					else if (e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) 
					{
						damage = d.stats.damageSweep;
					}
					else if (damager.getAttackCooldown() < 0.95) damage = damage / 5;
					
				}
				else show = false;
				if (show && !e.isCancelled())
				{
					Location hitLocation = e.getEntity().getLocation().subtract(e.getEntity().getLocation().subtract(damager.getLocation()).multiply(0.3))
							.add(0,1.25,0);
					ArmorStand h = DMobManager.hit(e.getEntity(), (int)damage,ChatColor.WHITE);
					
					IndicatorTask t = new IndicatorTask(h,hitLocation);
					t.runTaskTimer(Dungeons.instance, 1,15);
					indicators.add(t);
				}
				
			}
			else damage = mob.type.damage;
			DungeonsPlayer d = DungeonsPlayerManager.i.get((Player)e.getEntity());
			if (d.stats.abilities != null) for (AbsAbility a : d.stats.abilities) if (a != null && mob != null) damage *= a.onDamage(d,damage,e.getCause(),mob.type);
			d.damage((int)damage, false);
		}
		else
		{
			DMob mob = DMobManager.get(e.getEntity());
			if (mob == null)
			{
				if (!e.getEntity().isInvulnerable()) e.getEntity().remove();
				return;
			}
			
			
			
			Player damager;
			int damage;
			ArrayList<AbsAbility> abilities = new ArrayList<AbsAbility>();
			ChatColor hit = ChatColor.WHITE;
			
			if (e.getDamager() instanceof AbstractArrow)
			{
				AbstractArrow arrow = (AbstractArrow)e.getDamager();
				Entity shooter = (Entity) arrow.getShooter();
				if (!(shooter instanceof Player)) 
				{
					e.setCancelled(true);
					return;
				}
				damage = arrow.getPersistentDataContainer().getOrDefault(DMobManager.arrowDamage, PersistentDataType.INTEGER,1);
				if (e.getDamager() instanceof Trident || arrow.isCritical()) 
				{
					hit = ChatColor.RED;
					new SoundTask(shooter.getLocation(),(Player)shooter,Sound.ENTITY_ARROW_HIT_PLAYER,0.7f,0.4f).runTaskLater(Dungeons.instance, 1);
				}
				else 
				{
					damage /= 2;
					new SoundTask(shooter.getLocation(),(Player)shooter,Sound.ENTITY_ARROW_HIT_PLAYER,1.2f,1.1f).runTaskLater(Dungeons.instance, 1);
				}
				damager = (Player)shooter;
				
				if (e.getDamager().getType() == EntityType.TRIDENT)
				{
					EntityThrownTrident t = ((CraftTrident) e.getDamager()).getHandle();
					ItemStack thrownTrident = CraftItemStack.asBukkitCopy(t.trident);
					Item item = ItemBuilder.get(thrownTrident);
					
					if (item.data.containsKey("ability")) abilities.add(AbilityManager.get((String)item.data.get("ability")));
				}
			}
			else if (e.getDamager() instanceof Player)
			{
				damager = (Player)e.getDamager();
				DungeonsPlayer d = DungeonsPlayerManager.i.get(damager);
				damage = d.stats.damage;
				if (damager.getInventory().getItemInMainHand().getType() == Material.BOW) damage = 1;
				else if (e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK)
				{
					if (mob.entities.get(0) != e.getEntity())
					{
						e.setCancelled(true);
						return;
					}
					damage = d.stats.damageSweep;
				}
				else if (damager.getAttackCooldown() < 0.95 && damager.getLocation().distance(e.getEntity().getLocation()) < 4) damage = damage / 5;
			}
			else return;
			DungeonsPlayer dp = DungeonsPlayerManager.i.get(damager);
			abilities.addAll(dp.stats.abilities);
			//for (AbsAbility a : abilities) damage = a.onAttack(dp, mob,damage);
			
			for (DMobAbility mab : mob.type.abilities) damage = mab.damaged(mob, dp,damage);
			damage = Math.max(0, damage);
			mob.damage(damage, damager,abilities);
			
			
			Location hitLocation = e.getEntity().getLocation().subtract(e.getEntity().getLocation().subtract(damager.getLocation()).multiply(0.3))
					.add(0,1.25,0);
			ArmorStand h = DMobManager.hit(e.getEntity(), mob.lastDamage,hit);
			
			IndicatorTask t = new IndicatorTask(h,hitLocation);
			t.runTaskTimer(Dungeons.instance, 1,15);
			indicators.add(t);

			((LivingEntity)e.getEntity()).setNoDamageTicks(0);
			if (mob.type.kbresist*(1/damager.getAttackCooldown()) > Math.random() || (mob.modifier != null && mob.modifier.dmgresist > 0.45))
			{
				if (e.getEntity() instanceof Damageable) new TaskDamageKnockback(e.getEntity(),0).runTaskLater(Dungeons.instance, 1);
			}
			if (damager.getInventory().getItemInMainHand().hasItemMeta()) for (AbsEnchant en : EnchantManager.get(damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer())) if (en != null) en.onHitAfter(dp, mob,h);

		}
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent e)
	{
		if (e.getState() == State.CAUGHT_ENTITY && e.getCaught() instanceof LivingEntity)
		{
			if (e.getCaught() instanceof Player) e.setCancelled(true);
			else ((LivingEntity)e.getCaught()).damage(0, e.getPlayer());
		}
		if (e.getState() == State.CAUGHT_FISH || e.getState() == State.BITE) e.setCancelled(true);
	}
	
	
	
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onSlimeSplit(SlimeSplitEvent e)
	{
		e.setCancelled(true);
	}
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e)
	{
		if (e.getHitBlock() != null && e.getEntityType() != EntityType.TRIDENT)
		{
			new TaskRemoveProjectile(e.getEntity()).runTaskLater(Dungeons.instance, 15);
		}
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void launchProjectile(ProjectileLaunchEvent e)
	{
		EntityType t = e.getEntityType();
		if (t == EntityType.TRIDENT || t == EntityType.ARROW)
		{
			Projectile p = e.getEntity();
			ProjectileSource shooter = p.getShooter();
			if (shooter instanceof Player)
			{
				DungeonsPlayer d = DungeonsPlayerManager.i.get((Player) shooter);
				p.getPersistentDataContainer().set(DMobManager.arrowDamage, PersistentDataType.INTEGER, d.stats.damage);
			}
			else
			{
				DMob m = DMobManager.get((Entity) shooter);
				if (m != null) 
				{
					p.getPersistentDataContainer().set(DMobManager.arrowDamage, PersistentDataType.INTEGER, m.type.damage);
				}
			}
		}
	}
	
	@EventHandler
	public void onTame(EntityTameEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBow(EntityShootBowEvent e)
	{
		e.setConsumeItem(false);
		if (e.getEntity() instanceof Player) ((Player)e.getEntity()).updateInventory();
		if (e.getProjectile() instanceof AbstractArrow)
		{
			((AbstractArrow)e.getProjectile()).setPickupStatus(PickupStatus.DISALLOWED);
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent e)
	{
		e.setDamage(0);
		
		if (e.getEntity() instanceof Player && DMobManager.get(e.getEntity()) == null)
		{
			Player p = (Player)e.getEntity();
			DungeonsPlayer dp = DungeonsPlayerManager.i.get((Player)e.getEntity());
			
			if (e.isCancelled()) return;
			double dealt = 0;
			boolean trued = true;
			switch (e.getCause())
			{
			case DROWNING:
				dealt = ((dp.stats.health/8f)+dp.stats.regen);
				((Player)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,0));
				break;
			case VOID:
				dealt = Integer.MAX_VALUE;
				break;
			case CONTACT:
				dealt = 10;
				break;
			case PROJECTILE:
			case ENTITY_ATTACK:
				break;
			case FIRE:
				dealt = 15*(1+((float)dp.stats.armour/100));
				break;
			case FIRE_TICK:
				dealt = 5*(1+((float)dp.stats.armour/100));
				break;
			case LAVA:
				dealt = Math.max(dp.stats.health/2,200);
				break;
			case WITHER:
				dealt = 20 + (dp.stats.health / 50);
				break;
			case POISON:
				dealt = 5;
				break;
			case FALL:
				dealt = Math.pow(p.getFallDistance()/2.25, 2.1);
				if (Dungeons.w.getBlockAt(e.getEntity().getLocation().subtract(0, 1, 0)).getType() == Material.BLACK_CONCRETE) dealt = Integer.MAX_VALUE;
				break;
			default:
				break;
			}
			if (e.getCause() != DamageCause.ENTITY_ATTACK && e.getCause() != DamageCause.PROJECTILE) for (AbsAbility a : dp.stats.abilities) dealt *= a.onDamage(dp,dealt,e.getCause(),null);
			if (dealt > 0) dp.damage((int)dealt, trued);
		}
		else 
		{
			e.getEntity().setFireTicks(0);
			DMob mob = DMobManager.get(e.getEntity());
			switch (e.getCause())
			{
			case SUFFOCATION:
				if (mob != null)
				{
					mob.damage(5, null);
				}
				break;
			case VOID:
				if (mob != null) mob.destroy(null);
				else
				{
					e.getEntity().remove();
				}
				break;
			case ENTITY_EXPLOSION:
			case BLOCK_EXPLOSION:
				new TaskDamageKnockback(e.getEntity(),0).runTask(Dungeons.instance);
			case FALL:
				if (Dungeons.w.getBlockAt(e.getEntity().getLocation().subtract(0, 1, 0)).getType() == Material.BLACK_CONCRETE
					&& mob != null)
				{
					mob.remove();
					break;
				}
			default:
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void onEntityIgnite(ExplosionPrimeEvent e)
	{
		e.setCancelled(true);
		if (e.getEntityType() == EntityType.CREEPER)
		{
			DMob mob = DMobManager.get(e.getEntity());
			if (mob != null)
			{
				e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 3, false,false,e.getEntity());
				mob.destroy(null);
			}
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent e)
	{
		e.setDroppedExp(0);
		e.getDrops().clear();
		
		DMob mob = DMobManager.get(e.getEntity());
		if (mob != null) 
		{
			if (e.getEntity().getType() == EntityType.SLIME)
			{
				String slime = StringManipulator.get(mob.type.entityData, "splitmob");
				if (slime == null) return;
				int am = Integer.parseInt(StringManipulator.get(mob.type.entityData, "splitcount"));
				for (int i = 0; i < am;i++) new DMob(DMobManager.types.get(slime),mob.owner,e.getEntity().getLocation(),false);
			}
		}
	}
	@EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event)
	{
		event.setCancelled(true);
    }
	
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e)
	{
		if (e.getEntity() instanceof LivingEntity) ((LivingEntity)e.getEntity()).getEquipment().clear();
	}
	
}
