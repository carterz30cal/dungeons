package com.carterz30cal.bosses;

import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DungeonMob;

public class AliveBossHandler extends BukkitRunnable
{
	public AbsBoss boss;
	public DungeonMob rep;
	
	public AliveBossHandler()
	{
		runTaskTimer(Dungeons.instance,1,1);
	}
	@Override
	public void run()
	{
		boss.tick();
	}

}
