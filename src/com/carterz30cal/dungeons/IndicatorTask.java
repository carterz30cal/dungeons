package com.carterz30cal.dungeons;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class IndicatorTask extends BukkitRunnable
{
	public ArmorStand entity;
	public Location t;
	private boolean del;
	public IndicatorTask(ArmorStand e,Location l)
	{
		entity = e;
		t = l;
		del = false;
	}
	@Override
	public void run()
	{
		if (del)
		{
			entity.remove();
			this.cancel();
		} else {
			entity.teleport(t);
			del = true;
		}
	}
	
	public void clean()
	{
		entity.remove();
		this.cancel();
	}
}
