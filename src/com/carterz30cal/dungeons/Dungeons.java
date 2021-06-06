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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.inventory.Inventory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.carterz30cal.areas.EventTicker;
import com.carterz30cal.areas.InfestedHunter;
import com.carterz30cal.areas.NecropolisBoss;
import com.carterz30cal.areas.NecropolisCrypts2;
import com.carterz30cal.areas.WaterwayBoss;
import com.carterz30cal.areas.WaterwayRain;
import com.carterz30cal.areas.WaterwaySandMiniboss;
import com.carterz30cal.areas.WaterwaySpearFishing;
import com.carterz30cal.areas.WaterwayTutorial;
import com.carterz30cal.bosses.BossManager;
import com.carterz30cal.commands.*;
import com.carterz30cal.crafting.RecipeManager;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.gui.ListenerGUIEvents;
import com.carterz30cal.gui.MarketGUI;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ShopManager;
import com.carterz30cal.items.abilities.AbilityManager;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mining.TaskMining;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.ListenerChunkUnload;
import com.carterz30cal.mobs.ListenerDismountEvent;
import com.carterz30cal.npcs.NPCManager;
import com.carterz30cal.packets.Packetz;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.ListenerBlockEvents;
import com.carterz30cal.player.ListenerEntityDamage;
import com.carterz30cal.player.VoteListener;
import com.carterz30cal.player.skilltree.AbsSkill;
import com.carterz30cal.quests.Quest;
import com.carterz30cal.quests.TutorialManager;
import com.carterz30cal.tasks.TaskBlockReplace;
import com.carterz30cal.tasks.TaskSpawn;
import com.carterz30cal.utility.RandomFunctions;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;

import net.md_5.bungee.api.ChatColor;
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
	
	
	private Map<Player,Integer> discordprompts = new HashMap<>();
	
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
		new WaterwayBoss();
		new WaterwaySandMiniboss();
		new WaterwayTutorial();
		new NecropolisCrypts2();
		new InfestedHunter();
		new NecropolisBoss();
		
		RandomFunctions.r = new Random();
		//-69, 100, 20994
		
		blocks = new HashMap<Block,TaskBlockReplace>();
		loadOnline();
		
		display.runTaskTimer(this, 0, 5);
		regen.runTaskTimer(this, 0, 40);
		new TaskSpawn().runTaskTimer(Dungeons.instance, 0, 260);
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(damager, this);
		pm.registerEvents(join, this);
		pm.registerEvents(listenerGUI, this);
		pm.registerEvents(listenerChunk, this);
		pm.registerEvents(listenerDismount, this);
		pm.registerEvents(listenerBlock, this);
		pm.registerEvents(new VoteListener(), this);
		
		getCommand("dungeons").setExecutor(new CommandDungeons());
		getCommand("warp").setExecutor(new CommandHub());
		getCommand("leaderboard").setExecutor(new CommandLeaderboard());
		getCommand("trade").setExecutor(new CommandTrade());
		getCommand("market").setExecutor(new CommandMarket());
		getCommand("stats").setExecutor(new CommandStats());
		getCommand("playtime").setExecutor(new CommandPlaytime());
		getCommand("discord").setExecutor(new CommandDiscord());
		getCommand("maxitem").setExecutor(new CommandMaxItem());
		getCommand("tutorial").setExecutor(new CommandTutorial());
		getCommand("vote").setExecutor(new CommandVote());
		
		NPCManager.sendall();
		
		Quest.init();
		Packetz.init();
		MarketGUI.init();
		TutorialManager.init();
		AbsSkill.init();
		
		new BukkitRunnable()
		{

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) 
				{
					if (discordprompts.getOrDefault(p, 0) > 1) continue;
					p.sendMessage(ChatColor.GOLD + "Consider joining the discord! https://discord.gg/U4WsVRG");
					discordprompts.put(p, discordprompts.getOrDefault(p, 0) + 1);
				}
			}
			
		}.runTaskTimer(this, 20*60*30, 20*60*30);
		
		/*
		int[] randomTest = new int[6];
		int tests = 1000000;
		for (int i = 0; i < tests;i++) randomTest[RandomFunctions.random(0, 5)]++;
		for (int r : randomTest) System.out.println(r  + " - " + ((double)r/tests) * 100 + "%");
		*/
		
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event){
                PacketContainer packet = event.getPacket();
                
                EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                DungeonsPlayer d = DungeonsPlayerManager.i.get(event.getPlayer());
                
                if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK && d.mining == null)
                {
                	new BukkitRunnable()
                	{
                		public void run()
                		{
                			Block target = d.player.getTargetBlockExact(5);
                        	if (target == null) return;
                    		if (d.area.mining.blocks.containsKey(target.getType())) new TaskMining(d,target);
                		}
                	}.runTaskLater(Dungeons.instance, 2);
                }
                else if (digType == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK && d.mining != null)
                {
                	d.mining.end();
            		d.mining = null;
                }
                
            }
        });
		
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
		for (Player p : Bukkit.getOnlinePlayers()) 
		{
			DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
			for (AbsAbility a : d.stats.abilities) a.onEnd(d);
			p.closeInventory();
		}
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
