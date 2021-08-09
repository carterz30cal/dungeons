package com.carterz30cal.player;

import java.time.Instant;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	private Team area2;
	private Team xpBoost;
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
		area2 = board.registerNewTeam("area2");
		area2.addEntry(ChatColor.BLACK + "");
		xpBoost = board.registerNewTeam("xp");
		xpBoost.addEntry(ChatColor.AQUA + "");
		
		refresh();
		dp.player.setScoreboard(board);
	}
	
	public void refresh()
	{
		health.setPrefix(ChatColor.RED + "Health: " + ChatColor.WHITE + dp.getHealth() + "/" + dp.stats.health);
		obje.getScore(ChatColor.RED + "").setScore(6);
		armour.setPrefix(ChatColor.BLUE + "Armour: " + ChatColor.WHITE + dp.stats.armour);
		obje.getScore(ChatColor.BLUE + "").setScore(5);
		if (dp.playerHasMana()) mana.setPrefix(ChatColor.LIGHT_PURPLE + "Mana: " + ChatColor.WHITE + dp.getMana() + "/" + dp.stats.mana);
		else mana.setPrefix("Mana: 0");
		obje.getScore(ChatColor.LIGHT_PURPLE + "").setScore(4);
		obje.getScore(ChatColor.RESET + "").setScore(7);
		coins.setPrefix(ChatColor.GOLD + "Coins: " + ChatColor.WHITE + dp.coins);
		obje.getScore(ChatColor.GOLD + "").setScore(8);
		obje.getScore(ChatColor.RESET + "" + ChatColor.RESET).setScore(3);
		area.setPrefix(ChatColor.GOLD + "You are in:");
		obje.getScore(ChatColor.WHITE + "").setScore(1);
		if (dp.inTemple())
		{
			area2.setPrefix(ChatColor.DARK_RED + "THE TEMPLE");
			obje.getScore(ChatColor.BLACK + "").setScore(0);
		}
		else
		{
			area2.setPrefix(ChatColor.RED + dp.area.name);
			obje.getScore(ChatColor.BLACK + "").setScore(0);
		}
		
		
		if (dp.voteBoost != null && dp.voteBoost.isAfter(Instant.now())) xpBoost.setPrefix(ChatColor.GREEN + "Vote boost active!");
		else xpBoost.setPrefix(ChatColor.RED + "Vote boost inactive!");
		obje.getScore(ChatColor.AQUA + "").setScore(2);
	}
}
