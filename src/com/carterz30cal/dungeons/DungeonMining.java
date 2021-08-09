package com.carterz30cal.dungeons;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;

import org.bukkit.ChatColor;

public class DungeonMining 
{
	public Dungeon dungeon;
	
	public HashMap<Material,Material> blocks;
	public HashMap<Material,Integer> hardness;
	public HashMap<Material,String> ores;
	public int xp;
	
	public String boss;
	public SpawnPosition spawn;
	public int requirement;
	public int progress;
	
	public HashMap<Block,Material> raresreplace = new HashMap<>();
	public Material rareore;
	public List<Material> rarecorrection;
	public boolean replace;
	public int chance;
	public int outof;
	
	public DungeonMining()
	{
		blocks = new HashMap<Material,Material>();
		ores = new HashMap<Material,String>();
		hardness = new HashMap<>();
		
		xp = 0;
		boss = null;
		requirement = 10000;
		progress = 0;
	}
	
	public void progress()
	{
		progress++;
		if (progress >= requirement) 
		{
			for (Player p : dungeon.players) p.sendMessage(ChatColor.RED + "You hear a deep rumbling!");
			progress = 0;
			new DMob(DMobManager.types.get(boss),spawn,spawn.position,false);
		}
	}
}
