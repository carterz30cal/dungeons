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
	}
	public void setText(String[] st,String[] mid,String[] e)
	{
		start = st;
		repeat = mid;
		end = e;
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
				if (type.equals(item))
				{
					if (consumes) i.setAmount(Math.max(0, i.getAmount()-amount));
					dia = end;
					dun.questProgress.put(id,"finished");
					dun.coins += coins;
					dun.level.give(xp);
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
				new String[] {"Hurry up, my feet might fall off"}, new String[] {"Phew.... thank you, friend",
						"Have this magic dust i found lying around","Come back if you want something else to do, friend"});
		Quest zombiejim_3 = new Quest(zombiejim);
		zombiejim_3.id = "zombiejim_3";
		zombiejim_3.item = "armour_cloud_boots";
		zombiejim_3.amount = 1;
		zombiejim_3.consumes = false;
		zombiejim_3.setReward("armour_zombiejim_chestplate", 1, 0);
		zombiejim_3.setText(new String[] {"Hey again friend!","I need to look at some Cloud Boots for my next book","Do you think you can find me some?"
				,"Don't worry, I'm not keeping them, i just need a look"},
				new String[] {"Get on with it, my publisher is pestering me"}, new String[] {"You really are a good friend",
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
				new String[] {"I heard humours the Titan sometimes carries one"},
				new String[] {"Oh thank you!","Here, have 3000 coins and another weird book i found"
						,"This one feels more powerful","Maybe I'll find some material for my new book while out exploring!"});
		Quest zombiejim_6 = new Quest(zombiejim);
		zombiejim_6.id = "zombiejim_6";
		zombiejim_6.item = "crypt_key";
		zombiejim_6.amount = 5;
		zombiejim_6.consumes = true;
		zombiejim_6.setReward("armour_zombiejim_boots", 1, 0);
		zombiejim_6.setText(new String[] {"Hi friend","I think I am going to retire to the Crypts soon",
				"I have one last job for you","To get back to the Crypts, I'll need enough Crypt Keys, 5 should cut it"
				,"I don't think you'll find any here, you'll need to look elsewhere"},
				new String[] {"Apparently a merchant in Necropolis sells them"},
				new String[] {"You are a true friend","I'll leave you with my favourite slippers"
						,"I fashioned them out of those water boots you gave me","They're pretty nice, if I say so myself"});
		
		QuestNpc clucky = new QuestNpc(new Location(Dungeons.w,-42,118.9,21005,77.5f,-16),EntityType.CHICKEN,"Clucky",Sound.ENTITY_CHICKEN_HURT);
		
		Quest clucky_1 = new Quest(clucky);
		clucky_1.id = "clucky_1";
		clucky_1.setItem("fishing_fish", 1);
		clucky_1.setReward(null, 0, 100);
		clucky_1.setText(new String[] {"I wish I could climb ladders.","I could leave this pit... Explore the world.",
				"Maybe you could help me?","I know of a potion.","But first, can you fetch me a fish?"},
				new String[] {"Just one fish, please."},
				new String[] {"Thanks.","Have 100 coins for your effort."});
		
		Quest clucky_2 = new Quest(clucky);
		clucky_2.id = "clucky_2";
		clucky_2.setItem("water_fragment", 22);
		clucky_2.setReward("sword_fishingforkstrange", 1, 0);
		clucky_2.setText(new String[] {"Right. To make this potion I need many ingredients.","But to start, I need the base.",
				"For this I will need 22 water fragments"},
				new String[] {"The 22 water fragments will make the water required for the potion."},
				new String[] {"These are of excellent quality, thank you. Have this fishing spear I found years ago."});
		Quest clucky_3 = new Quest(clucky);
		clucky_3.id = "clucky_3";
		clucky_3.setItem("ocean_fragment", 6);
		clucky_3.setReward(null, 0, 300);
		clucky_3.setText(new String[] {"Let's continue.","To purify the water, I need 6 ocean fragments."},
				new String[] {"6 ocean fragments should do it."},
				new String[] {"The water looks crystal clear.","The next few ingredients may be harder to acquire.",
						"Here is 300 coins for your help."});
		Quest clucky_4 = new Quest(clucky);
		clucky_4.id = "clucky_4";
		clucky_4.setItem("quest_clucky4", 1);
		clucky_4.setReward("sharpener_midas", 1,0);
		clucky_4.setText(new String[] {"Time to start brewing.","I invented a stew years ago which is useful in many potions."
				,"It is made from magic dust, a catalyst and some leaf material"},
				new String[] {"The Fisherman sells handcrafted bowls"},
				new String[] {"Perfectly crafted!","This may take some time to brew.",
						"Have this midas flint."});
		
		QuestNpc john = new QuestNpc(new Location(Dungeons.w,-22.5,88,22063.5,-90,0),EntityType.ZOMBIE,"John",Sound.ENTITY_ZOMBIE_HURT);
		Quest john_1 = new Quest(john);
		john_1.id = "john_1";
		john_1.setItem("enchanted_mushroom", 1);
		john_1.setReward(1000);
		john_1.setText(new String[] {"I need an enchanted mushroom. This guy called the Shroom King has one, but he's kinda scary."
				,"Do you think you could get it from him?"
		}, new String[] {"The Shroom King lives just in this tunnel."}, new String[] {"Oh thank you ever so much!"});
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
