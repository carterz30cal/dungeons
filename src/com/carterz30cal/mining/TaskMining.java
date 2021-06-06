package com.carterz30cal.mining;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.player.DungeonsPlayer;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockBreakAnimation;

public class TaskMining extends BukkitRunnable
{
	public DungeonsPlayer player;
	public Block mining;

	private int start;
	private int remaining;
	public TaskMining(DungeonsPlayer p,Block b)
	{
		player = p;
		mining = b;
		
		start = p.area.mining.hardness.get(b.getType());
		remaining = start;
		
		p.mining = this;
		
		runTaskTimer(Dungeons.instance,1,1);
	}
	@Override
	public void run() 
	{
		Block target = player.player.getTargetBlockExact(5);
		
		if (!player.stats.heldtype.equals("tool")) 
		{
			end();
			return;
		}
		if (mining == null || (!mining.equals(target) && target != null))
		{
			if (player == null || player.area == null || player.area.mining == null || player.area.mining.blocks == null || !player.area.mining.blocks.containsKey(target.getType()))
			{
				player.mining = null;
				end();
			}
			else
			{
				mining = target;
				start = player.area.mining.hardness.get(target.getType());
				remaining = start;
			}
			return;
		}
		
		//player.mining = this;
		remaining -= player.stats.miningspeed;
		
		// stuff
		int progress = (int) (9 * (1 - ((double)(remaining+1)/(start))));
		
		if (remaining < 0)
		{
			Bukkit.getServer().getPluginManager().callEvent(new BlockBreakEvent(mining,player.player));
			Location l = mining.getLocation();
			BlockPosition pos = new BlockPosition(l.getBlockX(),l.getBlockY(),l.getBlockZ());
			PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(l.hashCode(),pos,-1);
			
			((CraftPlayer)player.player).getHandle().playerConnection.sendPacket(packet);
			mining = null;
			return;
		}
		else
		{
			Location l = mining.getLocation();
			BlockPosition pos = new BlockPosition(l.getBlockX(),l.getBlockY(),l.getBlockZ());
			PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(l.hashCode(),pos,progress);
			
			((CraftPlayer)player.player).getHandle().playerConnection.sendPacket(packet);
		}
		
	}
	
	public void end()
	{
		cancel();
		
		if (mining != null)
		{
			Location l = mining.getLocation();
			BlockPosition pos = new BlockPosition(l.getBlockX(),l.getBlockY(),l.getBlockZ());
			PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(l.hashCode(),pos,-1);
			
			((CraftPlayer)player.player).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
