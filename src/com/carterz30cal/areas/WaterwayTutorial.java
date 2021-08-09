package com.carterz30cal.areas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.ArmourstandFunctions;

public class WaterwayTutorial extends AbsDungeonEvent
{ 
	public static final Location spawn = new Location(Dungeons.w,20.5,87,21033.5);
	public static final Location end = new Location(Dungeons.w,8,87,21025);
	public List<ArmorStand> displays = new ArrayList<>();
	
	public List<DMob> dummies = new ArrayList<>();
	public WaterwayTutorial()
	{
		super();
		
		set();
		setDummies();
	}
	public void set()
	{
		for (ArmorStand d : displays) d.remove();
		displays.clear();
		
		display(15,88,21033,"Welcome to mcExperiment!");
		display(15,87.7,21033,"Walk along the path to");
		display(15,87.4,21033,"learn how to play!");
		
		display(11,88,21029,"To begin, you will want");
		display(11,87.7,21029,"to purchase a Cheap");
		display(11,87.4,21029,"Sword from Tutorial");
		display(11,87.1,21029,"Steve to your left.");
		
		display(7,88,21033,"Use that sword to");
		display(7,87.7,21033,"kill a dummy!");
		
		display(2,88,21034,"Most mobs will drop useful");
		display(2,87.7,21034,"items that can craft items");
		display(2,87.4,21034,"for use in your adventure!");
		display(2,87.1,21034,"To find drops, open");
		display(2,86.8,21034,"your ingredient sack!");
		
		display(5,88.3,21023,"Crafting in mcExperiment");
		display(5,88,21023,"is very easy. Open the");
		display(5,87.7,21023,"recipe browser, insert");
		display(5,87.4,21023,"an ingredient and you're set!");
		display(5,87.1,21023,"Then you insert the items in the");
		display(5,86.8,21023,"correct pattern in the anvil menu");
		
		display(-1,88,21019,"To enchant your swords and");
		display(-1,87.7,21019,"armours, you need a book");
		display(-1,87.4,21019,"and an appropriate catalyst.");
		display(-1,87.1,21019,"These can be found from");
		display(-1,86.8,21019,"mobs, recipes and shops.");
		
		display(9,88,21017,"Finally, sharpeners and");
		display(9,87.7,21017,"runes can be applied to");
		display(9,87.4,21017,"swords using the 'Applying'");
		display(9,87.1,21017,"submenu of the Anvil.");
		
		display(8,88,21026,"You are ready to set out on");
		display(8,87.7,21026,"your adventure! Start by");
		display(8,87.4,21026,"fighting Drenched and");
		display(8,87.1,21026,"crafting a Water Blade!");
		
	}
	private void setDummies()
	{
		System.out.println("AREGRGRGRGHRHRH");
		dummies.add(DMobManager.spawn("tutorial_dummy", new SpawnPosition(9.5,87,21028.5)));

		dummies.add(DMobManager.spawn("tutorial_dummy", new SpawnPosition(7.5,87,21028.5)));

		dummies.add(DMobManager.spawn("tutorial_dummy", new SpawnPosition(5.5,87,21028.5)));

		dummies.add(DMobManager.spawn("tutorial_dummy", new SpawnPosition(3.5,87,21028.5)));
	}
	
	@Override
	public void tick()
	{
		boolean spawn = false;
		for (Player p : Bukkit.getOnlinePlayers()) 
		{
			DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
			if (p.getLocation().distance(end) < 1.2) d.warp("waterway");
			if (d.area.id != null && d.area.id.equals("waterway")) spawn = true;
		}
		if (dummies.isEmpty() && spawn) setDummies();
		dummies.removeIf( (DMob m) -> m == null || m.health < 1);
	}
	
	private void display(double x,double y,double z,String display)
	{
		ArmorStand d = ArmourstandFunctions.create(new Location(Dungeons.w,x+.5,y,z+.5));
		
		d.setCustomName(display);
		displays.add(d);
	}
	
	@Override
	public void end()
	{
		for (ArmorStand d : displays) d.remove();
		displays.clear();
	}
}
