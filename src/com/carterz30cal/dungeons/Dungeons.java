package com.carterz30cal.dungeons;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.Inventory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.carterz30cal.areas.EventTicker;
import com.carterz30cal.areas.NecropolisCrypts2;
import com.carterz30cal.areas.WaterwayRain;
import com.carterz30cal.areas.WaterwaySpearFishing;
import com.carterz30cal.bosses.BossManager;
import com.carterz30cal.commands.CommandDungeons;
import com.carterz30cal.commands.CommandHub;
import com.carterz30cal.commands.CommandLeaderboard;
import com.carterz30cal.commands.CommandTrade;
import com.carterz30cal.crafting.RecipeManager;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.gui.ListenerGUIEvents;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ShopManager;
import com.carterz30cal.items.abilities.AbilityManager;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.ListenerChunkUnload;
import com.carterz30cal.mobs.ListenerDismountEvent;
import com.carterz30cal.npcs.NPCManager;
import com.carterz30cal.packets.Packetz;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.ListenerBlockEvents;
import com.carterz30cal.player.ListenerEntityDamage;
import com.carterz30cal.quests.Quest;
import com.carterz30cal.tasks.TaskBlockReplace;
import com.carterz30cal.tasks.TaskSpawn;
import com.carterz30cal.utility.RandomFunctions;
public class Dungeons extends JavaPlugin 
{
	public static Dungeons instance;
	public static long scoreboardTick;
	public static World w;
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
	
	private File fPerks;
	public FileConfiguration fPerksC;
	
	public HashMap<Block,TaskBlockReplace> blocks;
	@Override
	public void onEnable()
	{
		instance = this;
		w = Bukkit.getWorld("hub");
		
		new ItemBuilder();
		
		listenerGUI = new ListenerGUIEvents();
		ListenerGUIEvents.cancelled = new ArrayList<Inventory>();
		
		listenerChunk = new ListenerChunkUnload();
		listenerDismount = new ListenerDismountEvent();
		listenerBlock = new ListenerBlockEvents();
		damager = new ListenerEntityDamage();
		join = new ListenerPlayerJoin();
		
		display = new ScoreboardDisplayTask();
		regen = new RegenTask();
		
		initFiles();
		
		new AbilityManager();
		new EnchantManager();
		new DungeonManager();
		new RecipeManager();
		new DungeonsPlayerManager();
	    new DMobManager();
	    new ShopManager();
		new NPCManager();
		new BossManager();
		
		
		new EventTicker();
		new WaterwayRain();
		new WaterwaySpearFishing();
		//new NecropolisCrypts();
		new NecropolisCrypts2();
		
		RandomFunctions.r = new Random();
		//-69, 100, 20994
		
		blocks = new HashMap<Block,TaskBlockReplace>();
		loadOnline();
		
		display.runTaskTimer(this, 0, 5);
		regen.runTaskTimer(this, 0, 40);
		new TaskSpawn().runTaskTimer(Dungeons.instance, 0, 325);
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(damager, this);
		pm.registerEvents(join, this);
		pm.registerEvents(listenerGUI, this);
		pm.registerEvents(listenerChunk, this);
		pm.registerEvents(listenerDismount, this);
		pm.registerEvents(listenerBlock, this);
		
		getCommand("dungeons").setExecutor(new CommandDungeons());
		getCommand("warp").setExecutor(new CommandHub());
		getCommand("leaderboard").setExecutor(new CommandLeaderboard());
		getCommand("trade").setExecutor(new CommandTrade());
		
		NPCManager.sendall();
		
		Quest.init();
		Packetz.init();
		
		
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
		DMobManager.end();
		EventTicker.end();
		NPCManager.purge();
		
		for (TaskBlockReplace t : blocks.values())
		{
			t.block.setType(t.material);
			t.cancel();
		}
		
		Quest.remove();
	}

}
