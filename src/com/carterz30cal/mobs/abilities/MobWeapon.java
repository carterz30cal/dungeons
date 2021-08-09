package com.carterz30cal.mobs.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.ArmourstandFunctions;
import com.carterz30cal.utility.RandomFunctions;

import org.bukkit.ChatColor;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class MobWeapon extends DMobAbility 
{
	public Map<DMob,ThrowingWeapon> weapons = new HashMap<>();
	public Map<DMob,Integer> cooldown = new HashMap<>();
	public String item;
	public int damage;
	public int cd;
	
	
	
	public MobWeapon(FileConfiguration data, String path) {
		super(data, path);
		
		item = data.getString(path + ".item", "IRON_AXE");
		damage = data.getInt(path + ".damage",0);
		cd = data.getInt(path + ".cooldown",20);
	}

	public void add(DMob mob)
	{
		List<Player> potential = new ArrayList<>(Bukkit.getOnlinePlayers());
		potential.removeIf((Player p) -> p.getLocation().distance(mob.entities.get(0).getLocation()) > 15);
		if (potential.size() == 0) return;
		Player target = (Player) RandomFunctions.get(potential.toArray());
		weapons.put(mob, new ThrowingWeapon(mob.entities.get(0).getLocation(),target,item));
	}
	
	public void tick(DMob mob)
	{
		if (!weapons.containsKey(mob)) 
		{
			int c = cooldown.getOrDefault(mob, 0);
			if (c < cd) cooldown.put(mob, c+1);
			else
			{
				List<Player> potential = new ArrayList<>(Bukkit.getOnlinePlayers());
				potential.removeIf((Player p) -> p.getLocation().distance(mob.entities.get(0).getLocation()) > 15 || p.getGameMode() != GameMode.SURVIVAL);
				if (potential.size() == 0) return;
				Player target = (Player) RandomFunctions.get(potential.toArray());
				weapons.put(mob, new ThrowingWeapon(mob.entities.get(0).getLocation(),target,item));
				cooldown.put(mob,0);
			}
			return;
		}
		ThrowingWeapon t = weapons.get(mob);
		ArmorStand w = t.w;
		t.tick++;
		try
		{
			w.teleport(w.getLocation().add(t.direction), TeleportCause.PLUGIN);
		}
		catch (IllegalArgumentException e)
		{
			System.out.println(ChatColor.RED + "[Dungeons/MobWeapon] Infinity Error - Deleted Weapon.");
			t.w.remove();
			weapons.remove(mob);
			return;
		}
		
		w.setRightArmPose(new EulerAngle(w.getRightArmPose().getX()+0.15,0,0.5));
		if (t.tick > 80)
		{
			t.w.remove();
			weapons.remove(mob);
			return;
		}
		List<Player> potential = new ArrayList<>(Bukkit.getOnlinePlayers());
		potential.removeIf((Player p) -> p.getLocation().distance(t.w.getLocation()) > 0.7);
		if (potential.size() > 0)
		{
			for (Player p : potential) 
			{
				DungeonsPlayerManager.i.get(p).damage(damage, false);
				p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.4f, 0.9f);
				
				CraftPlayer c = (CraftPlayer)p;
				PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (c.getHandle(),(byte)2);
				for (Player k : Bukkit.getOnlinePlayers())
				{
					c = (CraftPlayer)k;
					c.getHandle().playerConnection.sendPacket(packet);
				}
			}
			Dungeons.w.spawnParticle(Particle.ITEM_CRACK, t.w.getLocation(), 30, 0.1, 0.1, 0.1, ItemBuilder.i.build(item, 1));
			t.w.remove();
			weapons.remove(mob);
		}
	}
	
	public void killed(DMob mob)
	{
		if (weapons.containsKey(mob)) weapons.get(mob).w.remove();
	}
}

class ThrowingWeapon
{
	public ArmorStand w;
	public Vector direction;
	public int tick;
	
	public ThrowingWeapon(Location l,Entity target,String item)
	{
		w = ArmourstandFunctions.create(l);
		w.setSmall(false);
		w.setSilent(true);
		w.addEquipmentLock(EquipmentSlot.HAND, LockType.REMOVING_OR_CHANGING);
		w.getEquipment().setItemInMainHand(ItemBuilder.i.build(item, 1));
		w.setRightArmPose(new EulerAngle(0.5,0,0.5));
		w.setInvulnerable(true);
		w.setCustomNameVisible(false);
		
		direction = (target.getLocation().toVector().subtract(l.toVector())).normalize().multiply(0.6);
	}
}