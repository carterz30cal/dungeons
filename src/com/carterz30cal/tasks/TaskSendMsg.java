package com.carterz30cal.tasks;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.player.DungeonsPlayer;

public class TaskSendMsg extends BukkitRunnable {

	public ArrayList<DungeonsPlayer> receivers;
	public String msg;
	
	public TaskSendMsg (ArrayList<DungeonsPlayer> r, String m,int seconds)
	{
		receivers = r;
		msg = m;
		
		runTaskLater(Dungeons.instance,seconds*20);
	}
	
	@Override
	public void run()
	{
		for (DungeonsPlayer d : receivers) d.player.sendMessage(msg);
	}

}
