package com.carterz30cal.quests;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.tasks.TaskSendMsg;
import com.carterz30cal.utility.InventoryHandler;

import net.md_5.bungee.api.ChatColor;

public class Quest
{
	public static HashMap<String,QuestNpc> quests;
	
	public String id;
	public String prereq; // the quest before it. null means there isn't a quest before it.
	
	
	public QuestNpc questgiver;
	
	
	public String[] start;
	public String[] repeat;
	public String[] end;
	
	public String item;
	public boolean consumes = true;
	public int amount;
	
	public ItemStack reward;
	public int coins;
	public int xp;
	
	public Quest(QuestNpc npc)
	{
		questgiver = npc;
		questgiver.quests.add(this);
		
		id = questgiver.name + "_" + questgiver.quests.size();
	}
	public void setText(String[] st,String[] mid,String[] e)
	{
		start = st;
		repeat = mid;
		end = e;
	}
	public void setText(String st,String mid,String e)
	{
		start = st.split(";");
		repeat = mid.split(";");
		end = e.split(";");
	}
	public void setItem(String i,int a)
	{
		item = i;
		amount = a;
		consumes = true;
	}
	public void setItem(String i,int a,boolean c)
	{
		item = i;
		amount = a;
		consumes = c;
	}
	public void setReward(String item,int a,int c)
	{
		if (item == null) reward = null;
		else reward = ItemBuilder.i.build(item, a);
		coins = c;
		xp = 0;
	}
	public void setReward(String item,int a,int c,int x)
	{
		if (item == null) reward = null;
		else reward = ItemBuilder.i.build(item, a);
		coins = c;
		xp = x;
	}
	public void setReward(String item,String enchants,int a,int c)
	{
		if (item == null) reward = null;
		else reward = ItemBuilder.i.build(item, null, enchants);
		coins = c;
		xp = 0;
	}
	public void setReward(int x)
	{
		xp = x;
	}
	public void setReward(int x,int c)
	{
		xp = x;
		coins = c;
	}
	public void interact(Player p)
	{
		/*
		 * stages
		 * null / start = start
		 * started = repeat
		 * finished = end
		 */
		DungeonsPlayer dun = DungeonsPlayerManager.i.get(p);
		String stage = dun.questProgress.getOrDefault(id,"start");
		String[] dia = start;
		if (stage == "doing")
		{
			dia = repeat;
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i != null && i.hasItemMeta() && i.getAmount() >= amount)
			{
				String type = i.getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING);
				if (type != null && type.equals(item))
				{
					if (consumes) i.setAmount(Math.max(0, i.getAmount()-amount));
					dia = end;
					dun.questProgress.put(id,"finished");
					dun.coins += coins;
					if (xp != 0) dun.level.give(xp,false);
					if (reward != null) InventoryHandler.addItem(dun, reward);
				}
			}
		}
		else
		{
			dun.questProgress.put(id,"doing");
		}
		int c = 0;
		for (String d : dia)
		{
			new TaskSendMsg(p,ChatColor.GOLD + "[QUEST] " + questgiver.name + ChatColor.RESET + ": " + d,c);
			new SoundTask(p.getLocation(),p,questgiver.speak,1,1).runTaskLater(Dungeons.instance, c*20);
			c++;
		}
	}
	public static void init()
	{
		quests = new HashMap<String,QuestNpc>();
		
		QuestNpc zombiejim = new QuestNpc(new Location(Dungeons.w,-1, 106,20988,-15,0),EntityType.ZOMBIE,"Zombie Jim",Sound.ENTITY_ZOMBIE_HURT);
		Quest zombiejim_1 = new Quest(zombiejim);
		zombiejim_1.id = "zombiejim_1";
		zombiejim_1.item = "wetpaper";
		zombiejim_1.amount = 64;
		zombiejim_1.consumes = true;
		zombiejim_1.setReward("sword_zombiejim", 1, 0);
		zombiejim_1.setText(new String[] {"Hi there. I'm Zombie Jim","Could you bring me a stack of wet paper?","I'll reward you handsomely!!!"},
				new String[] {"Come back when you have a stack of wet paper"}, new String[] {"Thank you so much!!!","I can use this to write my latest book",
						"Here, have my BEST SWORD!!","Come back if you want something else to do, friend"});
		Quest zombiejim_2 = new Quest(zombiejim);
		zombiejim_2.id = "zombiejim_2";
		zombiejim_2.item = "armour_water_boots";
		zombiejim_2.amount = 1;
		zombiejim_2.consumes = true;
		zombiejim_2.setReward("magic_dust", 1, 0);
		zombiejim_2.setText(new String[] {"Hey friend!","My feet are cold. You reckon you can find me some water boots?","I promise it will be worth it"},
				new String[] {"Arhhhh, water boots please."}, new String[] {"Phew.... thank you, friend",
						"Have this magic dust i found lying around","Come back if you want something else to do, friend"});
		Quest zombiejim_3 = new Quest(zombiejim);
		zombiejim_3.id = "zombiejim_3";
		zombiejim_3.item = "armour_cloud_boots";
		zombiejim_3.amount = 1;
		zombiejim_3.consumes = false;
		zombiejim_3.setReward("armour_zombiejim_chestplate", 1, 0);
		zombiejim_3.setText(new String[] {"Hey again friend!","I need to look at some Cloud Boots for my next book","Do you think you can find me some?"
				,"The titan sometimes has them.","Don't worry, I'm not keeping them, i just need a look"},
				new String[] {"Find me cloud boots! The titan has them!"}, new String[] {"You really are a good friend",
						"For your reward.. i came across this enchanted chestplate on my travels","You might like it.","Speak to you soon!"});
		Quest zombiejim_4 = new Quest(zombiejim);
		zombiejim_4.id = "zombiejim_4";
		zombiejim_4.item = "fishing_fishbarrel";
		zombiejim_4.amount = 1;
		zombiejim_4.consumes = true;
		zombiejim_4.setReward("book","midastouch,3", 1, 2000);
		zombiejim_4.setText(new String[] {"Psst.. Over here friend.","I'm working on another new book.",
				"I need some fish. A barrel of it should do.","You can do that for me, right?"},
				new String[] {"You'll need a fishing spear.","The Fisherman should be able to supply one of those"},
				new String[] {"What would I do without you?","Here is 2000 coins and this dusty old book I found somewhere"
						,"Not sure what it does, maybe you can figure it out","See you soon friend!"});
		Quest zombiejim_5 = new Quest(zombiejim);
		zombiejim_5.id = "zombiejim_5";
		zombiejim_5.item = "sword_hyperblade";
		zombiejim_5.amount = 1;
		zombiejim_5.consumes = true;
		zombiejim_5.setReward("book","midastouch,4", 1, 3000);
		zombiejim_5.setText(new String[] {"Nice to see you my friend","My last book was a huge success thanks to you",
				"I want to explore","I've been told the Hyperblade is a good sword"
				,"Could you get one for me? I'll pay handsomely!"},
				new String[] {"I heard humours the Titan sometimes carries a hyperblade.."},
				new String[] {"Oh thank you!","Here, have 3000 coins and another weird book i found"
						,"This one feels more powerful","Maybe I'll find some material for my new book while out exploring!"});
		Quest zombiejim_6 = new Quest(zombiejim);
		zombiejim_6.id = "zombiejim_6";
		zombiejim_6.item = "crypt_key1";
		zombiejim_6.amount = 3;
		zombiejim_6.consumes = true;
		zombiejim_6.setReward("armour_zombiejim_boots", 1, 0);
		zombiejim_6.setText(new String[] {"Hi friend","I think I am going to retire to the Crypts soon",
				"I have one last job for you","To get back to the Crypts, I'll need enough Crypt Keys, 3 should cut it"
				,"I don't think you'll find any here, you'll need to look elsewhere"},
				new String[] {"3 crypt keys from the necropolis merchant, thanks."},
				new String[] {"You are a true friend","I'll leave you with my favourite slippers"
						,"I fashioned them out of those water boots you gave me","They're pretty nice, if I say so myself"});
		
		QuestNpc clucky = new QuestNpc(new Location(Dungeons.w,-60.5,100,21021.5,177,0),EntityType.CHICKEN,"Clucky",Sound.ENTITY_CHICKEN_HURT);
		
		/*
		 * CLUCKYS QUESTS
		 * #1 = fetch him 6 water fragments D
		 * #2 = fetch him 10 fish D
		 * #3 = an ocean blade D
		 * #4 = 3 royal leaf D
		 * #5 = ocean chestplate D
		 * #5.1 = 4 storm crystals. D
		 * #6 = hyperspear D
		 * #7 = titan plate
		 * #8 = titan helm
		 * #9 = 1 venomous eye
		 * #10 = 2 holy water fragments
		 * 
		 * 
		 */
		
		Quest cluckyn_1 = new Quest(clucky);
		cluckyn_1.setItem("water_fragment", 6);
		cluckyn_1.setText("I'm brewing something really cool.;Can you fetch me 6 water fragments?", "I need 6 water fragments.", "Thanks!;Have 100 coins!");
		cluckyn_1.setReward(50, 100);
		
		Quest cluckyn_2 = new Quest(clucky);
		cluckyn_2.setItem("fishing_fish", 10);
		cluckyn_2.setText("Can you get me 10 fish please?;They're essential for my potion!", "I need 10 fish for my potion.", "Have 200 coins!");
		cluckyn_2.setReward(75, 200);
		
		Quest cluckyn_3 = new Quest(clucky);
		cluckyn_3.setItem("sword_ocean", 1,true);
		cluckyn_3.setText("I need a sword.;Can you get an ocean blade?", "I want an ocean blade.", "Yay!;I can fight monsters now.;Have 275 coins!");
		cluckyn_3.setReward(100,275);
		
		Quest cluckyn_4 = new Quest(clucky);
		cluckyn_4.setItem("royalleaf", 3);
		cluckyn_4.setText("This next one is a little more tricky.;I need 3 royal leaves, which spawns rarely while mining.;You up for it?", "3 royal leaves please.",
				"You're pretty good at this.;Have my old pickaxe!");
		cluckyn_4.setReward("pickaxe_clucky", 1, 0);
		
		Quest cluckyn_5 = new Quest(clucky);
		cluckyn_5.setItem("armour_ocean_chestplate", 1);
		cluckyn_5.setText("Waterway isn't very warm.;You reckon you can get me an ocean chestplate?", "An ocean chestplate, please!", "Thanks for your help!;Have 500 coins.");
		cluckyn_5.setReward(150, 500);
		
		Quest cluckyn_6 = new Quest(clucky);
		cluckyn_6.setItem("storm_crystals", 4);
		cluckyn_6.setText("4 storm crystals please!", "You'll find them on Soaked during the rain event.", "Wow thanks!");
		cluckyn_6.setReward(200);
		
		Quest cluckyn_7 = new Quest(clucky);
		cluckyn_7.setItem("sword_hyperspear", 1);
		cluckyn_7.setText("Turns out the ocean blade isn't for me.;Can you fetch me a hyperspear?", "I want a hyperspear.", "You're amazing!;Have this midas flint!");
		cluckyn_7.setReward("sharpener_midas", 1, 0,700);
		
		QuestNpc taylor = new QuestNpc(new Location(Dungeons.w,-35.5,93,21054.5,105,-3),EntityType.CREEPER,"Taylor",Sound.ENTITY_CREEPER_HURT);
		
		Quest taylor_1 = new Quest(taylor);
		taylor_1.setItem("sand_catalyst", 4);
		taylor_1.setText("Can you get me 4 sand catalysts, please?", "4 sand catalysts, please.", "Thanks.");
		taylor_1.setReward(100, 400);
		Quest taylor_2 = new Quest(taylor);
		taylor_2.setItem("regeneration_stone", 1);
		taylor_2.setText("I want one of those yellow rocks that grow.", "A yellow, growing rock, thanks.", "Thank you.");
		taylor_2.setReward(300, 800);
		Quest taylor_3 = new Quest(taylor);
		taylor_3.setItem("spell_tsunami", 1);
		taylor_3.setText("I would like the tsunami spell.;I've heard the hydra sometimes drops it.", "I want the tsunami spell from the hydra.", "Yay!");
		taylor_3.setReward(300, 800);
		Quest taylor_4 = new Quest(taylor);
		taylor_4.setItem("armour_hydra_boots", 1);
		taylor_4.setText("Everyone's feet are cold here.;Mine are no exception.;Can you get me a pair of hydra boots?",
				"I would like some hydra boots.;The hydra drops them.", "Awesome, thanks.");
		taylor_4.setReward(1000, 2000);
		
		
		QuestNpc harris = new QuestNpc(new Location(Dungeons.w,-13.5, 105, 20973.5, 5, 1),EntityType.BLAZE,"Harris",Sound.ENTITY_BLAZE_HURT);
		Quest harris_1 = new Quest(harris);
		harris_1.setText("I have some really cool books;Bring me 10 leaf mash and i'll show you one.","I need 10 leaf mush.","Alright, here you go.");
		harris_1.setItem("leafmash", 10);
		harris_1.setReward("book", "spirit,1", 1, 0);
		
		Quest harris_2 = new Quest(harris);
		harris_2.setText("You want another cool book?;Bring me a waterway core piece and you can have it.","One waterway core piece, please.","Ok! Here you are.");
		harris_2.setItem("waterway_core", 1);
		harris_2.setReward("book", "trunk,3", 1, 0);
		
		Quest harris_3 = new Quest(harris);
		harris_3.setText("Hungry for more?;Bring me any enchanted book and i'll exchange it for one of my own.", "Give me any book.", "Sweet!;There you go.");
		harris_3.setItem("book", 1);
		harris_3.setReward("book","execution,4;blade,2;shocking,1",1,0);
		
		Quest harris_4 = new Quest(harris);
		harris_4.setText("Still need more books?;Bring me a stack of water fragments and i'll give you..;I dunno.. something for your armour",
				"I require a stack of water fragments.","You actually did it!");
		harris_4.setItem("water_fragment", 64);
		harris_4.setReward("book", "vitals,1;protection,2;armourpolish,1", 1, 0);
		
		Quest harris_5 = new Quest(harris);
		harris_5.setText("Another?!;Alright.. fetch me 2 compressed tissue",
				"I need 2 compressed tissue","Huh.;You actually got that.;Wow.");
		harris_5.setItem("compressed_tissue", 2);
		harris_5.setReward("book", "vitals,3;growth,3", 1, 0);
		
		QuestNpc cliff = new QuestNpc(new Location(Dungeons.w,28.5,101,22023.5,147,0),EntityType.ENDERMAN,"Cliff",Sound.ENTITY_ENDERMAN_AMBIENT);
		Quest cliff_1 = new Quest(cliff);
		cliff_1.setText("Hi.;I'm Cliff.;These crypts can be incredibly rewarding, but i can't fit.;Will you collect 1 crypt dust for me?",
				"I would like 1 crypt dust, which can be found in the crypts.", "Awesome!;Come back for more later!");
		cliff_1.setItem("crypt_dust", 1);
		cliff_1.setReward("book", "polished,4", 1, 0);
		Quest cliff_2 = new Quest(cliff);
		cliff_2.setText("I could really use some gel.;You reckon you can get 64?;You can?;Excellent!","You're getting me 64 gel from the crypts.",
				"I can craft sewer armour finally!;Have 1200 coins for your time.");
		cliff_2.setItem("gel", 64);
		cliff_2.setReward(1500, 1200);
		Quest cliff_3 = new Quest(cliff);
		cliff_3.setText("Right.;This one's a toughie.;I need 4 magic cacti.", "Can you fetch me 4 magic cacti?", "Wow. I didn't think you could do it.;"
				+ "Have this book and 2500 coins."
				);
		cliff_3.setItem("magic_cactus", 4);
		cliff_3.setReward("book", "polished,6", 1, 2500);
		cliff_3.setReward(2250);
		
		
		QuestNpc john = new QuestNpc(new Location(Dungeons.w,-22.5,88,22063.5,-90,0),EntityType.ZOMBIE,"John",Sound.ENTITY_ZOMBIE_HURT);
		Quest john_1 = new Quest(john);
		john_1.id = "john_1";
		john_1.setItem("enchanted_mushroom", 1);
		john_1.setReward(1000);
		john_1.setText(new String[] {"I need an enchanted mushroom. This guy called the Shroom King has one, but he's kinda scary."
				,"Do you think you could get it from him?"
		}, new String[] {"The Shroom King lives just in this tunnel."}, new String[] {"Oh thank you ever so much!"});
		
		QuestNpc letty = new QuestNpc(new Location(Dungeons.w,-108.5,104,20979.5,-52,-2),EntityType.SKELETON,"Letty",Sound.ENTITY_SKELETON_STEP);
		Quest letty_1 = new Quest(letty);
		letty_1.id = "letty_1";
		letty_1.setItem("armour_waterway_boots",1);
		letty_1.setReward(5000,10000);
		letty_1.setText(new String[] {"I've heard the boss has these real fancy boots.","According to others, they're pretty hard to get.",
		"Show me some and i'll reward you with a heap of coins."},new String[] {"I want to see the rare boots the boss drops."}, new String[] {"So they do exist!?","Thank youu!"});
		
	}
	public static void remove()
	{
		for (QuestNpc npc : quests.values()) 
		{
			npc.questgiver.remove();
			npc.display.remove();
			npc.display2.remove();
		}
	}
}
