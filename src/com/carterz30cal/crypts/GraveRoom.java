package com.carterz30cal.crypts;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.utility.ParticleFunctions;
import com.carterz30cal.utility.RandomFunctions;

public class GraveRoom extends CryptRoom
{
	private int difficulty;
	
	private int[] plasma = {1,3,9,4};
	
	public GraveRoom(int id, int ox, int oz, int[] c1, int[] c2, int ylevel, CryptLootTable l) {
		super(id, ox, oz, c1, c2, ylevel, l);
		
		type = CryptRoomType.GRAVESTONE;
	}
	@Override
	public void register(CryptGenerator crypt)
	{
		difficulty = crypt.difficulty;
		return; //this doesn't do anything with a base room, but can be used by others to obtain extra data.
	}
	
	@Override
	public void addMobs(int ox,int oz,CryptMobs m)
	{
		if (activated && !cleared)
		{
			ParticleFunctions.stationary(new Location(Dungeons.w,midpoint().getBlockX() + RandomFunctions.random(-3, 3),y + RandomFunctions.random(0, 3),midpoint().getBlockZ() + RandomFunctions.random(-3, 3)), Particle.ASH, 1);
			return;
		}
		else if (cleared) return;

		DMob mob = DMobManager.spawn("crypt_grave" + difficulty, new SpawnPosition(
				midpoint().getBlockX()+2, y, midpoint().getBlockZ() + 0.5));
		mob.entities.get(0).setSilent(true);
		((LivingEntity)mob.entities.get(0)).setAI(false);
		((LivingEntity)mob.entities.get(0)).setInvulnerable(true);
		mobs.add(mob);
		
		activated = true;
	}
	@Override
	public void check()
	{
		for (int m = 0; m < mobs.size();m++) if (mobs.get(m).health < 1) mobs.remove(m);
		
		if (mobs.size() == 0 && activated && !cleared) 
		{
			int cx = midpoint().getBlockX();
			int cz = midpoint().getBlockZ();
			if (activated && mobs.size() == 0) cleared = true;
			Dungeons.w.dropItem(new Location(Dungeons.w,cx+1.5,y,cz + 0.5), ItemBuilder.i.build("crypts_plasma", plasma[difficulty-1]));
			if (difficulty == 3) Dungeons.w.dropItem(new Location(Dungeons.w,cx+1.5,y,cz + 0.5), ItemBuilder.i.build("crypts_exotic", 1));
		}
	}
	
	@Override
	public void report()
	{
		System.out.println("[CRYPT] Has Gravestone Room");
	}
	public void set(int x,int ly,int z,Material m, ArrayList<Block> removal)
	{
		Block b = Dungeons.w.getBlockAt(x, ly, z);
		if (b.getType() == Material.AIR)
		{
			b.setType(m);
			removal.add(b);
		}
	}
	@Override
	public void generateBarrels(int ox,int oz,ArrayList<Block> removal)
	{
		int cx = midpoint().getBlockX();
		int cz = midpoint().getBlockZ();
		
		set(cx+1,y-1,cz,Material.COARSE_DIRT,removal);
		set(cx+2,y-1,cz,Material.COARSE_DIRT,removal);
		set(cx,y,cz,Material.BEDROCK,removal);
		set(cx,y+1,cz,Material.BEDROCK,removal);
	}
}
