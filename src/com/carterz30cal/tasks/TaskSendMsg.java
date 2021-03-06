package com.carterz30cal.tasks;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskSendMsg extends BukkitRunnable {

	public ArrayList<DungeonsPlayer> receivers;
	public String msg;
	
	public TaskSendMsg (ArrayList<DungeonsPlayer> r, String m,int seconds)
	{
		receivers = r;
		msg = m;
		
		runTaskLater(Dungeons.instance,seconds*20);
	}
	public TaskSendMsg (Player r, String m,int seconds)
	{
		receivers = new ArrayList<DungeonsPlayer>();
		receivers.add(DungeonsPlayerManager.i.get(r));
		msg = m;
		
		runTaskLater(Dungeons.instance,seconds*20);
	}
	
	@Override
	public void run()
	{
		for (DungeonsPlayer d : receivers) d.player.sendMessage(msg);
	}

}
