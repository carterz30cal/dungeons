package com.carterz30cal.player;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;


public class DungeonsPlayerManager
{
	public static DungeonsPlayerManager i;
	public HashMap<UUID,DungeonsPlayer> players;
	
	public DungeonsPlayerManager()
	{
		i = this;
		players = new HashMap<UUID,DungeonsPlayer>();
	}
	public DungeonsPlayer get(Player p)
	{
		if (!players.containsKey(p.getUniqueId())) 
		{
			create(p);
			return null;
		}
		else return players.get(p.getUniqueId());
	}
	
	public void create(Player p)
	{
		FileConfiguration playerc = Dungeons.instance.getPlayerConfig();
		String path = p.getUniqueId().toString();
		
		if (!playerc.contains(path)) createData(p.getUniqueId());
		players.put(p.getUniqueId(), new DungeonsPlayer(p));
	}
	public void saveAll()
	{
		for (DungeonsPlayer player : players.values())
		{
			save(player);
		}
	}
	public void createData(UUID uuid)
	{
		FileConfiguration playerc = Dungeons.instance.getPlayerConfig();
		String path = uuid.toString();
		if (!playerc.contains(path))
		{
			playerc.createSection(path);
			playerc.createSection(path + ".skills");
			//playerc.createSection(path + ".perks");
			//playerc.createSection(path + ".settings");
			playerc.createSection(path + ".explorer");
			playerc.createSection(path + ".quests");
		}
	}
	
	public void save(DungeonsPlayer dp)
	{
		FileConfiguration playerc = Dungeons.instance.getPlayerConfig();
		String path = dp.player.getUniqueId().toString();
		if (!playerc.contains(path)) createData(dp.player.getUniqueId());
		
		playerc.set(path + ".skills",null);
		for (String skill : dp.level.pointAllocation.keySet())
		{
			playerc.set(path + ".skills." + skill, dp.level.pointAllocation.get(skill));
		}
		/*
		for (String perk : dp.perks.getPerks())
		{
			playerc.set(path + ".perks." + perk, dp.perks.getKills(perk));
		}
		*/

		playerc.set(path + ".backpack", null);
		playerc.createSection(path + ".backpack");
		for (int p = 0; p < dp.backpackb.size(); p++) for (BackpackItem item : dp.backpackb.get(p)) if (item != null) item.save(path + ".backpack." + p);
		if (dp.explorer.areaPoints.size() > 0)
		{
			for (Entry<String,Integer> dungeon : dp.explorer.areaPoints.entrySet())
			{
				playerc.set(path + ".explorer." + dungeon.getKey(), dungeon.getValue());
			}
		}
		for (Entry<String,String> quest : dp.questProgress.entrySet())
		{
			playerc.set(path + ".quests." + quest.getKey(), quest.getValue());
		}
		playerc.set(path + ".coins", dp.coins);
		
		/*
		playerc.set(path + ".settings.progressbar", dp.settingSkillsDisplay);
		playerc.set(path + ".settings.perkbackground", dp.perkBackground);
		playerc.set(path + ".settings.colourblind", dp.colourblindMode);
		playerc.set(path + ".settings.highlightrenamed", dp.highlightRenamed);
		*/
		playerc.set(path + ".experience", dp.level.experience);
		playerc.set(path + ".points", dp.level.points);
		
	}
}
