package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;
import com.carterz30cal.utility.ParticleFunctions;
import org.bukkit.Particle;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class AbilitySpiritcrusher extends AbsAbility
{
	public static Map<DungeonsPlayer,Integer> cooldown = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Skullcrushing Throw");
		d.add("Throw your axe, dealing melee damage");
		d.add("upto 19 blocks away.");
		d.add("Thrown axes also deal 18% as magic damage.");
		d.add("Costs 20 mana per throw.");
		d.add("Doesn't deal sweeping damage.");
		return d;
	}
	public void onTick  (DungeonsPlayer d) 
	{
		cooldown.put(d, cooldown.getOrDefault(d, 0) - 1);
	}
	@Override
	public void finalStats(DungeonsPlayerStats d)
	{
		d.damageSweep = 0;
	}
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (cooldown.getOrDefault(d, 0) > 0) return false;
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (!d.useMana(20)) return false;
			cooldown.put(d, 20);
			ItemStack held = d.player.getInventory().getItemInMainHand();
			ArmorStand a = (ArmorStand) e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation().add(0,0.5,0), EntityType.ARMOR_STAND);
			a.setVisible(false);
			a.setInvulnerable(true);
			a.setCanPickupItems(false);
			a.addEquipmentLock(EquipmentSlot.HAND, LockType.REMOVING_OR_CHANGING);
			a.getEquipment().setItemInMainHand(held);
			new BukkitRunnable()
			{
				Vector dir = e.getPlayer().getEyeLocation().getDirection().normalize().multiply(0.5);
				ArrayList<Entity> hit = new ArrayList<Entity>();
				boolean returning = false;
				
				private void going()
				{
					a.setRightArmPose(a.getRightArmPose().add(0.35, 0, 0));
					a.teleport(a.getLocation().add(dir));
					
					double dist = e.getPlayer().getLocation().distance(a.getLocation());
					double req = 19;
					if (dist > req || !Dungeons.w.getBlockAt(a.getLocation().add(0,1,0)).isPassable())
					{
						returning = true;
						hit.clear();
						return;
					}

					damage();
				}

				private void damage()
				{
					Collection<Entity> ent = a.getWorld().getNearbyEntities(a.getLocation().add(0,1,0), 1, 1, 1);
					for (Entity en : ent)
					{
						if (en == e.getPlayer() || en == a || en == d.player || !(en instanceof LivingEntity)
								|| hit.contains(en) || DMobManager.get(en) == null) continue;

						LivingEntity len = (LivingEntity)en;
						PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (((CraftEntity)en).getHandle(),(byte)2);
						for (Player k : Bukkit.getOnlinePlayers())
						{
							((CraftPlayer)k).getHandle().playerConnection.sendPacket(packet);
						}
						DMobManager.get(en).damage((int) (d.stats.damage * 0.18), d, DamageType.MAGIC, false);
						len.damage(0,e.getPlayer());
						hit.add(en);
					}
				}
				private void returning()
				{
					double rotation = (e.getPlayer().getLocation().distance(a.getLocation())-1.3) / 6;
					a.setRightArmPose(new EulerAngle(rotation,0,0));
					
					Vector v = a.getLocation().subtract(e.getPlayer().getLocation()).toVector().normalize();
					a.teleport(a.getLocation().subtract(v));
					
					if (e.getPlayer().getLocation().distance(a.getLocation()) < 0.5)
					{
						a.remove();
						cancel();
						return;
					}
					damage();
				}
				@Override
				public void run()
				{
					ParticleFunctions.moving(a.getLocation().clone().add(0,1,0), Particle.ENCHANTMENT_TABLE, 4,0.2);
					if (returning) returning();
					else going();
				}
		
			}.runTaskTimer(Dungeons.instance, 1,1);
			return true;
		}
		return false;
	}

}

