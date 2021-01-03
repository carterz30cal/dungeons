package com.carterz30cal.areas;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;

public class EventTicker extends BukkitRunnable
{
	public static ArrayList<AbsDungeonEvent> events;
	
	public EventTicker()
	{
		events = new ArrayList<AbsDungeonEvent>();
		runTaskTimer(Dungeons.instance,0,1);
	}
	
	
	@Override
	public void run()
	{
		for (AbsDungeonEvent e : events) e.tick();
	}

	public static void end()
	{
		for (AbsDungeonEvent e : events) e.end();
	}
}
