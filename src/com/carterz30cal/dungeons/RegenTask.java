package com.carterz30cal.dungeons;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class RegenTask extends BukkitRunnable {

	@Override
	public void run()
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p.getFireTicks() > 0) continue;
			DungeonsPlayer dp = DungeonsPlayerManager.i.get(p);
			//int heal = (int)Math.round(((double)dp.stats.health)*0.01) + dp.stats.regen;
			int heal = dp.stats.regen;
			dp.heal(heal);
		}
	}

}
