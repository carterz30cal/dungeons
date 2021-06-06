package com.carterz30cal.player;



import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.carterz30cal.areas.AbsDungeonEvent;
import com.carterz30cal.areas.EventTicker;
import com.carterz30cal.crypts.CryptGenerator;
import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.gui.GUI;
import com.carterz30cal.mining.TaskMining;
import com.carterz30cal.packets.Packetz;
import com.carterz30cal.utility.Square;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class DungeonsPlayer
{
	public Player player;
	private double health;
	private double mana;
	public int coins;
	
	// the new age stuffs
	public CharacterSkill level;
	
	//public DungeonsPlayerSkills skills;
	public DungeonsPlayerStats stats;
	public DungeonsPlayerDisplay display;
	public DungeonsPlayerExplorer explorer;
	public Dungeon area;
	
	public GUI gui;

	public ArrayList<BackpackItem[]> backpackb;
	public Map<String,Integer> sack;
	
	public HashMap<String,String> questProgress;
	public Map<String,Integer> skills;
	public List<String> tutorials = new ArrayList<>();
	
	public boolean inCrypt;
	public CryptGenerator crypt;
	
	public boolean canOpen;
	// data for rewards
	
	
	
	public int afk;
	
	public Instant voteBoost;
	
	public Square restriction; // creative only.
	
	public int questcooldown;
	
	private int manacooldown;
	
	public int playtime;
	public boolean newaccount; // only True the first login.
	public PlayerRank rank;
	
	public int kills;
	
	public static ChatColor[] rankColours = {ChatColor.WHITE,ChatColor.GREEN,ChatColor.RED,ChatColor.LIGHT_PURPLE};
	
	public TaskMining mining;

	@SuppressWarnings("unchecked")
	public DungeonsPlayer(Player p)
	{
		FileConfiguration i = Dungeons.instance.getPlayerConfig();
		UUID u = p.getUniqueId();
		player = p;
		
		skills = new HashMap<>();
		if (i.contains(u + ".skilltree"))
		{
			for (String slo : i.getConfigurationSection(u + ".skilltree").getKeys(false))
			{
				skills.put(slo, i.getInt(u + ".skilltree." + slo));
			}
		}
		level = new CharacterSkill(this);
		
		//skills = new DungeonsPlayerSkills(p);
		stats = new DungeonsPlayerStats(p);
		explorer = new DungeonsPlayerExplorer(p);
		//stats.refresh();
		health = 1;
		mana = 0.99;
		area = DungeonManager.i.hub;
		canOpen = true;
		manacooldown = 0;
		display = new DungeonsPlayerDisplay(this);
		gui = null;
		
		kills = i.getInt(u + ".kills", 0);
		questProgress = new HashMap<>();
		if (i.contains(u + ".quests"))
		{
			for (String slo : i.getConfigurationSection(u + ".quests").getKeys(false))
			{
				questProgress.put(slo, i.getString(u + ".quests." + slo));
			}
		}
		sack = new HashMap<>();
		if (i.contains(u + ".sack"))
		{
			for (String slo : i.getConfigurationSection(u + ".sack").getKeys(false))
			{
				sack.put(slo, i.getInt(u + ".sack." + slo));
			}
		}
		
		voteBoost = Instant.ofEpochMilli(i.getLong(u + ".voteboost",0));
		if (voteBoost.isBefore(Instant.now())) 
		{
			voteBoost = null;
			DungeonsPlayer ins = this;
			new BukkitRunnable()
			{
				
				@Override
				public void run() {
					if (ins.newaccount) return;
					ins.player.sendMessage(ChatColor.AQUA + "You can get a 35% xp boost for 6 hours per /vote");
					ins.player.sendMessage(ChatColor.AQUA + "and it really helps the server out :)");
				}
				
			}.runTaskLater(Dungeons.instance, 200);
		}
		
		tutorials = (List<String>) i.getList(u + ".tutorials",new ArrayList<>());
		if (!tutorials.contains("none")) tutorials.add("none");
		playtime = i.getInt(u + ".playtime", 0);
		
		backpackb = new ArrayList<BackpackItem[]>();
		if (i.contains(u + ".backpack"))
		{
			for (String slo : i.getConfigurationSection(u + ".backpack").getKeys(false))
			{
				BackpackItem[] page = new BackpackItem[45];
				for (int k = 0; k < 45; k++)
				{
					if (i.contains(u + ".backpack." + slo + "." + k)) page[k] = new BackpackItem(u + ".backpack." + slo + "." + k,k);
				}
				backpackb.add(page);
			}
		}
		
		if (backpackb.size() == 0) backpackb.add(new BackpackItem[45]);
		coins = Dungeons.instance.getPlayerConfig().getInt(p.getUniqueId() + ".coins", 10);
		
		
		rank = PlayerRank.values()[i.getInt(u + ".rank",0)];
		updateRank();
		//skills.d = this;
		
		crypt = null;
	}
	public boolean canWarp(String location)
	{
		Dungeon loc = DungeonManager.i.warps.getOrDefault(location, DungeonManager.i.hub);
		
		if (inCrypt) return false;
		else if (loc.requiredtutorial.equals("none") || tutorials.contains(loc.requiredtutorial))
		{
			return true;
		}
		return false;
	}
	public void warp(String location)
	{
		Dungeon loc = DungeonManager.i.warps.getOrDefault(location, DungeonManager.i.hub);
		
		if (inCrypt) player.sendMessage(ChatColor.RED + "You cannot warp whilst in a crypt.");
		else if (canWarp(location)) player.teleport(loc.spawn, TeleportCause.PLUGIN);
	}
	
	
	public void updateRank()
	{
		player.setDisplayName(rankColours[rank.ordinal()] + player.getName() + ChatColor.RESET);
	}
	
	public void setHealth(double amount)
	{
		amount = Math.min(1,Math.max(0, amount));
		health = amount;
		player.setHealth(Math.max(1, health*20));
	}
	public void damage(int amount,boolean penetrating)
	{
		int damage = amount;
		if (player.getGameMode() != GameMode.SURVIVAL) damage = 0;
		if (!penetrating) 
		{
			double dr = 1.0 - ((double)stats.armour / (stats.armour+100));
			damage = (int)Math.round((double)damage * dr);
		}
		
		health -= (double)damage/(double)stats.health;
		player.setHealth(Math.max(1, health*20));
		if (health <= 0)
		{
			crypt = null;
			for (AbsDungeonEvent e : EventTicker.events) e.onPlayerDeath(this);
			Location sp = DungeonManager.i.dungeons.getOrDefault(DungeonManager.i.hash(player.getLocation().getBlockZ()),
					DungeonManager.i.hub).spawn;
			sp.setPitch(player.getLocation().getPitch());
			sp.setYaw(player.getLocation().getYaw());
			player.setVelocity(new Vector(0,0,0));
			player.teleport(sp,TeleportCause.PLUGIN);
			new SoundTask(player.getLocation(),player,Sound.ENTITY_PLAYER_DEATH,2.5f,1f).runTaskLater(Dungeons.instance, 5);
			health = 1;
			player.setHealth(20);
			for (PotionEffect pot : player.getActivePotionEffects())
			{
				player.removePotionEffect(pot.getType());
			}
			player.sendMessage(ChatColor.RED + "You died!");
			player.setFallDistance(0);
			player.setFireTicks(0);
			player.closeInventory();
			
			/*
			for (EntitySkinned skinned : EntitySkinned.alive.values())
			{
				PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn(skinned);
				PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(skinned.navigator.getEntityId());
				
				PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
				connection.sendPacket(namedEntitySpawn);
				connection.sendPacket(destroy);
			}
			*/
		}
	}
	
	public void lightning(int damage)
	{
		damage(damage,true);
		Packetz.lightningAllows.put(this, Packetz.lightningAllows.getOrDefault(this, 0)+1);
		Dungeons.w.strikeLightningEffect(player.getLocation());
		
		CraftPlayer c = (CraftPlayer)player;
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (c.getHandle(),(byte)2);
		for (Player k : Bukkit.getOnlinePlayers())
		{
			c = (CraftPlayer)k;
			c.getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	
	
	public void heal(int amount)
	{
		health += (double)amount/(double)stats.health;
		if (health > 1) health = 1;

		player.setHealth(Math.max(1, health*20));
	}
	public void heal(double amount)
	{
		health += amount;
		if (health > 1) health = 1;

		player.setHealth(Math.max(1, health*20));
	}
	public boolean useMana(int amount)
	{
		int m = getMana();
		if (m >= amount) 
		{
			mana -= amount/(double)stats.mana;
			manacooldown = 40;
		}
		showManaLevel();
		return m >= amount;
	}
	public boolean playerHasMana() 
	{
		return stats.mana > 0;
	}
	public void showManaLevel()
	{
		mana = Math.min(mana, 1);
		mana = Math.max(0, mana);
		if (stats.mana == 0) player.setExp(0);
		else player.setExp((float) mana);
		
		player.setLevel(0);
	}
	public void regainMana(double amount)
	{
		if (manacooldown == 0)
		{
			mana = Math.max(0, Math.min(1, mana + amount));
			showManaLevel();
		}
		else manacooldown--;
	}
	public void gainMana(int amount)
	{
		mana = Math.max(0, Math.min(1, mana + ((double)amount/(double)stats.mana)));
		showManaLevel();
	}
	public int getMana() {return (int)(stats.mana*mana);}
	public double getManaPercent() {return mana;}
	public int getHealth() {return (int) (stats.health*health);}
	public double getHealthPercent() {return health;}
	
}
