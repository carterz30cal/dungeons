package com.carterz30cal.player;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
	
	/*
	this is an admin-only function which archives a player account into a backup YML file then wipes the UUIDs data from players.yml.
	this is mainly for playtesting while saving the original data automatically.
	*/
	public void archive(Player p)
	{
		File backup = new File(Dungeons.instance.getDataFolder(), "backups.yml");
		if (!backup.exists())
		{
			backup.getParentFile().mkdirs();
			Dungeons.instance.saveResource("backups.yml",false);
		}
		FileConfiguration c = new YamlConfiguration();
		try 
		{
            c.load(backup);
        } 
		catch (IOException | InvalidConfigurationException e) 
		{
            e.printStackTrace();
        }
		
		save(c,DungeonsPlayerManager.i.get(p));
		try 
		{
			c.save(backup);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// create new data
		Dungeons.instance.getPlayerConfig().set(p.getUniqueId().toString(), null);
		create(p);
	}
	public void create(Player p)
	{
		FileConfiguration playerc = Dungeons.instance.getPlayerConfig();
		String path = p.getUniqueId().toString();
		
		boolean exists = true;
		if (!playerc.contains(path)) 
		{
			createData(p.getUniqueId());
			exists = false;
		}
		DungeonsPlayer d = new DungeonsPlayer(p);
		d.newaccount = !exists;
		players.put(p.getUniqueId(), d);
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
			playerc.createSection(path + ".skilltree");
			playerc.createSection(path + ".explorer");
			playerc.createSection(path + ".quests");
			playerc.createSection(path + ".sack");
			playerc.createSection(path + ".bestiary");
		}
	}
	public void save(DungeonsPlayer dp)
	{
		save(Dungeons.instance.getPlayerConfig(),dp);
	}
	public void save(FileConfiguration playerc,DungeonsPlayer dp)
	{
		String path = dp.player.getUniqueId().toString();
		if (!playerc.contains(path)) createData(dp.player.getUniqueId());
		
		playerc.set(path + ".skilltree", null);
		for (String sk : dp.skills.keySet())
		{
			playerc.set(path + ".skilltree." + sk,dp.skills.get(sk));
		}

		playerc.set(path + ".backpack", null);
		playerc.createSection(path + ".backpack");
		playerc.set(path + ".sack", null);
		playerc.createSection(path + ".sack");
		playerc.set(path + ".bestiary", null);
		playerc.createSection(path + ".bestiary");
		playerc.set(path + ".tutorials", dp.tutorials);
		playerc.set(path + ".playtime", dp.playtime);
		playerc.set(path + ".kills", dp.kills);
		playerc.set(path + ".version", Dungeons.instance.getDescription().getVersion());
		for (int p = 0; p < dp.backpackb.size(); p++) for (BackpackItem item : dp.backpackb.get(p)) if (item != null) item.save(playerc,path + ".backpack." + p);
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
		for (Entry<String,Integer> ingredient : dp.sack.entrySet())
		{
			playerc.set(path + ".sack." + ingredient.getKey(), ingredient.getValue());
		} 
		for (Entry<String,Integer> ingredient : dp.bestiary.entrySet())
		{
			playerc.set(path + ".bestiary." + ingredient.getKey(), ingredient.getValue());
		} 
		playerc.set(path + ".location", dp.area.id);
		playerc.set(path + ".coins", dp.coins);
		playerc.set(path + ".rank",dp.rank.ordinal());
		
		playerc.set(path + ".experience", dp.level.experience);
		playerc.set(path + ".level", dp.level.level);
		playerc.set(path + ".hunter", dp.level.hunter);
		if (dp.voteBoost == null || dp.voteBoost.isBefore(Instant.now())) playerc.set(path + ".voteboost", null);
		else playerc.set(path + ".voteboost", dp.voteBoost.toEpochMilli());
	}
}
