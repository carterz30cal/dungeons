package com.carterz30cal.tasks;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.crypts.RuneRoom;
import com.carterz30cal.dungeons.Dungeons;

public class TaskRuneRoom extends BukkitRunnable
{
	public RuneRoom room;
	
	public TaskRuneRoom(RuneRoom r)
	{
		room = r;
		
		runTaskTimer(Dungeons.instance,0,1);
	}
	
	@Override
	public void run()
	{
		if (room.display == null || !room.display.isValid() || room.display.isDead())
		{
			room.display = (ArmorStand)Dungeons.w.spawnEntity(room.midpoint().subtract(-1, 0.3, -1), EntityType.ARMOR_STAND);
			room.display.setInvulnerable(true);
			room.display.setVisible(false);
			room.display.setMarker(true);
			room.display.addEquipmentLock(EquipmentSlot.HEAD, LockType.REMOVING_OR_CHANGING);
		
			room.display.getEquipment().setHelmet(room.rune);
		}
		double oop = room.midpoint().getBlockY() + 0.2 + Math.sin(Dungeons.w.getTime() * 0.05);
		Location o = room.display.getLocation();
		o.setY(oop);
		o.setYaw(o.getYaw()+4);
		room.display.teleport(o, TeleportCause.PLUGIN);
		
	}

}
