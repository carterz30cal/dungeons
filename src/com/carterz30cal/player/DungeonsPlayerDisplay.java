package com.carterz30cal.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class DungeonsPlayerDisplay
{
	private DungeonsPlayer dp;
	
	private Scoreboard board;
	private Objective obje;
	
	private Team health;
	private Team mana;
	private Team armour;
	private Team area;
	private Team coins;
	private Team empty;
	public DungeonsPlayerDisplay(DungeonsPlayer dungplayer)
	{
		dp = dungplayer;
		
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		obje = board.registerNewObjective("mcExperiment", "dummy", ChatColor.GOLD + "mcExperiment" + ChatColor.WHITE + ": Dungeons");
		obje.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		health = board.registerNewTeam("health");
		health.addEntry(ChatColor.RED + "");
		mana = board.registerNewTeam("mana");
		mana.addEntry(ChatColor.LIGHT_PURPLE + "");
		armour = board.registerNewTeam("armour");
		armour.addEntry(ChatColor.BLUE + "");
		empty = board.registerNewTeam("empty");
		empty.addEntry(ChatColor.RESET + "");
		empty.addEntry(ChatColor.RESET + "" + ChatColor.RESET);
		//empty.addEntry(ChatColor.RESET + "" + ChatColor.RESET + "" + ChatColor.RESET);
		coins = board.registerNewTeam("coins");
		coins.addEntry(ChatColor.GOLD + "");
		area = board.registerNewTeam("area");
		area.addEntry(ChatColor.WHITE + "");
		
		refresh();
		dp.player.setScoreboard(board);
	}
	
	public void refresh()
	{
		health.setPrefix(ChatColor.RED + "Health: " + ChatColor.WHITE + dp.getHealth() + "/" + dp.stats.health);
		obje.getScore(ChatColor.RED + "").setScore(4);
		armour.setPrefix(ChatColor.BLUE + "Armour: " + ChatColor.WHITE + dp.stats.armour);
		obje.getScore(ChatColor.BLUE + "").setScore(3);
		if (dp.playerHasMana()) mana.setPrefix(ChatColor.LIGHT_PURPLE + "Mana: " + ChatColor.WHITE + dp.getMana() + "/" + dp.stats.mana);
		else mana.setPrefix("Mana: 0");
		obje.getScore(ChatColor.LIGHT_PURPLE + "").setScore(2);
		obje.getScore(ChatColor.RESET + "").setScore(5);
		coins.setPrefix(ChatColor.GOLD + "Coins: " + ChatColor.WHITE + dp.coins);
		obje.getScore(ChatColor.GOLD + "").setScore(6);
		obje.getScore(ChatColor.RESET + "" + ChatColor.RESET).setScore(1);
		area.setPrefix(ChatColor.GOLD + "You are in " + ChatColor.RED + dp.area.name);
		obje.getScore(ChatColor.WHITE + "").setScore(0);
	}
}
