package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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
import com.carterz30cal.items.ability.AbsAbility;
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
			mobs.put("digging_shovelT1", new String[] {"digging1_zombie","digging1_raider",
					"digging1_wolf","digging1_slime","digging1_silverfish","digging1_axe","digging1_defender"});
			mobs.put("digging_shovelT2", new String[] {"digging2_zombie","digging2_raider",
					"digging2_wolf","digging2_slime","digging2_silverfish","digging2_axe","digging2_defender","digging2_cow"});
			mobs.put("digging_shovelT3", new String[] {"digging3_zombie","digging3_raider",
					"digging3_wolf","digging3_slime","digging3_silverfish,3","digging3_axe","digging3_defender",
					"digging3_cow","digging3_key","digging3_golem"});
			mobs.put("digging_shovelT4", new String[] {"digging4_zombie","digging4_raider",
					"digging4_wolf","digging4_slime","digging4_silverfish,4","digging4_axe","digging4_defender",
					"digging4_cow","digging4_key","digging4_golem","digging4_statue","digging4_phantom"});
			
			items = new HashMap<String,ArrayList<String>>();
			ArrayList<String> t1 = new ArrayList<String>();
			addWeighted("digging_coal",50,t1);
			addWeighted("digging_leather",25,t1);
			addWeighted("coins;400",25,t1);
			items.put("digging_shovelT1", t1);
			
			ArrayList<String> t2 = new ArrayList<String>();
			addWeighted("digging_stone2",50,t2);
			addWeighted("digging_leather",25,t2);
			addWeighted("coins;750",25,t2);
			items.put("digging_shovelT2", t2);
			
			ArrayList<String> t3 = new ArrayList<>();
			addWeighted("digging_stone2",50,t3);
			addWeighted("coins;900",40,t3);
			addWeighted("coins;1750",10,t3);
			items.put("digging_shovelT3", t3);
			
			ArrayList<String> t4 = new ArrayList<>();
			addWeighted("digging_stone2",50,t4);
			addWeighted("coins;1200",35,t4);
			addWeighted("coins;1900",15,t4);
			items.put("digging_shovelT4", t4);
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
		d.add("Right click to see where to dig!");
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
				new Square(4  ,22073 ,0  ,22078 ,77),
				new Square(-23,22070 ,-20,22075 ,87),
				new Square(-32,22066 ,-31,22073 ,88)
		};
		Location l = RandomFunctions.get(sites).getRandom();
		if (Dungeons.w.getBlockAt(l.clone().add(0,1,0)).getType() != Material.AIR) return site();
		else return l;
	}
	public static Location s(Location b)
	{
		return b.clone().add(0.5, 1.1, 0.5);
	}
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		Location b = targets.get(d);
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Location p = d.player.getEyeLocation().subtract(0, 0.3, 0);
			p.add(d.player.getLocation().getDirection().multiply(0.5));
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
		
		if (b == null || !b.equals(e.getClickedBlock().getLocation())) return false;
		
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
