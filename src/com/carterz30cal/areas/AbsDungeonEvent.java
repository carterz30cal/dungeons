package com.carterz30cal.areas;

import org.bukkit.event.player.PlayerInteractEvent;

import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public abstract class AbsDungeonEvent
{
	
	public AbsDungeonEvent()
	{
		EventTicker.events.add(this);
	}
	
	public void tick()
	{
		
	}
	
	public void end()
	{
		
	}
	
	public DMobType eventPreSpawn(int dhash, DMobType type)
	{
		return null;
	}
	
	public boolean eventInteract(PlayerInteractEvent e)
	{
		return false;
	}
	
	public void onPlayerDeath(DungeonsPlayer died)
	{
		
	}
}
