package com.carterz30cal.player;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.gui.MonsterHunterGUI;
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
	
	public DungeonsPlayer owner;
	
	//public int prestige; // 100 levels = 1 prestige.
	public long experience;
	public long hunter;
	public int points;
	
	public int level;
	public HashMap<String,Integer> pointAllocation;
	
	public CharacterSkill(DungeonsPlayer o)
	{
		this.owner = o;
		
		if (o.versionSaved.equals("0.1.15"))
		{
			experience =  Dungeons.instance.getPlayerConfig().getLong(o.player.getUniqueId() + ".experience", 0);
			while (experience >= tonextlevel(level))
			{
				experience -= tonextlevel(level);
				level++;
			}
		}
		else
		{
			level = Dungeons.instance.getPlayerConfig().getInt(o.player.getUniqueId() + ".level", 0);
			experience = Dungeons.instance.getPlayerConfig().getLong(o.player.getUniqueId() + ".experience", 0);
		}
		
		
		
		hunter = Dungeons.instance.getPlayerConfig().getLong(o.player.getUniqueId() + ".hunter", 0);
		
		points = points();
		
		pointAllocation = new HashMap<String,Integer>();
		
		
		
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
		tablist.get(level).addEntry(o.player.getName());
		
		o.player.setPlayerListName(prettyText(level()) + " " + o.player.getName());
		
		updateBoard(this);
	}
	
	public int points()
	{
		int pt = 0;
		for (int i = 1; i <= level;i++) pt += 1 + CharacterSkill.bonus(i);
		
		pt += MonsterHunterGUI.level(hunter);
		
		int consumed = 0;
		for (int co : owner.skills.values()) consumed += co;
		pt -= consumed;
		
		
		
		return pt;
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
			if (!Bukkit.getOnlinePlayers().contains(t.owner.player)) continue;
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
			if (!Bukkit.getOnlinePlayers().contains(t.owner.player)) continue;
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
	
	public static long tonextlevel(int current)
	{
		int prestige = current/100;
		int l = current - (prestige*100);
		
		if (current % 100 == 99) return xpforlevel(current);
		return (long) (100 * Math.pow(1.2,l) * Math.pow(1.15,prestige));
	}
	
	public static int prestige(int level)
	{
		return level / 100;
	}
	
	
	public static long xpforlevel(int level)
	{
		int prestige = (level-1)/100;
		int l = level - (prestige*100);
		
		long t = 0;
		while (l > 0)
		{
			t += (long) (100 * Math.pow(1.2,l-1) * Math.pow(1.15,prestige));
			l--;
		}
		return t;
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
		if (tablist.get(prevle) != null) tablist.get(prevle).removeEntry(owner.player.getName());
		if (!tablist.containsKey(level))
		{
			Team t = board.registerNewTeam(Integer.toString(levelcap-level));
			tablist.put(level, t);
		}
		tablist.get(level).addEntry(owner.player.getName());
	}
	/*
	@Deprecated
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
	*/
	@Deprecated
	public int level()
	{
		return level;
	}
	public double progress()
	{
		return (double)experience / tonextlevel(level);
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
		int b = 1 + prestige(level);
		if (level % 5 == 0 && level != 0) b += 3;
		return b;
	}
	public void give(long amount)
	{
		give(amount,0,true);
	}
	public void give(long amount,int coins)
	{
		give(amount,coins,true);
	}
	public void give(long amount,boolean multiply)
	{
		give(amount,0,true);
	}
	public void give(long amount,int coins,boolean multiply)
	{
		long total = (long) (multiply ? (amount+owner.stats.flatxp) * owner.stats.miningXp : amount+owner.stats.flatxp);
		
		
		if (experience+total >= tonextlevel(level))
		{
			experience = 0;
			level++;
			
			points = points();
			
			owner.player.playSound(owner.player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
			owner.player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "LEVEL " + level + "!");
			owner.player.sendMessage(ChatColor.AQUA + " + " + (1+bonus(level)) + " skill points!");
			
			owner.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "LEVEL UP!"));
			
			owner.player.setPlayerListName(prettyText(level) + " " + owner.player.getName());
			updatePlayerList(level);
		}
		else
		{
			experience += total;
			if (coins > 0)
			{
				owner.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
						TextComponent.fromLegacyText(ChatColor.GOLD + "+" + coins + " coins   " + ChatColor.AQUA + "+" + StringManipulator.truncate(total) + " XP"
						+ " " + ChatColor.AQUA + "(" + prettyProgress() + "%)"));
			}
			else
			{
				owner.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
						TextComponent.fromLegacyText(ChatColor.AQUA + "+" + StringManipulator.truncate(total) + " XP"
						+ "    " + ChatColor.BLUE + prettyProgress() + "%"));
			}
			
		}
	}
	/*
	public void give(long amount,boolean multiply)
	{
		int current = level(experience);
		long am = (long)(amount * owner.stats.miningXp);
		if (!multiply) am = amount;
		am += owner.stats.flatxp;
		experience += am;
		int lvl = level(experience);

		owner.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "+" + StringManipulator.truncate(am) + " XP"
				+ "    " + ChatColor.BLUE + prettyProgress() + "%"));
		level = lvl;
		if (lvl > current)
		{
			int npoints = 0;
			for (int i = current; i < lvl;i++) npoints += 1 + bonus(i+1);
			owner.player.playSound(owner.player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
			owner.player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "LEVEL " + lvl + "!");
			owner.player.sendMessage(ChatColor.AQUA + " + " + npoints + " skill points!");
			points += npoints;
			
			owner.player.setPlayerListName(prettyText(level()) + " " + owner.player.getName());
			updatePlayerList(current);
		}
		updateBoard(this);
	}
	*/
	
	public void giveFlat(long amount)
	{
		give(amount - owner.stats.flatxp,0,false);
	}
	
}
