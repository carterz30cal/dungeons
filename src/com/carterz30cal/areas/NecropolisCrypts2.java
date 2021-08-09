package com.carterz30cal.areas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.crypts.*;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
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
	
	public CryptMobs mobs3;
	public CryptLootTable loot3;
	public CryptBlocks blocks3;
	
	private final String[] mobs_regular = {"crypts_zombie","crypts_zombie","crypts_zombiebaby","crypts_skeleton","crypts_skeleton"};
	private final String[] mobs_tough = {"crypts_zombie","crypts_zombie","crypts_zombie","crypts_zombie","crypts_skeleton","crypts_skeleton","crypts_mage","crypts_skeletoncaptain"};
	private final String[] mobs_nest = {"crypts_spider","crypts_spider","crypts_smallspider"};
	private final String[] mobs_rune = {"crypts_runemage","crypts_runeghoul"};
	private final String[] mobs_wet = {"ghoul2"};
	private final String[] mobs_miniboss = {"crypts_boss_slime1","crypts_boss_husk1","crypts_boss_knight1","crypts_boss_hunter1"};
	
	private final String[] mobs_regular2 = {"crypts_zombie2","crypts_zombie2","crypts_skeleton2","crypts_skeleton2","crypts_skeletoncaptain"};
	private final String[] mobs_tough2 = {"crypts_zombie2","crypts_hulk","crypts_mage2","crypts_skeletoncaptain","crypts_skeletoncommander","crypts_lord"};
	private final String[] mobs_wet2 = {"crypts_hulk"};
	
	
	
	private CryptMobs mobs_ancient;
	private CryptLootTable loot_ancient;
	private CryptBlocks blocks_ancient;
	private final String[] ancient_regular = {"cryptsa_ghoul","cryptsa_soldier"};
	private final String[] ancient_loot = {"cryptsa_ghoul","cryptsa_soldier","cryptsa_mage","cryptsa_assassin"};
	
	private int shift = 0;
	
	private CryptSettings[] difficulties = new CryptSettings[4];

	
	
	public NecropolisCrypts2()
	{
		super();
		
		loot = new CryptLootTable();
		loot.add("bone",650);
		loot.add("tissue",415);
		loot.add("gel",175);
		loot.add("stick_bone",15);
		loot.add("decaying_flesh",16);
		loot.add("crypt_dust",21);
		loot.add("catalyst=0",5);
		loot.add("book",8,"polished,1");
		loot.add("book",1,"polished,2");
		loot.add("book",4,"strength,1");
		loot.add("armour_crypt_helmet",2);
		loot.add("armour_crypt_chestplate",1);
		loot.add("armour_crypt_leggings",1);
		loot.add("armour_crypt_boots",2);
		loot.add("sword_cryptknight",1);
		loot.add("crypt_stone",5);
		loot.add("bow_cryptspirit", 1);
		loot.add("magic_cactus", 5);
		loot.itemsPerChest = new int[] {6,12};
		
		loot2 = new CryptLootTable();
		loot2.add("bone",2350);
		loot2.add("gel",550);
		loot2.add("crypt_dust",60);
		loot2.add("decaying_flesh",45);
		loot2.add("strange_tissue",11);
		loot2.add("catalyst=0",19);
		loot2.add("book",20,"polished,1");
		loot2.add("book",8,"polished,2");
		loot2.add("book",2,"polished,3");
		loot2.add("book",16,"strength,1");
		loot2.add("book",6,"strength,2");
		loot2.add("book",4,"sweeping,1");
		loot2.add("polish_jar", 5);
		loot2.add("sword_cryptknight",2);
		loot2.add("crypt_stone",6);
		loot2.add("bow_cryptspirit", 2);
		loot2.add("book", 14,"cryptlord,1");
		loot2.add("magic_cactus", 12);
		loot2.add("tissue",110);
		loot2.itemsPerChest = new int[] {4,15};
		
		
		
		loot3 = new CryptLootTable();
		loot3.add("gel",900);
		loot3.add("bone", 230);
		loot3.add("crypt_dust",60);
		loot3.add("decaying_flesh",160);
		loot3.add("catalyst=0",16);
		loot3.add("book",14,"polished,2");
		loot3.add("book",5,"polished,3");
		loot3.add("book",2,"polished,4");
		loot3.add("book",9,"strength,2");
		loot3.add("book",3,"strength,3");
		loot3.add("book",6,"sweeping,1");
		loot3.add("book",2,"sweeping,2");
		loot3.add("book",6,"cryptlord,2");
		loot3.add("book",1,"cryptlord,3");
		loot3.add("alchemical_note4", 4);
		loot3.add("potions_cryptroot", 15);
		loot3.add("sacrifice",3);
		loot3.add("magic_cactus", 9);

		loot3.itemsPerChest = new int[] {8,21};
		
		blocks3 = new CryptBlocks();
		blocks3.floor = new Material[]{Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA,Material.BLUE_TERRACOTTA};
		blocks3.walls = new Material[]{Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA,Material.BLUE_TERRACOTTA};
		blocks3.roof = new Material[] {Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA,Material.BLUE_TERRACOTTA};
		blocks3.roomroof = new Material[] {Material.GLOWSTONE,Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA,Material.BLUE_TERRACOTTA};
		blocks3.path = Material.SPRUCE_PLANKS;
		blocks3.support = Material.SPRUCE_FENCE;
		
		mobs3 = new CryptMobs();
		mobs3.mobs.put(CryptRoomType.NORMAL,new String[] {"crypt_zombie3","crypt_spirit3"});
		mobs3.mobs.put(CryptRoomType.LOOT,new String[] {"crypt_zombie3","crypt_spirit3","crypt_invis3","crypt_archer3"});
		mobs3.mobs.put(CryptRoomType.NEST,new String[] {"crypt_spider3"});
		mobs3.mobs.put(CryptRoomType.RUNE, mobs_rune);
		mobs3.mobs.put(CryptRoomType.WET, mobs_wet2);
		mobs3.mobs.put(CryptRoomType.MINIBOSS,new String[] {"crypts_boss_king3"});
		
		
		
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
		mobs2.mobs.put(CryptRoomType.MINIBOSS,new String[]{"crypts_boss_slime2","crypts_boss_husk2","crypts_boss_hunter2"});
		
		
		
		
		blocks_ancient = new CryptBlocks();
		blocks_ancient.floor = new Material[] {Material.GRANITE,Material.POLISHED_GRANITE,Material.NETHERRACK};
		blocks_ancient.walls = new Material[] {Material.GRANITE,Material.POLISHED_GRANITE,Material.NETHERRACK};
		blocks_ancient.roof = new Material[] {Material.GRANITE,Material.POLISHED_GRANITE,Material.NETHERRACK};
		blocks_ancient.roomroof = new Material[] {Material.GRANITE,Material.POLISHED_GRANITE,Material.NETHERRACK,Material.GLOWSTONE};
		blocks_ancient.path = Material.RED_NETHER_BRICKS;
		blocks_ancient.support = Material.NETHER_BRICK_FENCE;
		
		mobs_ancient = new CryptMobs();
		mobs_ancient.mobs.put(CryptRoomType.NORMAL  ,ancient_regular);
		mobs_ancient.mobs.put(CryptRoomType.LOOT    ,ancient_loot);
		mobs_ancient.mobs.put(CryptRoomType.NEST	,ancient_regular);
		mobs_ancient.mobs.put(CryptRoomType.RUNE	,mobs_rune);
		mobs_ancient.mobs.put(CryptRoomType.WET	  	,ancient_regular);
		mobs_ancient.mobs.put(CryptRoomType.MINIBOSS,mobs_miniboss);
		
		loot_ancient = new CryptLootTable();
		loot_ancient.itemsPerChest = new int[] {5,16};
		loot_ancient.add("digging_coal", 1000);
		loot_ancient.add("digging_stone2", 50);
		loot_ancient.add("digging_diamond", 8);
		loot_ancient.add("catalyst=0", 25);
		loot_ancient.add("book", 6,"cryptwarrior,1");
		loot_ancient.add("book", 3,"shredding,1");
		loot_ancient.add("book", 4,"sweeping,1");
		loot_ancient.add("bow_stinger", 2);
		loot_ancient.add("book", 5,"cryptlord,1");
		
		CryptSettings difficulty1 = new CryptSettings();
		difficulty1.init(loot, mobs, blocks);
		difficulty1.size(65,65);
		difficulty1.position(22002, 40);
		difficulty1.room(11, 5, 8, 4);
		difficulty1.deadends = 40;
		difficulties[0] = difficulty1;
		
		// ornate key
		CryptSettings difficulty2 = new CryptSettings();
		difficulty2.init(loot2, mobs2, blocks);
		difficulty2.size(70,70);
		difficulty2.position(22002, 40);
		difficulty2.room(13, 5, 7, 3);
		difficulty2.deadends = 40;
		difficulties[1] = difficulty2;
		
		// exalted key
		CryptSettings difficulty3 = new CryptSettings();
		difficulty3.init(loot3, mobs3, blocks3);
		difficulty3.size(75,75);
		difficulty3.position(22002, 40);
		difficulty3.room(17, 6, 8, 3);
		difficulty3.deadends = 80;
		difficulties[2] = difficulty3;
		
		// ancient key, actually less difficult than difficulty 3 but shush
		CryptSettings difficulty4 = new CryptSettings();
		difficulty4.init(loot_ancient, mobs_ancient, blocks_ancient);
		difficulty4.size(40,40);
		difficulty4.position(22002, 40);
		difficulty4.room(6, 5, 7, 4);
		difficulty4.deadends = 15;
		difficulty4.canHaveRune = false;
		difficulties[3] = difficulty4;
		
		
		
		display_name = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 102.4, 22019.5));
		display_description = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 102, 22019.5));
		display_warning = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 101.6, 22019.5));

		display_name.setCustomName(ChatColor.GOLD + "" +  ChatColor.BOLD + "CRYPTS");
		display_description.setCustomName(ChatColor.GOLD + "Insert a key to begin!");
		display_warning.setCustomName(ChatColor.RED + "Beware!");
		
	}
	@Override
	public void tick()
	{
		for (Entry<DungeonsPlayer,CryptGenerator> e : crypts.entrySet())
		{
			CryptGenerator cry = e.getValue();
			DungeonsPlayer player = e.getKey();
			if (cry.started) cry.spawnNearby(player);
			else if (cry.ready)
			{
				player.player.teleport(cry.spawn.add(0,3,0));
				if (cry.variant != null) 
				{
					player.player.sendMessage(cry.variant.getMessage());
					new SoundTask(player.player.getLocation(), player.player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1).runTaskLater(Dungeons.instance, 10);
				}
				cry.started = true;
			}
			else cry.step();
		}
		
		
		
	}
	
	public void startCrypt(DungeonsPlayer player,int difficulty)
	{
		CryptGenerator starting;
		
		
		int x = 107 + (80 * shift);
		shift++;
		if (shift >= 10) shift = 0;
		
		CryptSettings settings = difficulties[difficulty-1];
		starting = new CryptGenerator(x,difficulty,settings);
		
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
	public void endCryptNow(DungeonsPlayer player)
	{
		CryptGenerator c = crypts.get(player);
		c.endremove();
		
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
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"");
		boolean key = it.equals("crypt_key1") || it.equals("crypt_key2") || (it.equals("crypt_key3") && d.level.level() >= 20) ||it.equals("crypt_key4");
		if (key && DungeonManager.i.hash(e.getPlayer().getLocation().getBlockZ()) == 2
				&& e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.LODESTONE
				&& !crypts.containsKey(DungeonsPlayerManager.i.get(e.getPlayer())))
		{
			int dif = Integer.parseInt(it.substring(it.length()-1));
			startCrypt(d,dif);
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
		
		for (DungeonsPlayer p : crypts.keySet()) endCryptNow(p);
	}
}
