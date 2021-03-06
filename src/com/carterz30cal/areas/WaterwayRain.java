package com.carterz30cal.areas;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.utility.ArmourstandFunctions;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class WaterwayRain extends AbsDungeonEvent
{
	
	private final static int offperiod = 60000;
	private final static int onperiod = 12000;
	
	private boolean on;
	private int ticks;
	
	private ArmorStand display;
	
	public static HashMap<DMobType,DMobType> eventmobs;
	public WaterwayRain()
	{
		super();
		display();
		
		eventmobs = new HashMap<DMobType,DMobType>();
		
		eventmobs.put(DMobManager.types.get("drenched1"),DMobManager.types.get("soaked1"));
		eventmobs.put(DMobManager.types.get("drenched2"),DMobManager.types.get("soaked2"));
		eventmobs.put(DMobManager.types.get("drenched3"),DMobManager.types.get("soaked3"));
	}
	private void display()
	{
		display = ArmourstandFunctions.create(new Location(Dungeons.w,-69,100.3,20993.3));
	}
	@Override
	public void tick()
	{
		if (!display.isValid()) display();
		// state switch
		if (on && ticks == onperiod || !on && ticks == offperiod)
		{
			ticks = 0;
			on = !on;
			Dungeons.w.setStorm(on);
		}
		
		ticks++;
		if (!on) display.setCustomName(ChatColor.GOLD +"It will rain in " + StringManipulator.time(offperiod-ticks,offperiod));
		else display.setCustomName(ChatColor.GOLD + "Raining for another " + StringManipulator.time(onperiod-ticks,onperiod));
	}
	
	@Override
	public DMobType eventPreSpawn(int dhash, DMobType spawn)
	{
		if (on && dhash == 1) return eventmobs.get(spawn);
		else return null;
	}
	
	@Override
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (e.getAction() != Action.RIGHT_CLICK_AIR) return false;
		if (!e.hasItem() || e.getItem() == null) return false;
		
		String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"");
		if (it.equals("rainbringer") && DungeonManager.i.hash(e.getPlayer().getLocation().getBlockZ()) == 1)
		{
			if (on) e.getPlayer().sendMessage(ChatColor.RED + "It's already raining!");
			else
			{
				e.getItem().setAmount(e.getItem().getAmount()-1);
				ticks = onperiod-6000;
				on = true;
				Dungeons.w.setStorm(true);
				return true;
			}
		}
		else if (it.equals("rainbringer")) e.getPlayer().sendMessage(ChatColor.RED + "You're not in Waterway!");
		return false;
	}
	
	@Override
	public void end()
	{
		Dungeons.w.setStorm(false);
		display.remove();
	}
}
