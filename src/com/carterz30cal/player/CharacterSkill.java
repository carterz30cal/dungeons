package com.carterz30cal.player;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CharacterSkill
{
	public static final int levelcap = 999;
	
	public static final HashMap<Integer,Long> levels = new HashMap<>();
	
	public static CharacterSkill[] leaderboard = new CharacterSkill[20];
	
	public static Map<Integer,Team> tablist = new HashMap<>();
	public static Scoreboard board;
	
	public Player owner;
	public long experience;
	public int points;
	
	private int level;
	public HashMap<String,Integer> pointAllocation;
	
	public CharacterSkill(Player owner)
	{
		this.owner = owner;
		
		experience = Dungeons.instance.getPlayerConfig().getLong(owner.getUniqueId() + ".experience", 0);
		points = Dungeons.instance.getPlayerConfig().getInt(owner.getUniqueId() + ".points", 0);
		pointAllocation = new HashMap<String,Integer>();
		
		if (Dungeons.instance.getPlayerConfig().contains(owner.getUniqueId() + ".skills"))
		{
			for (String l : Dungeons.instance.getPlayerConfig().getConfigurationSection(owner.getUniqueId() + ".skills").getKeys(false))
			{
				pointAllocation.put(l, Dungeons.instance.getPlayerConfig().getInt(owner.getUniqueId() + ".skills." + l, 0));
			}
		}
		
		level = level(experience);
		
		if (board == null)
		{
			board = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective ob = board.registerNewObjective("playerlist", "dummy","dummy");
			ob.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		}
		
		if (!tablist.containsKey(level))
		{
			Team t = board.registerNewTeam(Integer.toString(levelcap-level));
			tablist.put(level, t);
		}
		tablist.get(level).addEntry(owner.getName());
		
		owner.setPlayerListName(prettyText(level()) + " " + owner.getName());
		
		updateBoard(this);
	}
	
	
	public static void updateBoard(CharacterSkill updated)
	{
		ArrayList<CharacterSkill> old = new ArrayList<>();
		
		old.addAll(Arrays.asList(leaderboard));
		if (!old.contains(updated)) old.add(updated);
		
		leaderboard = new CharacterSkill[20];
		for (CharacterSkill t : old)
		{
			if (t == null) continue;
			if (!Bukkit.getOnlinePlayers().contains(t.owner)) continue;
			for (int i = 0; i < 20; i++)
			{
				if (leaderboard[i] == null)
				{
					leaderboard[i] = t;
					break;
				}
				else if (t.experience > leaderboard[i].experience)
				{
					for (int j = 19; j > i;j--) leaderboard[j] = leaderboard[j-1];
					leaderboard[i] = t;
					break;
				}
			}
		}
		
	}
	
	public static void removeFromBoard(CharacterSkill updated)
	{
		ArrayList<CharacterSkill> old = new ArrayList<>();
		
		old.addAll(Arrays.asList(leaderboard));
		old.remove(updated);
		
		leaderboard = new CharacterSkill[20];
		for (CharacterSkill t : old)
		{
			if (t == null) continue;
			if (!Bukkit.getOnlinePlayers().contains(t.owner)) continue;
			for (int i = 0; i < 20; i++)
			{
				if (leaderboard[i] == null)
				{
					leaderboard[i] = t;
					break;
				}
				else if (t.experience > leaderboard[i].experience)
				{
					for (int j = 19; j > i;j--) leaderboard[j] = leaderboard[j-1];
					leaderboard[i] = t;
					break;
				}
			}
		}
		
	}
	
	
	
	
	
	public static long scaler(int level)
	{
		return (long) (100 * (Math.pow(level /5, 3)+1));
	}
	
	public int get(String skill)
	{
		return pointAllocation.getOrDefault(skill, 0);
	}

	public void updatePlayerList(int prevle)
	{
		if (tablist.get(prevle) != null) tablist.get(prevle).removeEntry(owner.getName());
		if (!tablist.containsKey(level))
		{
			Team t = board.registerNewTeam(Integer.toString(levelcap-level));
			tablist.put(level, t);
		}
		tablist.get(level).addEntry(owner.getName());
	}
	public static long requirement(int level)
	{
		long check = levels.getOrDefault(level, (long) -1);
		if (check != -1) return check;
		
		long value = 0;
		if (level <= 1) value = 100;
		else if (level % 5 == 0) 
		{
			value = requirement(level-1) + scaler(level);
		}
		else
		{
			int five = (level / 5) * 5;
			int diff = level-five;
			long add = scaler(level);
			value = requirement(five) + (add * diff);
		}
		
		levels.put(level, value);
		return value;
	}
	
	public static int level(long exp)
	{
		int c = 0;
		while (requirement(c+1) < exp) c++;
		return Math.min(levelcap, c);
	}
	public int level()
	{
		return level;
	}
	public double progress()
	{
		if (level == 0) return (double)experience / 100;
		
		int current = level(experience);
		return ((double)(experience - requirement(current))) / (requirement(current+1) - requirement(current));
	}
	public int prettyProgress()
	{
		int p = (int) Math.round(Math.min(100, 100 * progress()));
		if (p == 100) return 99;
		return p;
	}
	public static String prettyText(int level)
	{
		String lvl = "" + level;
		if (level < 10) lvl = ChatColor.GRAY + lvl;
		else
		{
			String top = Integer.toString(level).substring(0,1);
			if (top.equals("1")) lvl = ChatColor.BLUE + lvl;
			else if (top.equals("2")) lvl = ChatColor.AQUA + lvl;
			else if (top.equals("3")) lvl = ChatColor.GREEN + lvl;
			else if (top.equals("4")) lvl = ChatColor.DARK_GREEN + lvl;
			else if (top.equals("5")) lvl = ChatColor.YELLOW + lvl;
			else if (top.equals("6")) lvl = ChatColor.GOLD + lvl;
			else if (top.equals("7")) lvl = ChatColor.RED + lvl;
			else if (top.equals("8")) lvl = ChatColor.DARK_RED + lvl;
			else if (top.equals("9")) lvl = ChatColor.LIGHT_PURPLE + lvl;
		}
		return ChatColor.WHITE + "[" + lvl + ChatColor.RESET + "" + ChatColor.WHITE + "]";
	}
	public static int bonus(int level)
	{
		if (level == 25) return 4;
		if (level == 50) return 4;
		if (level >= 59) return 1;
		if (level % 5 == 0 && level != 0) return 1;
		return 0;
	}
	public void give(long amount)
	{
		give(amount,true);
	}
	public void give(long amount,boolean multiply)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(owner);
		int current = level(experience);
		long am = (long)(amount * d.stats.miningXp);
		if (!multiply) am = amount;
		experience += am;
		int lvl = level(experience);

		owner.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "+" + StringManipulator.truncate(am) + " XP"
				+ "    " + ChatColor.BLUE + prettyProgress() + "%"));
		level = lvl;
		if (lvl > current)
		{
			int npoints = lvl - current;
			for (int i = current; i < lvl;i++) npoints += bonus(i);
			owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
			owner.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "LEVEL " + lvl + "!");
			owner.sendMessage(ChatColor.AQUA + " + " + npoints + " skill points!");
			points += npoints;
			
			owner.setPlayerListName(prettyText(level()) + " " +DungeonsPlayer.rankColours[d.rank.ordinal()] + owner.getName());
			updatePlayerList(current);
		}
		updateBoard(this);
	}
	
}
