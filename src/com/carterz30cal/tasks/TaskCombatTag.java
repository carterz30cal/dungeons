package com.carterz30cal.tasks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.mobs.MobAction;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskCombatTag extends BukkitRunnable
{
	public DungeonsPlayer p;
	public DungeonMob m;
	
	public TaskCombatTag(Player player,DungeonMob mob)
	{
		p = DungeonsPlayerManager.i.get(player);
		m = mob;
	}
	// an action can occur once every second (each action has different criteria)
	@Override
	public void run()
	{
		if (m.health() <= 0 || m.type.actions == null || p.player.getLocation().distance(m.entity.getLocation()) > 25) 
		{
			m.tagged.remove(p.player);
			cancel();
			return;
		}
		for (MobAction action : m.type.actions)
		{
			for (String effect : action.effects.keySet())
			{
				String data = action.effects.get(effect);
				switch (effect)
				{
				case "line":
					Location playloc = p.player.getEyeLocation().subtract(0, 0.5, 0);
					Particle particle = Particle.valueOf(data.split(",")[0]);
					int particles = Integer.parseInt(data.split(",")[1]);
					double xdist = (m.entity.getLocation().getX()-playloc.getX())/particles;
					double ydist = (m.entity.getLocation().getY()+1-playloc.getY())/particles;
					double zdist = (m.entity.getLocation().getZ()-playloc.getZ())/particles;
					
					while (particles > 0)
					{
						Location pos = new Location(playloc.getWorld(),
								playloc.getX()+(xdist*particles),
								playloc.getY()+(ydist*particles),
								playloc.getZ()+(zdist*particles));
						playloc.getWorld().spawnParticle(particle, pos, 3);
						particles--;
					}
					break;
				case "damage":
					p.player.damage(Integer.parseInt(data));
					p.damage(Integer.parseInt(data), true);
					break;
				case "heal":
					m.heal(Integer.parseInt(data));
					break;
				}
			}
		}

	}

}
