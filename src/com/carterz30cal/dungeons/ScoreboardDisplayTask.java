package com.carterz30cal.dungeons;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class ScoreboardDisplayTask extends BukkitRunnable{

	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers())
		{
			DungeonsPlayer dp = DungeonsPlayerManager.i.get(p);
			dp.stats.refresh();
			dp.display.refresh();
		}
		
	}

}
