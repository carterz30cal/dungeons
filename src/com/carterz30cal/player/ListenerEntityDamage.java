package com.carterz30cal.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.IndicatorTask;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.tasks.TaskDamageKnockback;
import com.carterz30cal.tasks.TaskRemoveProjectile;

public class ListenerEntityDamage implements Listener
{
	public List<IndicatorTask> indicators = new ArrayList<IndicatorTask>();
	
	@EventHandler(priority=EventPriority.LOW)
	public void onEntityDamageEntity(EntityDamageByEntityEvent e)
	{
		e.setCancelled(false);
		e.setDamage(0);
		if (e.getEntity() instanceof Player)
		{
			DungeonMob mob = DungeonMob.getMob(e.getDamager().getUniqueId());
			int damage = 0;
			if (mob == null) 
			{
				if (e.getDamager().getType() == EntityType.ARROW) damage = e.getDamager().getPersistentDataContainer().getOrDefault(DungeonMobCreator.arrowDamage,PersistentDataType.INTEGER,0);
				else return;
			}
			else damage = mob.type.damage;
			DungeonsPlayerManager.i.get((Player)e.getEntity()).damage(damage, false);
		}
		else
		{
			if (e.getDamager() instanceof AbstractArrow)
			{
				AbstractArrow arrow = (AbstractArrow)e.getDamager();
				Entity shooter = (Entity) arrow.getShooter();
				
				
				DungeonMob mob = DungeonMob.getMob(e.getEntity().getUniqueId());
				if (mob == null)
				{
					if (!e.getEntity().isInvulnerable()) e.getEntity().remove();
					return;
				}
				
				int damage = arrow.getPersistentDataContainer().getOrDefault(DungeonMobCreator.arrowDamage, PersistentDataType.INTEGER,1);
				ChatColor hitColour = ChatColor.WHITE;
				if (arrow.isCritical()) hitColour = ChatColor.RED;
				else damage *= 0.5;
				
				if (shooter instanceof Player)
				{
					mob.damage(damage, (Player)shooter);
					
					Location hitLocation = e.getEntity().getLocation().subtract(e.getEntity().getLocation().subtract(shooter.getLocation()).multiply(0.3))
							.add(0,1.25,0);
					ArmorStand h = DungeonMobCreator.i.createHit(e.getEntity(), damage,hitColour);
					
					IndicatorTask t = new IndicatorTask(h,hitLocation);
					if (arrow.isCritical()) new SoundTask(shooter.getLocation(),(Player)shooter,Sound.ENTITY_ARROW_HIT_PLAYER,0.7f,0.4f).runTaskLater(Dungeons.instance, 1);
					else new SoundTask(shooter.getLocation(),(Player)shooter,Sound.ENTITY_ARROW_HIT_PLAYER,1.2f,1.1f).runTaskLater(Dungeons.instance, 1);
					t.runTaskTimer(Dungeons.instance, 1,15);
					indicators.add(t);
				}
				else mob.damage(damage, null);
				

			}
			else if (e.getDamager() instanceof Player)
			{
				Player p = (Player)e.getDamager();
				DungeonsPlayer dp = DungeonsPlayerManager.i.get(p);
				DungeonMob mob = DungeonMob.getMob(e.getEntity().getUniqueId());
				if (mob == null)
				{
					if (!e.getEntity().isInvulnerable()) e.getEntity().remove();
					return;
				}
				else if (mob.entity.isInvulnerable()) 
				{
					e.setCancelled(true);
					return;
				}
				e.setCancelled(false);
				mob.entity.damage(0);
				int damage = dp.stats.damage;
				if (p.getInventory().getItemInMainHand().getType() == Material.BOW) damage = 1;
				
				if (e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) 
				{
					if (e.getEntity() == mob.armourstand || e.getEntity() == mob.silverfish) 
					{
						e.setCancelled(true);
						return;
					}
					damage = dp.stats.damageSweep;
				}
				else damage = Math.round(damage*p.getAttackCooldown());
				
				Location hitLocation = e.getEntity().getLocation().subtract(e.getEntity().getLocation().subtract(p.getLocation()).multiply(0.3))
						.add(0,1.25,0);
				ArmorStand h;
				if (mob.modifier == null) h = DungeonMobCreator.i.createHit(e.getEntity(), (damage-mob.type.armour),null);
				else h = DungeonMobCreator.i.createHit(e.getEntity(),(int) Math.round((damage-mob.type.armour)*(1-mob.modifier.damageReduction)),null);
				IndicatorTask t = new IndicatorTask(h,hitLocation);
				t.runTaskTimer(Dungeons.instance, 1,15);
				indicators.add(t);
				
				mob.damage(damage, p);
				((LivingEntity)mob.entity).setNoDamageTicks(0);
				if (mob.type.knockbackResist*(1/p.getAttackCooldown()) > Math.random() || (mob.modifier != null && mob.modifier.damageReduction > 0.45))
				{
					new TaskDamageKnockback(e.getEntity(),0).runTaskLater(Dungeons.instance, 1);
				}
				if (((Player)e.getDamager()).getInventory().getItemInMainHand() != null) for (AbsEnchant en : EnchantManager.get(((Player)e.getDamager()).getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer())) if (en != null) en.onHitAfter(dp, mob,h);
				//if (((Damageable)e.getEntity()).getHealth() > 1) e.setDamage(1);
			}
		}
	}
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e)
	{
		if (e.getHitBlock() != null)
		{
			new TaskRemoveProjectile(e.getEntity()).runTaskLater(Dungeons.instance, 15);
		}
	}
	@EventHandler
	public void onBow(EntityShootBowEvent e)
	{
		if (e.getProjectile() instanceof AbstractArrow)
		{
			((AbstractArrow)e.getProjectile()).setPickupStatus(PickupStatus.DISALLOWED);
		}
		if (e.getEntity() instanceof Player)
		{
			DungeonsPlayer d = DungeonsPlayerManager.i.get((Player) e.getEntity());
			e.getProjectile().getPersistentDataContainer().set(DungeonMobCreator.arrowDamage, PersistentDataType.INTEGER, d.stats.damage);
		}
		else
		{
			DungeonMob m = DungeonMob.mobs.get(e.getEntity().getUniqueId());
			if (m != null) 
			{
				e.getProjectile().getPersistentDataContainer().set(DungeonMobCreator.arrowDamage, PersistentDataType.INTEGER, m.type.damage);
			}
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent e)
	{
		e.setDamage(0);
		
		if (e.getEntity() instanceof Player)
		{
			Player p = (Player)e.getEntity();
			DungeonsPlayer dp = DungeonsPlayerManager.i.get((Player)e.getEntity());
			switch (e.getCause())
			{
			case DROWNING:
				dp.damage((int) ((dp.stats.health/8f)+dp.stats.regen), true);
				((Player)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,0));
				break;
			case VOID:
				dp.damage(Integer.MAX_VALUE, true);
				break;
			case CONTACT:
				dp.damage(10, true);
				break;
			case ENTITY_ATTACK:
				break;
			case FIRE:
				dp.damage((int)(15*(1+((float)dp.stats.armour/100))), true);
				break;
			case FIRE_TICK:
				dp.damage((int)(5*(1+((float)dp.stats.armour/100))), true);
				break;
			case LAVA:
				dp.damage(Math.max(dp.stats.health/2,200), true);
				break;
			case WITHER:
				dp.damage(20, true);
				break;
			case FALL:
				dp.damage((int)Math.pow(p.getFallDistance()/2.25, 2.1), true);
				break;
			default:
				break;
			}
		}
		else 
		{
			DungeonMob mob = DungeonMob.mobs.get(e.getEntity().getUniqueId());
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
			case LIGHTNING:
				e.getEntity().setFireTicks(0);
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
			DungeonMob mob = DungeonMob.mobs.get(e.getEntity().getUniqueId());
			if (mob != null)
			{
				mob.entity.getLocation().getWorld().createExplosion(mob.entity.getLocation(), 3, false,false,e.getEntity());
				mob.destroy(null);
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e)
	{
		e.setDroppedExp(0);
		e.getDrops().clear();
	}
	@EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event)
	{
		event.setCancelled(true);
    }
	
}
