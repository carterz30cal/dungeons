package com.carterz30cal.bosses;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.carterz30cal.player.DungeonsPlayer;

public abstract class AbsBoss
{
	public AliveBossHandler abh;
	protected ArrayList<DungeonsPlayer> players;
	protected int stage;
	public int health;
	
	public AbsBoss()
	{
		players = new ArrayList<DungeonsPlayer>();
		stage = 0;
	}
	
	// variables
	public abstract int end();
	
	// states + boss fight beginning and end
	public abstract void entry();
	public abstract void transition();
	public abstract void exit();
	
	// tick.
	public abstract void tick();
	
	// triggers
	public abstract void projectileHit(BossProjectile p, Player t);
	public void hit()
	{
		
	}
	
	public void remove (DungeonsPlayer d)
	{
		if (!players.contains(d)) return;
		players.remove(d);
	}
}
