package com.carterz30cal.dungeons;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import com.carterz30cal.commands.CommandDungeons;
import com.carterz30cal.commands.CommandHub;
import com.carterz30cal.crafting.RecipeManager;
import com.carterz30cal.gui.ListenerGUIEvents;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ShopManager;
import com.carterz30cal.mobs.DungeonManager;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.mobs.ListenerChunkUnload;
import com.carterz30cal.mobs.ListenerDismountEvent;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.ListenerBlockEvents;
import com.carterz30cal.player.ListenerEntityDamage;
import com.carterz30cal.player.NPCManager;
import com.carterz30cal.tasks.TaskSpawn;
public class Dungeons extends JavaPlugin 
{
	public static Dungeons instance;
	public static long scoreboardTick;
	
	private ListenerEntityDamage damager;
	private ListenerPlayerJoin join;
	private ListenerGUIEvents listenerGUI;
	private ListenerChunkUnload listenerChunk;
	private ListenerDismountEvent listenerDismount;
	private ListenerBlockEvents listenerBlock;
	
	public ScoreboardDisplayTask display;
	public RegenTask regen;
	
	private File players;
	private FileConfiguration playersC;
	
	private File lItems;
	private FileConfiguration lItemsC;
	
	private File fPerks;
	public FileConfiguration fPerksC;
	@Override
	public void onEnable()
	{
		instance = this;
		
		new ItemBuilder();
		
		listenerGUI = new ListenerGUIEvents();
		listenerChunk = new ListenerChunkUnload();
		listenerDismount = new ListenerDismountEvent();
		listenerBlock = new ListenerBlockEvents();
		damager = new ListenerEntityDamage();
		join = new ListenerPlayerJoin();
		
		display = new ScoreboardDisplayTask();
		regen = new RegenTask();
		
		initFiles();
		
		new DungeonManager();
		new RecipeManager();
		new DungeonsPlayerManager();
		new EnchantHandler();
	    new DungeonMobCreator();
	    new ShopManager();
		new NPCManager();
		
		DungeonMob.mobs = new HashMap<UUID,DungeonMob>();
		DungeonMob.silverfi = new HashMap<UUID,DungeonMob>();
		DungeonMob.arm = new HashMap<UUID,DungeonMob>();
		loadOnline();
		
		display.runTaskTimer(this, 0, 10);
		regen.runTaskTimer(this, 0, 40);
		new TaskSpawn().runTaskTimer(Dungeons.instance, 0, 400);
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(damager, this);
		pm.registerEvents(join, this);
		pm.registerEvents(listenerGUI, this);
		pm.registerEvents(listenerChunk, this);
		pm.registerEvents(listenerDismount, this);
		pm.registerEvents(listenerBlock, this);
		
		getCommand("dungeons").setExecutor(new CommandDungeons());
		getCommand("warp").setExecutor(new CommandHub());
	}
	
	private void initFiles()
	{
		players = new File(getDataFolder(), "players.yml");
		if (!players.exists())
		{
			players.getParentFile().mkdirs();
			saveResource("players.yml",false);
		}
		playersC = new YamlConfiguration();
		try 
		{
            playersC.load(players);
        } 
		catch (IOException | InvalidConfigurationException e) 
		{
            e.printStackTrace();
        }
		
		lItems = new File(getDataFolder(),"items.yml");
		if (!lItems.exists())
		{
			lItems.getParentFile().mkdirs();
			saveResource("items.yml",false);
		}
		lItemsC = new YamlConfiguration();
		try 
		{
			lItemsC.load(lItems);
		} 
		catch (IOException | InvalidConfigurationException e)
		{
            e.printStackTrace();
        }
		
		
		
		
		fPerks = new File(getDataFolder(),"perks.yml");
		if (!fPerks.exists())
		{
			fPerks.getParentFile().mkdirs();
			saveResource("perks.yml",false);
		}
		fPerksC = new YamlConfiguration();
		try 
		{
			fPerksC.load(fPerks);
		} 
		catch (IOException | InvalidConfigurationException e)
		{
            e.printStackTrace();
        }
	}
	
	private void loadOnline()
	{
		for (Player p : Bukkit.getServer().getOnlinePlayers())
		{
			DungeonsPlayerManager.i.create(p);
		}
	}
	
	public FileConfiguration getPlayerConfig()
	{
		return playersC;
	}
	

	@Override
	public void onDisable()
	{
		DungeonsPlayerManager.i.saveAll();
		try 
		{
			playersC.save(players);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		DungeonMob.noremove = true;
		for (DungeonMob mob : DungeonMob.mobs.values())
		{
			mob.destroy(null);
		}
		
		for (Entity slime : ShopManager.positions.keySet()) slime.remove();
		for (Player p : Bukkit.getOnlinePlayers()) 
		{
			p.closeInventory();
			NPCManager.removeNPCs(p);
		}
	}

}
