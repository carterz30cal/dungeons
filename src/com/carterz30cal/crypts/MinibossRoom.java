package com.carterz30cal.crypts;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.utility.RandomFunctions;

public class MinibossRoom extends CryptRoom
{
	private DMob miniboss;
	
	public MinibossRoom(int id, int ox, int oz, int[] c1, int[] c2, int ylevel, CryptLootTable l)
	{
		super(id, ox, oz, c1, c2, ylevel, l);
		
		type = CryptRoomType.MINIBOSS;
	}

	@Override
	public void register(CryptGenerator crypt)
	{
		int x = corner1[0] + ((corner2[0] - corner1[0]) / 2);
		int z = corner1[1] + ((corner2[1] - corner1[1]) / 2);
		miniboss = DMobManager.spawn(RandomFunctions.get(crypt.mobs.mobs.get(CryptRoomType.MINIBOSS)),new SpawnPosition(offset[0]+x,y,offset[1]+z),false);
		if (miniboss == null) 
		{
			register(crypt);
			return;
		}
		for (Entity e : miniboss.entities) 
		{
			LivingEntity mini = (LivingEntity)e;
			mini.setAI(false);
			mini.setInvulnerable(true);
		}
	}
	@Override
	public void destroy()
	{
		miniboss.remove();
	}
	@Override
	public void report()
	{
		System.out.println("[CRYPT] Miniboss: " + miniboss.type.name);
	}
	@Override
	public void activateMobs()
	{
		for (Entity e : miniboss.entities) 
		{
			LivingEntity mini = (LivingEntity)e;
			mini.setAI(true);
			mini.setInvulnerable(false);
		}
		for (DMob mob : mobs)
		{
			((LivingEntity)mob.entities.get(0)).setAI(true);
			((LivingEntity)mob.entities.get(0)).setInvulnerable(false);
		}
	}
	@Override
	public void addMobs(int ox,int oz,CryptMobs m)
	{
		if (activated) return;
		if (cleared) return;
		
		int mobc = 7;
		while (mobc > 0)
		{
			DMob mob = DMobManager.spawn("crypts1_defender", new SpawnPosition(
					ox+RandomFunctions.random(corner1[0]+1, corner2[0]-1), y, oz+RandomFunctions.random(corner1[1]+1, corner2[1]-1)));
			mob.entities.get(0).setSilent(true);
			((LivingEntity)mob.entities.get(0)).setAI(false);
			((LivingEntity)mob.entities.get(0)).setInvulnerable(true);
			mobs.add(mob);
			
			mobc--;
		}
		activated = true;
	}
}
