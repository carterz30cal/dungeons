package com.carterz30cal.tasks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.mobs.MobAction;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskCombatTag extends BukkitRunnable
{
	public DungeonsPlayer p;
	public DungeonMob m;
	
	public TaskCombatTag(Player player,DungeonMob mob)
	{
		if (player != null) p = DungeonsPlayerManager.i.get(player);
		m = mob;
	}
	// an action can occur once every second (each action has different criteria)
	@Override
	public void run()
	{
		if (m.health <= 0 || m.type.actions == null || p.player.getLocation().distance(m.entity.getLocation()) > 25) 
		{
			m.tagged.remove(p.player);
			cancel();
			return;
		}
		for (MobAction action : m.type.actions)
		{
			boolean stop = false;
			for (String effect : action.effects)
			{
				if (stop) break;

				String data = effect.split(":")[1];
				String[] ds = data.split(",");
				switch (effect.split(":")[0])
				{
				case "distance":
					int distance = (int)Math.ceil(p.player.getLocation().distance(m.entity.getLocation()));
					if ((ds[0].equals("over") && distance < Integer.parseInt(ds[1]))
							|| (ds[0].equals("under") && distance >= Integer.parseInt(ds[1]))) stop = true;
					break;
				case "health":
					int healthpercent = (int)Math.ceil(((double)m.health/m.health())*100);
					
					if ((ds[0].equals("over") && healthpercent < Integer.parseInt(ds[1]))
							|| (ds[0].equals("under") && healthpercent >= Integer.parseInt(ds[1]))) stop = true;
					break;
				case "spawn":
					m.summonCaps.putIfAbsent(ds[0], 0);
					SpawnPosition s = new SpawnPosition(m.entity.getLocation());
					int count = Integer.parseInt(data.split(",")[1]);
					while (count > 0)
					{
						if (m.summonCaps.get(ds[0]) > Integer.parseInt(ds[2])) break;
						DungeonMobCreator.i.create(ds[0], s);
						count--;
						m.summonCaps.put(ds[0], m.summonCaps.get(ds[0])+1);
					}
					break;
				case "line":
					Location playloc = p.player.getEyeLocation().subtract(0, 0.5, 0);
					Particle particle = Particle.valueOf(data.split(",")[0]);
					int particles = Integer.parseInt(data.split(",")[1]);
					int particleDensity = Integer.parseInt(data.split(",")[2]);
					double xdist = (m.entity.getLocation().getX()-playloc.getX())/particles;
					double ydist = (m.entity.getLocation().getY()+1-playloc.getY())/particles;
					double zdist = (m.entity.getLocation().getZ()-playloc.getZ())/particles;
					
					int time = 0;
					while (particles > 0)
					{
						Location pos = new Location(playloc.getWorld(),
								playloc.getX()+(xdist*particles),
								playloc.getY()+(ydist*particles),
								playloc.getZ()+(zdist*particles));
						
						new TaskSpawnParticle(particle,pos,particleDensity).runTaskLater(Dungeons.instance, time);
						particles--;
						if (particles % 5 == 0) time++;
					}
					break;
				case "damage":
					p.player.damage(Integer.parseInt(data));
					p.damage(Integer.parseInt(data), false);
					break;
				case "heal":
					m.heal(Integer.parseInt(data));
					break;
				}
			}
		}

	}

}
