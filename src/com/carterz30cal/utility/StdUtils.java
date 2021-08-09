package com.carterz30cal.utility;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.player.DungeonsPlayer;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class StdUtils
{
	public static void drawLaser(Location l1,Location l2,int density,float size,Color colour)
	{
		Location ad = l1.clone();
		Vector dir = l2.toVector().subtract(l1.toVector()).multiply(1d/density);
		for (int i = 0; i < density;i++) Dungeons.w.spawnParticle(Particle.REDSTONE,ad.add(dir), 1, 0, 0, 0, new Particle.DustOptions(colour, size));
	}
	public static void damageAnim(DungeonsPlayer hurt)
	{
		CraftPlayer c = (CraftPlayer)hurt.player;
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (c.getHandle(),(byte)2);
		for (Player k : Bukkit.getOnlinePlayers())
		{
			c = (CraftPlayer)k;
			c.getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	public static void setBlock(int x, int y, int z, Material type, byte data) {
	    net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) Dungeons.w).getHandle();
	    net.minecraft.server.v1_16_R3.Chunk nmsChunk = nmsWorld.getChunkAt(x >> 4, z >> 4);
	    BlockPosition bp = new BlockPosition(x, y, z);
	    
	    IBlockData ibd = CraftMagicNumbers.getBlock(type, data);

	    nmsChunk.setType(bp, ibd, false);
	}
	
	public static boolean areBlocksOnVector(Location a, Location b) {
        //A to B
        Vector v = b.toVector().subtract(a.toVector());
        double j = Math.floor(v.length());
        v.multiply(1/v.length()); //Converting v to a unit vector
        for (int i = 0; i<=j; i++) {
            v = b.toVector().subtract(a.toVector());
            v.multiply(1/v.length());
            Block block = a.getWorld().getBlockAt((a.toVector().add(v.multiply(i))).toLocation(a.getWorld()));
            if (!block.getType().equals(Material.AIR) && !block.getType().equals(Material.WATER) && !block.getType().equals(Material.TALL_GRASS)) { //Here you can set your own "transparent" blocks
                return true;
            }
        }
        return false;
    }
	
}
