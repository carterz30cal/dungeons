package com.carterz30cal.player;

import java.util.Date;
import java.util.HashMap;
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
			playerc.createSection(path + ".perks");
			playerc.createSection(path + ".settings");
			playerc.createSection(path + ".backpack");
		}
	}
	@SuppressWarnings("deprecation")
	public void save(DungeonsPlayer dp)
	{
		FileConfiguration playerc = Dungeons.instance.getPlayerConfig();
		String path = dp.player.getUniqueId().toString();
		if (!playerc.contains(path)) createData(dp.player.getUniqueId());
		for (String skill : dp.skills.getSkills())
		{
			playerc.set(path + ".skills." + skill, dp.skills.getSkill(skill));
		}
		for (String perk : dp.perks.getPerks())
		{
			playerc.set(path + ".perks." + perk, dp.perks.getKills(perk));
		}
		HashMap<Integer,BackpackItem> items = new HashMap<Integer,BackpackItem>();
		for (BackpackItem item : dp.backpack)
		{
			if (item != null) items.put(item.slot, item);
		}
		for (int slot = 0; slot < 54;slot++)
		{
			if (items.containsKey(slot))
			{
				items.get(slot).save(path + ".backpack");
			}
			else playerc.set(path + ".backpack." + slot, null);
		}
		playerc.set(path + ".coins", dp.coins);
		Date set = new Date();
		playerc.set(path + ".lastlogin", set.getDate() + "/" + set.getMonth() + "/" + set.getYear());
		playerc.set(path + ".settings.progressbar", dp.settingSkillsDisplay);
		playerc.set(path + ".settings.perkbackground", dp.perkBackground);
		playerc.set(path + ".settings.colourblind", dp.colourblindMode);
		playerc.set(path + ".settings.highlightrenamed", dp.highlightRenamed);
		
	}
}
