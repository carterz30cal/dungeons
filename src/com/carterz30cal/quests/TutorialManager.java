package com.carterz30cal.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.tasks.TaskSendMsg;

import net.md_5.bungee.api.ChatColor;

public class TutorialManager
{
	public static List<Tutorial> tutorials = new ArrayList<>();
	
	public static void init()
	{
		Tutorial join = new Tutorial();
		join.name = "Welcome!";
		join.add(TutorialTrigger.JOIN, "");
		join.msg("Welcome to mcExperiment! This is an RPG server with");
		join.msg("levelling, custom items, mobs and more!");
		join.msg("To begin, walk through the tutorial!");
		
		Tutorial killfirstdrenched = new Tutorial();
		killfirstdrenched.name = "First Blood";
		killfirstdrenched.add(TutorialTrigger.KILL_ENEMY, "drenched1");
		killfirstdrenched.add(TutorialTrigger.KILL_ENEMY, "drenched2");
		killfirstdrenched.add(TutorialTrigger.KILL_ENEMY, "drenched3");
		killfirstdrenched.add(TutorialTrigger.KILL_ENEMY, "drenched4");
		killfirstdrenched.msg("You've killed your first drenched!");
		killfirstdrenched.msg("You will have acquired some XP and coins");
		killfirstdrenched.msg("XP is the main measure of progress on the server");
		killfirstdrenched.msg("and coins are useful for purchasing and trading");
		killfirstdrenched.msg("All drops will be sent to your backpack for safekeeping");
		
		Tutorial waterfragment = new Tutorial();
		waterfragment.name = "Water Fragment";
		waterfragment.add(TutorialTrigger.DROP, "water_fragment");
		waterfragment.msg("You've obtained a Water Fragment! Use the");
		waterfragment.msg("recipe browser in the Dungeons Menu to");
		waterfragment.msg("see what you can make!");
		waterfragment.msg("We suggest trying to make a water blade");
		waterfragment.msg("which requires 2 water fragments and 1");
		waterfragment.msg("flimsy paper stick.");
		
		Tutorial pickaxe = new Tutorial();
		pickaxe.name = "Strike the Earth!";
		pickaxe.add(TutorialTrigger.BUY_ITEM, "pickaxe_flimsy");
		pickaxe.add(TutorialTrigger.CRAFT_ITEM, "pickaxe_water");
		pickaxe.add(TutorialTrigger.CRAFT_ITEM, "pickaxe_paper");
		pickaxe.add(TutorialTrigger.CRAFT_ITEM, "pickaxe_mush");
		pickaxe.msg("You can use pickaxes to obtain materials");
		pickaxe.msg("from certain blocks in each dungeon");
		pickaxe.msg("For example, in Waterway, you can mine");
		pickaxe.msg("green terracotta and concrete to obtain");
		pickaxe.msg("leaf mash, which makes several items");
		
		Tutorial titan = new Tutorial();
		titan.name = "Titan Slayer";
		titan.add(TutorialTrigger.KILL_ENEMY, "drenched_titan");
		titan.msg("You have helped slay the mighty titan drench!");
		titan.msg("You will have received a lootbox which may contain");
		titan.msg("rare loot and enchanted books!");
		
		Tutorial waterwayboss = new Tutorial();
		waterwayboss.name = "A Risky Encounter";
		waterwayboss.add(TutorialTrigger.CRAFT_ITEM, "summonkey_waterway");
		waterwayboss.msg("You have crafted a waterway key, and now you may summon");
		waterwayboss.msg("the mighty Dried Drench in the altar room at the end of");
		waterwayboss.msg("waterway. Be careful, it's tough without friends!");
		
		Tutorial waterwayboss_beaten = new Tutorial();
		waterwayboss_beaten.name = "King of Waterway";
		waterwayboss_beaten.add(TutorialTrigger.KILL_ENEMY, "waterwayboss_boss");
		waterwayboss_beaten.msg("You have defeated the mighty Dried Drench and");
		waterwayboss_beaten.msg("can now advance to Necropolis. Be alert, for");
		waterwayboss_beaten.msg("the skeletons there are not friendly!");
		
		Tutorial necropolis_beaten = new Tutorial();
		necropolis_beaten.name = "Necropolis Lord";
		necropolis_beaten.add(TutorialTrigger.KILL_ENEMY, "necropolis_boss");
		necropolis_beaten.msg("You've defeated The Grave Lord and may now pass through");
		necropolis_beaten.msg("to the Infested Caverns. Good luck!");
		
		Tutorial catalyst = new Tutorial();
		catalyst.name = "Enchanting";
		catalyst.add(TutorialTrigger.BUY_ITEM, "catalyst=0");
		catalyst.add(TutorialTrigger.DROP, "catalyst=0");
		catalyst.msg("You've gotten your first catalyst! These can");
		catalyst.msg("be used to enchant items and combine books");
		catalyst.msg("to make them more powerful!");
		
		Tutorial wands = new Tutorial();
		wands.name = "Might and magic!";
		wands.add(TutorialTrigger.CRAFT_ITEM, "wand_crude");
		wands.add(TutorialTrigger.CRAFT_ITEM, "wand_sewer");
		wands.msg("Wands are powerful weapons, but they need to contain");
		wands.msg("a spell to work. Spells can be found all around the");
		wands.msg("world, and many are dropped by powerful monsters");
		
		Tutorial sharpeners = new Tutorial();
		sharpeners.name = "Sharpened!";
		sharpeners.add(TutorialTrigger.BUY_ITEM, "sharpener_crude");
		sharpeners.msg("Sharpeners are a great way of boosting your stats!");
		sharpeners.msg("To apply one to a weapon, use the applying menu");
		sharpeners.msg("located in the anvil. Remember, you can only use");
		sharpeners.msg("4 of them, so choose wisely.");
		
		Tutorial chamberguardian = new Tutorial();
		chamberguardian.name = "Chamber Access";
		chamberguardian.add(TutorialTrigger.KILL_ENEMY, "temple_chamberguard");
		chamberguardian.msg("You have slain the chamber guardian and may");
		chamberguardian.msg("now access the chamber! Be careful, it's tough!");
		
		
	}
	
	
	public static void fireEvent(DungeonsPlayer player,TutorialTrigger event,String data)
	{
		for (Tutorial tutorial : tutorials)
		{
			if (player.tutorials.contains(tutorial.name)) continue;
			if (!tutorial.triggered(event, data)) continue;
			
			player.tutorials.add(tutorial.name);
			player.player.playSound(player.player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
			new TaskSendMsg(5,player.player,ChatColor.GOLD + "" + ChatColor.BOLD + "Tutorial: " + tutorial.name);
			for (String message : tutorial.messages) new TaskSendMsg(8,player.player,ChatColor.AQUA + message);
			
		}
	}
}
