package com.carterz30cal.mobs.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.tasks.TaskBlockReplace;

public class MobAirWalker extends DMobAbility
{
	public MobAirWalker(FileConfiguration data, String path)
	{
		super(data, path);
	
		block = Material.valueOf(data.getString(path + ".block", "BEDROCK"));
	}

	public Material block;
	
	public void tick(DMob mob)
	{
		for (int x = -1; x <= 1; x++)
		{
			for (int z = -1; z <= 1; z++)
			{
				Location l = mob.entities.get(0).getLocation();
				l.setY(41);
				Block b = l.getBlock();
				
				if (!Dungeons.instance.blocks.containsKey(b) && b.getType() == Material.AIR)
				{
					TaskBlockReplace tbr = new TaskBlockReplace(b,Material.AIR);
					b.setType(block);
					tbr.runTaskLater(Dungeons.instance, 12);
					
					Dungeons.instance.blocks.put(b, tbr);
				}
				else if (Dungeons.instance.blocks.containsKey(b) && b.getType() == block)
				{
					TaskBlockReplace t = Dungeons.instance.blocks.get(b);
					t.cancel();
					
					Dungeons.instance.blocks.remove(b);
					TaskBlockReplace tbr = new TaskBlockReplace(b,Material.AIR);
					tbr.runTaskLater(Dungeons.instance, 12);
					Dungeons.instance.blocks.put(b, tbr);
				}
			}
		}
	}
}
