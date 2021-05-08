package com.carterz30cal.areas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.crypts.CryptBlocks;
import com.carterz30cal.crypts.CryptGenerator;
import com.carterz30cal.crypts.CryptLoot;
import com.carterz30cal.crypts.CryptLootTable;
import com.carterz30cal.crypts.CryptMobs;
import com.carterz30cal.crypts.CryptRoomType;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.ArmourstandFunctions;
import com.carterz30cal.utility.Stats;

import net.md_5.bungee.api.ChatColor;

public class NecropolisCrypts2 extends AbsDungeonEvent
{
	public ArmorStand display_name;
	public ArmorStand display_description;
	public ArmorStand display_warning;
	
	public int openticks;
	public int dooroffset;
	public boolean started;
	public int difficulty;
	
	public Map<DungeonsPlayer,CryptGenerator> crypts = new HashMap<>();
	
	public DungeonsPlayer cryptOwner;
	
	public CryptGenerator crypt;
	public CryptLootTable loot;
	public CryptBlocks blocks;
	public CryptMobs mobs;
	
	public CryptMobs mobs2;
	public CryptLootTable loot2;
	
	private final String[] mobs_regular = {"crypts_zombie","crypts_zombie","crypts_zombiebaby","crypts_skeleton","crypts_skeleton"};
	private final String[] mobs_tough = {"crypts_zombie","crypts_zombie","crypts_zombie","crypts_zombie","crypts_skeleton","crypts_skeleton","crypts_mage","crypts_skeletoncaptain"};
	private final String[] mobs_nest = {"crypts_spider","crypts_spider","crypts_smallspider"};
	private final String[] mobs_rune = {"crypts_runemage","crypts_runeghoul"};
	private final String[] mobs_wet = {"ghoul2"};
	private final String[] mobs_miniboss = {"crypts_miniboss_slime1","crypts_miniboss_husk1","crypts_miniboss_knight1","crypts_miniboss_hunter1"};
	
	private final String[] mobs_regular2 = {"crypts_zombie2","crypts_zombie2","crypts_skeleton2","crypts_skeleton2","crypts_skeletoncaptain"};
	private final String[] mobs_tough2 = {"crypts_zombie2","crypts_hulk","crypts_mage2","crypts_skeletoncaptain","crypts_skeletoncommander"};
	private final String[] mobs_wet2 = {"crypts_hulk"};
	
	
	
