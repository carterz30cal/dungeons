package com.carterz30cal.tasks;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskSpawn extends BukkitRunnable
{

	@Override
	public void run() 
	{
		ArrayList<Location> players = new ArrayList<Location>();
		for (Player p : Bukkit.getOnlinePlayers())
		{
			DungeonsPlayer pd = DungeonsPlayerManager.i.get(p);
			int h = DungeonManager.i.hash(p.getLocation().getBlockZ());
			pd.area.players.remove(pd.player);
			pd.area = DungeonManager.i.dungeons.getOrDefault(h, DungeonManager.i.hub);
			pd.area.players.add(pd.player);
			if (p.getGameMode() == GameMode.SURVIVAL) players.add(p.getLocation());
			pd.area.activated = true;
		}
		
		for (Dungeon d : DungeonManager.i.dungeons.values())
		{
			if (!d.activated) continue;
			for (Entry<SpawnPosition, String> spawn : d.spawns.entrySet())
			{
				boolean spa = false;
				for (Location l : players)
				{
					
					if (spawn.getKey().position.distance(l) < 15 && Math.abs(spawn.getKey().position.getBlockY()-l.getBlockY()) <= 4)
					{
						spa = true;
						break;
					}
				}
				if (spa && spawn.getKey().mob == null)
				{
					DungeonMobCreator.i.create(spawn.getValue().toLowerCase(), spawn.getKey());
				}
				
			}
			d.activated = false;
		}
	}
	
}
