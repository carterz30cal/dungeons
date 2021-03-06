package com.carterz30cal.player;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.carterz30cal.areas.AbsDungeonEvent;
import com.carterz30cal.areas.EventTicker;
import com.carterz30cal.crypts.CryptGenerator;
import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.gui.GUI;

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
	
	public HashMap<String,String> questProgress;
	
	public boolean inCrypt;
	public CryptGenerator crypt;
	
	public boolean canOpen;
	// data for rewards
	public Date lastLogin; // if null then this is the first login and the beginners stuff should be given.
	
	public int questcooldown;
	
	private int manacooldown;

	public DungeonsPlayer(Player p)
	{
		FileConfiguration i = Dungeons.instance.getPlayerConfig();
		UUID u = p.getUniqueId();
		player = p;
		
		level = new CharacterSkill(p);
		
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
		
		questProgress = new HashMap<String,String>();
		if (i.contains(u + ".quests"))
		{
			for (String slo : i.getConfigurationSection(u + ".quests").getKeys(false))
			{
				questProgress.put(slo, i.getString(u + ".quests." + slo));
			}
		}
		
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
		
		//skills.d = this;
		
		crypt = null;
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
		
		health -= (double)damage/stats.health;
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
		mana = Math.max(0, Math.min(1, mana + (amount/stats.mana)));
		showManaLevel();
	}
	public int getMana() {return (int)(stats.mana*mana);}
	public double getManaPercent() {return mana;}
	public int getHealth() {return (int) (stats.health*health);}
	public double getHealthPercent() {return health;}
	
}
