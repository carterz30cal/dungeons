package com.carterz30cal.tasks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;

public class TaskBlockReplace extends BukkitRunnable
{
	public Block block;
	public Material material;
	
	public TaskBlockReplace(Block b, Material m)
	{
		block = b;
		material = m;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		block.setType(material);
		if (Dungeons.instance.blocks.containsKey(block)) Dungeons.instance.blocks.remove(block);
	}
}