	private CryptMobs mobs_ancient;
	private CryptLootTable loot_ancient;
	private CryptBlocks blocks_ancient;
	private final String[] ancient_regular = {"cryptsa_ghoul","cryptsa_soldier"};
	private final String[] ancient_loot = {"cryptsa_ghoul","cryptsa_soldier","cryptsa_mage","cryptsa_assassin"};
	
	
	
	
	public NecropolisCrypts2()
	{
		super();
		
		loot = new CryptLootTable();
		loot.loot.add(new CryptLoot("bone",650));
		loot.loot.add(new CryptLoot("tissue",415));
		loot.loot.add(new CryptLoot("gel",150));
		loot.loot.add(new CryptLoot("stick_bone",15));
		loot.loot.add(new CryptLoot("stick_gel",4));
		loot.loot.add(new CryptLoot("decaying_flesh",16));
		loot.loot.add(new CryptLoot("crypt_dust",21));
		loot.loot.add(new CryptLoot("catalyst=0",5));
		loot.loot.add(new CryptLoot("book","polished,1",8));
		loot.loot.add(new CryptLoot("book","polished,2",1));
		loot.loot.add(new CryptLoot("book","strength,1",4));
		loot.loot.add(new CryptLoot("armour_crypt_helmet",2));
		loot.loot.add(new CryptLoot("armour_crypt_chestplate",1));
		loot.loot.add(new CryptLoot("armour_crypt_leggings",1));
		loot.loot.add(new CryptLoot("armour_crypt_boots",2));
		loot.loot.add(new CryptLoot("sword_cryptknight",1));
		loot.loot.add(new CryptLoot("crypt_stone",5));
		loot.add("bow_cryptspirit", 1);
		loot.itemsPerChest = new int[] {5,12};
		loot.init();
		
		loot2 = new CryptLootTable();
		loot2.loot.add(new CryptLoot("bone",2500));
		loot2.loot.add(new CryptLoot("gel",500));
		loot2.loot.add(new CryptLoot("crypt_dust",60));
		loot2.loot.add(new CryptLoot("decaying_flesh",45));
		loot2.loot.add(new CryptLoot("strange_tissue",11));
		loot2.loot.add(new CryptLoot("catalyst=0",19));
		loot2.loot.add(new CryptLoot("book","polished,1",20));
		loot2.loot.add(new CryptLoot("book","polished,2",8));
		loot2.loot.add(new CryptLoot("book","polished,3",2));
		loot2.loot.add(new CryptLoot("book","strength,1",16));
		loot2.loot.add(new CryptLoot("book","strength,2",6));
		loot2.loot.add(new CryptLoot("book","sweeping,1",2));
		loot2.loot.add(new CryptLoot("armour_crypt2_helmet",3));
		loot2.loot.add(new CryptLoot("armour_crypt2_chestplate",1));
		loot2.loot.add(new CryptLoot("armour_crypt2_leggings",2));
		loot2.loot.add(new CryptLoot("armour_crypt2_boots",4));
		loot2.loot.add(new CryptLoot("sword_cryptknight",2));
		loot2.loot.add(new CryptLoot("crypt_stone",6));
		loot2.add("bow_cryptspirit", 2);
		loot2.add("book", 1,"cryptlord,1");
		loot2.itemsPerChest = new int[] {3,14};
		loot2.init();
		
		blocks = new CryptBlocks();
		blocks.floor = new Material[]{Material.STONE,Material.COBBLESTONE};
		blocks.walls = new Material[]{Material.MOSSY_COBBLESTONE,Material.COBBLESTONE};
		blocks.roof = new Material[] {Material.STONE,Material.ANDESITE,Material.MOSSY_COBBLESTONE,Material.COBBLESTONE};
		blocks.roomroof = new Material[] {Material.GLOWSTONE,Material.STONE,Material.ANDESITE,Material.MOSSY_COBBLESTONE,Material.COBBLESTONE};
		blocks.path = Material.OAK_PLANKS;
		blocks.support = Material.OAK_FENCE;
		
		mobs = new CryptMobs();
		mobs.mobs.put(CryptRoomType.NORMAL,mobs_regular);
		mobs.mobs.put(CryptRoomType.LOOT,mobs_tough);
		mobs.mobs.put(CryptRoomType.NEST,mobs_nest);
		mobs.mobs.put(CryptRoomType.RUNE, mobs_rune);
		mobs.mobs.put(CryptRoomType.WET, mobs_wet);
		mobs.mobs.put(CryptRoomType.MINIBOSS,mobs_miniboss);
		
		mobs2 = new CryptMobs();
		mobs2.mobs.put(CryptRoomType.NORMAL,mobs_regular2);
		mobs2.mobs.put(CryptRoomType.LOOT,mobs_tough2);
		mobs2.mobs.put(CryptRoomType.NEST,mobs_nest);
		mobs2.mobs.put(CryptRoomType.RUNE, mobs_rune);
		mobs2.mobs.put(CryptRoomType.WET, mobs_wet2);
		mobs2.mobs.put(CryptRoomType.MINIBOSS,mobs_miniboss);
		
		
		blocks_ancient = new CryptBlocks();
		blocks_ancient.floor = new Material[] {Material.GRANITE,Material.POLISHED_GRANITE,Material.NETHERRACK};
		blocks_ancient.walls = new Material[] {Material.GRANITE,Material.POLISHED_GRANITE,Material.NETHERRACK};
		blocks_ancient.roof = new Material[] {Material.GRANITE,Material.POLISHED_GRANITE,Material.NETHERRACK};
		blocks_ancient.roomroof = new Material[] {Material.GRANITE,Material.POLISHED_GRANITE,Material.NETHERRACK,Material.GLOWSTONE};
		blocks_ancient.path = Material.NETHER_BRICKS;
		blocks_ancient.support = Material.NETHER_BRICK_FENCE;
		
		mobs_ancient = new CryptMobs();
		mobs_ancient.mobs.put(CryptRoomType.NORMAL  ,ancient_regular);
		mobs_ancient.mobs.put(CryptRoomType.LOOT    ,ancient_loot);
		mobs_ancient.mobs.put(CryptRoomType.NEST	,ancient_regular);
		mobs_ancient.mobs.put(CryptRoomType.RUNE	,mobs_rune);
		mobs_ancient.mobs.put(CryptRoomType.WET	  	,ancient_regular);
		mobs_ancient.mobs.put(CryptRoomType.MINIBOSS,mobs_miniboss);
		
		loot_ancient = new CryptLootTable();
		loot_ancient.itemsPerChest = new int[] {10,21};
		loot_ancient.add("digging_stone", 2000);
		loot_ancient.add("digging_ancientstone", 50);
		loot_ancient.add("strange_tissue",10);
		loot_ancient.add("book", 6,"cryptwarrior,1");
		loot_ancient.add("book", 3,"shredding,1");
		loot_ancient.add("bow_stinger", 2);
		loot_ancient.add("book", 4,"cryptlord,1");
		loot_ancient.init();
		
	}
	@Override
	public void tick()
	{
		for (Entry<DungeonsPlayer,CryptGenerator> e : crypts.entrySet())
		{
			CryptGenerator cry = e.getValue();
			if (cry.started) cry.spawnNearby(e.getKey());
			else if (cry.ready)
			{
				e.getKey().player.teleport(cry.spawn.add(0,3,0));
				cry.started = true;
			}
			else cry.step();
		}
		
		if (display_name == null || !display_name.isValid()) 
		{
			display_name = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 102.4, 22019.5));
			display_description = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 102, 22019.5));
			display_warning = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 101.6, 22019.5));
		}
		display_name.setCustomName(ChatColor.GOLD + "" +  ChatColor.BOLD + "CRYPTS");
		display_description.setCustomName(ChatColor.GOLD + "Insert a key to begin!");
		display_warning.setCustomName(ChatColor.RED + "Difficult!");
		
		/*
		if (crypt != null && crypt.complete && started)
		{
			crypt.spawnNearby(cryptOwner);
			if (openticks == 0)
			{
				cryptOwner.player.sendMessage(ChatColor.GOLD + "You cleared every room!");
				cryptOwner.player.sendMessage(ChatColor.GOLD + "Crypt will end in 20 seconds.");
			}
			else if (openticks == 20*20)
			{
				cryptOwner.player.sendMessage(ChatColor.GOLD + "Warping..");
				cryptOwner.player.teleport(new Location(Dungeons.w,27,101,22019));
				removeCrypt();
			}
			openticks++;
		}
		else if (cryptOwner == null || !cryptOwner.player.isValid() || !Bukkit.getOnlinePlayers().contains(cryptOwner.player))
		{
			
			if (openticks > 0) resetDoor();
			removeCrypt();
			if (display_name == null || !display_name.isValid()) 
			{
				display_name = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 102.4, 22019.5));
				display_description = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 102, 22019.5));
				display_warning = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 101.6, 22019.5));
			}
			display_name.setCustomName(ChatColor.GOLD + "" +  ChatColor.BOLD + "CRYPTS");
			display_description.setCustomName(ChatColor.GOLD + "Insert a key to begin!");
			display_warning.setCustomName(ChatColor.RED + "Difficult!");
			
			started = false;
			openticks = 0;
		}
		else
		{
			cryptOwner.canOpen = true;
			if (DungeonManager.i.hash(cryptOwner.player.getLocation().getBlockZ()) != 2) 
			{
				removeCrypt();
			}
			//30,104,22018 is top corner // door is +z -y from there.
			// we will start from 30,101,22018
			if (started && crypt != null) crypt.spawnNearby(cryptOwner);
			if (openticks % 20 == 0 && openticks < 80 && !started) moveDoorDown();
			
			if (display_name.isValid())
			{
				display_name.remove();
				display_description.remove();
				display_warning.remove();
			}
			if (!started) openticks++;
			else openticks = 0;
		}
		*/
	}
	
	public void startCrypt(DungeonsPlayer player,int difficulty)
	{
		CryptGenerator starting;
		
		int x = 107 + (80 * crypts.size());
		if (difficulty == 1) starting = new CryptGenerator(x, 40, 22002,65,65,5,8,11,4,loot,mobs,blocks,30);
		else if (difficulty == 2) starting = new CryptGenerator(x, 40, 22002,65,65,6,9,11,3,loot2,mobs2,blocks,30);
		else if (difficulty == 4) starting = new CryptGenerator(x,40,22002,65,65,6,9,11,3,loot_ancient,mobs_ancient,blocks_ancient,10);
		else starting = null;
		
		player.inCrypt = true;
		player.crypt = starting;
		//cryptOwner.player.teleport(starting.spawn.add(0, 3, 0));
		crypts.put(player,starting);
	}
	
	public void endCrypt(DungeonsPlayer player)
	{
		CryptGenerator c = crypts.get(player);
		c.remove();
		
		player.inCrypt = false;
		player.canOpen = true;
		player.crypt = null;
		
		crypts.remove(player);
	}
	
	
	
	@Override
	public void onPlayerDeath(DungeonsPlayer died)
	{
		if (crypts.containsKey(died)) endCrypt(died);
	}
	
	
	@Override
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (!e.hasItem() || e.getItem() == null || !e.getItem().hasItemMeta()) return false;
		
		String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"");
		boolean key = it.equals("crypt_key1") || it.equals("crypt_key2") || it.equals("crypt_key4");
		if (key && DungeonManager.i.hash(e.getPlayer().getLocation().getBlockZ()) == 2
				&& e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.LODESTONE
				&& !crypts.containsKey(DungeonsPlayerManager.i.get(e.getPlayer())))
		{
			int dif = Integer.parseInt(it.substring(it.length()-1));
			startCrypt(DungeonsPlayerManager.i.get(e.getPlayer()),dif);
			e.getPlayer().teleport(new Location(Dungeons.w,32,101,22019,-90,0));
			
			e.getItem().setAmount(e.getItem().getAmount() - 1);
			Stats.cryptsopened++;
			return true;
		}
		return false;
	}
	@Override
	public void end()
	{
		display_name.remove();
		display_description.remove();
		display_warning.remove();
		
		for (DungeonsPlayer p : crypts.keySet()) endCrypt(p);
	}
}
