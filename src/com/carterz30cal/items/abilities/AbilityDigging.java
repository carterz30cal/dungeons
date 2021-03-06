package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.RandomFunctions;
import com.carterz30cal.utility.Square;

import net.md_5.bungee.api.ChatColor;

public class AbilityDigging extends AbsAbility
{
	public static HashMap<DungeonsPlayer,Location> targets;
	public static HashMap<String,String[]> mobs;
	public static HashMap<String,ArrayList<String>> items;
	
	public AbilityDigging()
	{
		if (targets == null) targets = new HashMap<DungeonsPlayer,Location>();
		
		if (mobs == null)
		{
			mobs = new HashMap<String,String[]>();
			mobs.put("digging_shovelT1", new String[] {"digging_zombie1","digging_ghoul1",
					"digging_wolf1","digging_slime1","digging_bug1","digging_axe1","digging_defender1"});
			mobs.put("digging_shovelT2", new String[] {"digging_zombie2","digging_ghoul2","digging_wolf2",
					"digging_skeleton2","digging_slime2","digging_wolftamer2","digging_bug2","digging_axe2"
					,"digging_gremlin2,3","digging_defender2","digging_cow2"});
			
			items = new HashMap<String,ArrayList<String>>();
			ArrayList<String> t1 = new ArrayList<String>();
			addWeighted("digging_ancientstone",50,t1);
			addWeighted("digging_leather",35,t1);
			addWeighted("coins;125",15,t1);
			items.put("digging_shovelT1", t1);
			
			ArrayList<String> t2 = new ArrayList<String>();
			addWeighted("digging_ancientstone",100,t2);
			addWeighted("digging_leather",75,t2);
			addWeighted("digging_crystal",5,t2);
			addWeighted("coins;210",25,t2);
			addWeighted("coins;300",25,t2);
			addWeighted("coins;1000",5,t2);
			items.put("digging_shovelT2", t2);
		}
	}
	public void addWeighted(String item,int weight,ArrayList<String> list)
	{
		while (weight > 0)
		{
			list.add(item);
			weight--;
		}
	}
	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Digger");
		d.add("You can dig up treasures and tough mobs");
		d.add("Better shovels have better rewards");
		return d;
	}
	public void onTick  (DungeonsPlayer d) 
	{
		Location b = targets.get(d);
		if (b == null) 
		{
			b = site();
			targets.put(d, b);
		}
		if (b.distance(d.player.getLocation()) < 30)
		{
			d.player.spawnParticle(Particle.REDSTONE, s(b), 10, 0.25, 0, 0.25,0,new DustOptions (Color.ORANGE,0.8f));
		}
	}
	public static Location site()
	{
		Square[] sites = 
		{
				new Square(4  ,22038 ,7  ,22043 ,86),
				new Square(3  ,22052 ,1  ,22048 ,92),
				new Square(4  ,22060 ,7  ,22057 ,89),
				new Square(2  ,22053 ,-2 ,22055 ,87),
				new Square(-18,22065 ,-11,22056 ,87),
				new Square(3  ,22064 ,7  ,22069 ,91),
				new Square(6  ,22058 ,3  ,22059 ,93),
				new Square(-2 ,22062 ,-6 ,22059 ,83),
				new Square(5  ,22073 ,0  ,22078 ,77),
				new Square(-23,22070 ,-20,22075 ,87),
				new Square(-32,22066 ,-31,22073 ,88)
		};
		
		return RandomFunctions.get(sites).getRandom();
	}
	public static Location s(Location b)
	{
		return b.clone().add(0.5, 1.1, 0.5);
	}
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		Location b = targets.get(d);
		if (e.getAction() == Action.RIGHT_CLICK_AIR)
		{
			Location p = d.player.getEyeLocation().subtract(0, 0.3, 0);
			if (p.distance(s(b)) > 80) return false;
			if (b == null) return false;
			Vector v = s(b).subtract(p).toVector().normalize();
			while (p.distance(s(b)) > 1)
			{
				p = p.add(v);
				d.player.spawnParticle(Particle.REDSTONE, p, 3, 0, 0, 0,0,new DustOptions (Color.WHITE,0.8f));
			}
		}
		if (!e.hasBlock()) return false;
		if (!e.hasItem()) return false;
		// here is where we check the block
		
		if (!b.equals(e.getClickedBlock().getLocation())) return false;
		
		String tier = ItemBuilder.getItem(e.getItem());
		// temporary single loot table
		if (RandomFunctions.random(1, 4) == 1)
		{
			String ri = RandomFunctions.get(items.get(tier).toArray()).toString();
			new SoundTask(b,d.player,Sound.BLOCK_NOTE_BLOCK_PLING,0.5f,1.1f).runTaskLater(Dungeons.instance, 1);
			if (ri.split(";")[0].equals("coins"))
			{
				int amount = Integer.parseInt(ri.split(";")[1]);
				d.coins += amount;
				d.player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DIGGING!" + ChatColor.RESET + " " 
						+ ChatColor.GOLD + "You found " + amount + " coins!");
			}
			else
			{
				ItemStack item = ItemBuilder.i.build(ri, 1);
				InventoryHandler.addItem(d, item);
				d.player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DIGGING!" + ChatColor.RESET + " " 
						+ ChatColor.AQUA + "You found " + item.getItemMeta().getDisplayName() + "!");
			}
		}
		else
		{
			new SoundTask(b,d.player,Sound.BLOCK_NOTE_BLOCK_PLING,0.6f,1.4f).runTaskLater(Dungeons.instance, 1);
			String mob = (String) RandomFunctions.get(mobs.get(tier));
			int count = 1;
			if (mob.split(",").length > 1) count = Integer.parseInt(mob.split(",")[1]);
			DMobType type = DMobManager.types.get(mob.split(",")[0]);
			for (int c = 0; c < count; c++) DMobManager.spawn(mob.split(",")[0], new SpawnPosition(s(b)));
			d.player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DIGGING!" + ChatColor.RESET + " " 
									+ ChatColor.AQUA + "You dug up " + type.name + "!");
		}
		targets.put(d, site());
		return true;
	}
}
