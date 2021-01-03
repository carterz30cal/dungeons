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
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

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
	public DungeonsPlayerSkills skills;
	public DungeonsPlayerStats stats;
	public DungeonsPlayerDisplay display;
	public DungeonsPlayerPerks perks;
	public DungeonsPlayerExplorer explorer;
	public Dungeon area;
	
	public GUI gui;

	public ArrayList<BackpackItem[]> backpackb;
	
	public HashMap<String,String> settings;
	public int settingSkillsDisplay;
	public int perkBackground;
	public boolean colourblindMode;
	public boolean highlightRenamed;
	
	// data for rewards
	public Date lastLogin; // if null then this is the first login and the beginners stuff should be given.
	@SuppressWarnings("deprecation")
	public DungeonsPlayer(Player p)
	{
		FileConfiguration i = Dungeons.instance.getPlayerConfig();
		UUID u = p.getUniqueId();
		player = p;
		skills = new DungeonsPlayerSkills(p);
		stats = new DungeonsPlayerStats(p);
		perks = new DungeonsPlayerPerks(p);
		explorer = new DungeonsPlayerExplorer(p);
		//stats.refresh();
		health = 1;
		mana = 1;
		area = DungeonManager.i.hub;
		
		display = new DungeonsPlayerDisplay(this);
		gui = null;
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
		
		String[] date = i.getString(p.getUniqueId() + ".lastlogin","").split("/");
		if (date.length == 1) lastLogin = null;
		else lastLogin = new Date(Integer.parseInt(date[2])-1900,Integer.parseInt(date[1]),Integer.parseInt(date[0]));
		
		settings = new HashMap<String,String>();
		settingSkillsDisplay = Dungeons.instance.getPlayerConfig().getInt(p.getUniqueId() + ".settings.progressbar", 0);
		perkBackground = Dungeons.instance.getPlayerConfig().getInt(p.getUniqueId() + ".settings.perkbackground", 0);
		colourblindMode = Dungeons.instance.getPlayerConfig().getBoolean(p.getUniqueId() + ".settings.colourblind", false);
		highlightRenamed = i.getBoolean(p.getUniqueId() + ".settings.highlightrenamed",false);
		
		skills.d = this;
	}
	@SuppressWarnings("deprecation")
	public boolean rewardEligible()
	{
		Date date = new Date();
		date.setMinutes(0);
		date.setSeconds(0);
		date.setHours(0);
		return lastLogin == null || lastLogin.before(date);
	}
	public void damage(int amount,boolean penetrating)
	{
		int damage = amount;
		if (player.getGameMode() == GameMode.CREATIVE) damage = 0;
		if (!penetrating) 
		{
			double dr = 1.0 - ((double)stats.armour / (stats.armour+100));
			damage = (int)Math.round((double)damage * dr);
		}
		
		health -= (double)damage/stats.health;
		player.setHealth(Math.max(1, health*20));
		if (health <= 0)
		{
			Location sp = DungeonManager.i.dungeons.getOrDefault(DungeonManager.i.hash(player.getLocation().getBlockZ()),
					DungeonManager.i.hub).spawn;
			sp.setPitch(player.getLocation().getPitch());
			sp.setYaw(player.getLocation().getYaw());
			player.setVelocity(new Vector(0,0,0));
			player.teleport(sp);
			new SoundTask(player.getLocation(),player,Sound.BLOCK_BEACON_DEACTIVATE,2.5f,1f).runTaskLater(Dungeons.instance, 5);
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
		if (m >= amount) mana -= amount/(double)stats.mana;
		showManaLevel();
		return m >= amount;
	}
	public boolean playerHasMana() 
	{
		return stats.mana > 0;
	}
	public void showManaLevel()
	{
		if (stats.mana == 0) player.setExp(0);
		else player.setExp((float) mana);
		
		player.setLevel(0);
	}
	public void regainMana(double amount)
	{
		mana = Math.max(0, Math.min(1, mana + amount));
		showManaLevel();
	}
	public int getMana() {return (int)(stats.mana*mana);}
	public double getManaPercent() {return mana;}
	public int getHealth() {return (int) (stats.health*health);}
	public double getHealthPercent() {return health;}
	
}
