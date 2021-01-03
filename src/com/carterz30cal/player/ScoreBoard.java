package com.carterz30cal.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreBoard
{
	private DungeonsPlayer player;
	private Scoreboard board;
	private Objective objective;
	private Team team;
	
	public ScoreBoard (DungeonsPlayer p)
	{
		player = p;
		
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = board.registerNewObjective("mcExperiment", "dummy", ChatColor.GOLD + "mcExperiment" + ChatColor.WHITE + ": Dungeons");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		team = board.registerNewTeam("team");
		
		player.player.setScoreboard(board);
	}
	
	public void refresh()
	{
		for (String e : team.getEntries()) team.removeEntry(e);
		board.clearSlot(DisplaySlot.SIDEBAR);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		if (!player.playerHasMana())
		{
			
			team.addEntry(ChatColor.RED + "Health: " + player.getHealth() + "/" + player.stats.health);
			objective.getScore(ChatColor.RED + "Health: " + player.getHealth() + "/" + player.stats.health).setScore(1);
			team.addEntry(ChatColor.BLUE + "Armour: ");
			objective.getScore(ChatColor.BLUE + "Armour: ").setScore(0);
			team.setPrefix("");
		}
	}
}
