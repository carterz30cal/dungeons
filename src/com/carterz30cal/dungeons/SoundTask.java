package com.carterz30cal.dungeons;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SoundTask extends BukkitRunnable
{
	public Location l;
	public Player p;
	public Sound s;
	public float v;
	public float pi;
	
	public SoundTask(Location location,Player player,Sound sound,float volume,float pitch)
	{
		l = location;
		p = player;
		s = sound;
		v = volume;
		pi = pitch;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		p.playSound(l, s, v, pi);
	}
}
