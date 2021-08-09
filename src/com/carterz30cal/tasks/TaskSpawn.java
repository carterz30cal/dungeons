package com.carterz30cal.tasks;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.npcs.NPCManager;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskSpawn extends BukkitRunnable
{

	@Override
	public void run() 
	{
		ArrayList<Player> players = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers())
		{
			DungeonsPlayer pd = DungeonsPlayerManager.i.get(p);
			int h = DungeonManager.i.hash(p.getLocation().getBlockZ());
			pd.area.players.remove(pd.player);
			pd.area = DungeonManager.i.dungeons.getOrDefault(h, DungeonManager.i.hub);
			pd.area.players.add(pd.player);
			if (p.getGameMode() == GameMode.SURVIVAL) players.add(p);
			pd.area.activated = true;
		}
		
		for (Dungeon d : DungeonManager.i.dungeons.values())
		{
			if (!d.activated) continue;
			for (Entry<SpawnPosition, String> spawn : d.spawns.entrySet())
			{
				boolean spa = false;
				for (Player l : players)
				{
					
					if (spawn.getKey().position.distance(l.getLocation()) < 14 && Math.abs(spawn.getKey().position.getBlockY()-l.getLocation().getBlockY()) <= 5
							&& !checkIfBlocksOnVector(spawn.getKey().position,l.getLocation()))
					{
						spa = true;
						break;
					}
				}
				if (spawn.getKey().mob != null && !spawn.getKey().mob.entities.get(0).isValid()) 
				{
					spawn.getKey().mob.remove();
					spawn.getKey().mob = null;
				}
				if (spa && spawn.getKey().mob == null)
				{
					DMobManager.spawn(spawn.getValue(), spawn.getKey());
				}
				
			}
			d.activated = false;
		}
		
		NPCManager.sendNPCs();
	}
	
	private static boolean checkIfBlocksOnVector(Location a, Location b) {
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
