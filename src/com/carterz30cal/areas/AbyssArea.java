package com.carterz30cal.areas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.utility.OpenSimplex2;
import com.carterz30cal.utility.RandomFunctions;
import com.carterz30cal.utility.StdUtils;

public class AbyssArea extends AbsDungeonEvent 
{
	private final static double SCALE = 0.006;
	
	private Material[] environment = {Material.BLACK_WOOL,Material.GRAY_TERRACOTTA,Material.BLACK_CONCRETE,Material.GRAY_CONCRETE};
	private byte[] environ_data = {15,7,15,7};
	
	private Material[] pillar = {Material.STONE_BRICKS,Material.STONE,Material.COBBLESTONE};
	
	private List<String> loot = new ArrayList<>();
	
	private OpenSimplex2 noise;
	public AbyssArea()
	{
		super();
		noise = new OpenSimplex2(new Random().nextInt());
		
		register("light_shard",100);
		register("light_blade",1);
		register("light_bow",1);
		register("luxium",15);
		
		
		generate();
	}
	
	private void register(String item,int weight)
	{
		for (int w = 0;w < weight;w++) loot.add(item);
	}
	
	public void generate()
	{
		System.out.println("generating area!");
		for (int x = -200;x < 200;x++)
		{
			for (int z = 23800;z < 24200;z++)
			{
				if (x == -200 || x == 199 || z == 23800 || z == 24199) for (int y = 0; y < 150;y++) set(x,y,z,Material.BEDROCK);
				else
				{
					int h = height(x,z);
					int c = ceiling(x,z);
					
					for (int y = h; y >= 0;y--) 
					{
						int d = RandomFunctions.random(0, environment.length-1);
						set(x,y,z,environment[d],environ_data[d]);
					}
					for (int y = c; y < 150;y++)
					{
						int d = RandomFunctions.random(0, environment.length-1);
						set(x,y,z,environment[d],environ_data[d]);
					}
				}
				
				set(x,0,z,Material.BEDROCK);
				set(x,151,z,Material.BEDROCK);
			}
		}
		System.out.println("generating ravines!");
		for (int ravine = 0; ravine < 25;ravine++)
		{
			Vector direction = new Vector(r(),0,r());
			int length = RandomFunctions.random(6, 14);
			
			int x = RandomFunctions.random(-180,180);
			int z = RandomFunctions.random(23820, 24180);
			int depth = RandomFunctions.random(15, 25);
			int width = RandomFunctions.random(2, 4);
			for (int l = 0; l < length;l++)
			{
				int w = width;
				if (l == 0 || l == length - 1) w = 1;
				for (int cax = x; cax < x+w;cax++)
				{
					for (int caz = z; caz < z+w;caz++)
					{
						for (int y = height(cax,caz); y > depth;y--) 
						{
							if (y < depth+3) Dungeons.w.getBlockAt(cax, y, caz).setType(Material.LAVA);
							else set(cax,y,caz,Material.AIR);
						}
					}
				}
				
				x += direction.getBlockX();
				z += direction.getBlockZ();
			}
		}
		
		System.out.println("generating spikes!");
		for (int x = -200;x < 200;x++)
		{
			for (int z = 23800;z < 24200;z++)
			{
				double height = noise.noise2(x*0.06, z*0.06);
				if (height > 0.8)
				{
					int h = (int) (height(x,z) + ((height-0.8)*10*13));
					for (int y = h; y >= 0;y--) 
					{
						set(x,y,z,Material.BEDROCK,(byte)0);
					}
				}
			}
		}
		System.out.println("generating ores!");
		// ore generation
		for (int o = 0; o < 500;o++)
		{
			int x = RandomFunctions.random(-180,180);
			int y = RandomFunctions.random(12,140);
			int z = RandomFunctions.random(23820, 24180);
			int r = RandomFunctions.random(2, 5) + (y/50);
			if (Dungeons.w.getBlockAt(x, y, z).getType() == Material.AIR || Dungeons.w.getBlockAt(x, y, z).getType() == Material.BEDROCK) continue;
			
			for (int cx = x-r;cx < x+r;cx++)
			{
				for (int cz = z-r;cz < z+r;cz++)
				{
					for (int cy = y-r;cy < y+r;cy++)
					{
						double dist = Math.pow(cx-x, 2) + Math.pow(cy-y, 2) + Math.pow(cz-z, 2);
						if (dist < Math.pow(r, 2) && Dungeons.w.getBlockAt(cx, cy, cz).getType() != Material.AIR)
						{
							if ((int)Math.sqrt(dist) == r-1) set(cx,cy,cz,Material.WHITE_WOOL);
							else set(cx,cy,cz,Material.WHITE_STAINED_GLASS);
						}
					}
				}
			}
			
			// glowstone in the ores
			for (int l = 0; l < 21 + (y/20);l++)
			{
				int lx = x + RandomFunctions.random(-r, r);
				int ly = y + RandomFunctions.random(-r, r);
				int lz = z + RandomFunctions.random(-r, r);
				if (Dungeons.w.getBlockAt(lx, ly+1, lz).getType() != Material.WHITE_STAINED_GLASS) continue;
				if (Dungeons.w.getBlockAt(lx-1, ly, lz).getType() == Material.AIR
						|| Dungeons.w.getBlockAt(lx+1, ly, lz).getType() == Material.AIR
						|| Dungeons.w.getBlockAt(lx, ly-1, lz).getType() == Material.AIR
						|| Dungeons.w.getBlockAt(lx, ly+1, lz).getType() == Material.AIR
						|| Dungeons.w.getBlockAt(lx, ly, lz-1).getType() == Material.AIR
						|| Dungeons.w.getBlockAt(lx, ly, lz+1).getType() == Material.AIR) continue;
				
				 Dungeons.w.getBlockAt(lx, ly, lz).setType(Material.GLOWSTONE);
			}
		}
		System.out.println("generating pillars!");
		for (int p = 0; p < 75;p++)
		{
			int px = RandomFunctions.random(-180, 180);
			int pz = RandomFunctions.random(23850, 24150);
			int w = RandomFunctions.random(-2, 2);
			
			for (int y = 1; y < 150;y++) 
			{
				double v = Math.abs((-150 + (y*2))/150d);
				int width = (int) (v*5)+3+w;
				for (int xx = px-width; xx <= px+width;xx++)
				{
					for (int zz = pz-width; zz <= pz+width;zz++)
					{
						set(xx,Math.max(0, Math.min(150, y+(px-xx)+(pz-zz))),zz,RandomFunctions.get(pillar));
					}
				}
				
			}
		}
		
		System.out.println("generating loot chests!");
		for (int barrel = 0; barrel < 80;barrel++)
		{
			int px = RandomFunctions.random(-180, 180);
			int pz = RandomFunctions.random(23850, 24150);
			
			int h = height(px,pz);
			if (Dungeons.w.getBlockAt(px, h, pz).getType() == Material.AIR || Dungeons.w.getBlockAt(px, h+1, pz).getType() != Material.AIR) continue;
			
			org.bukkit.block.Block chest = Dungeons.w.getBlockAt(px, h, pz);
			
			ItemStack[] contents = new ItemStack[27];
			for (int i = 0; i < 14;i++) contents[RandomFunctions.random(0, 26)] = ItemBuilder.i.build((String) RandomFunctions.get(loot.toArray()), 1);
			
			chest.setType(Material.CHEST);
			((Chest)chest.getState()).getBlockInventory().setContents(contents);
		}
		
		System.out.println("generating the ruins!");
		for (int ruin = 0; ruin < 4;ruin++)
		{
			int px = RandomFunctions.random(-180, 180);
			int pz = RandomFunctions.random(23850, 24150);
			int sizex = RandomFunctions.random(8, 13);
			int sizez = RandomFunctions.random(8, 13);
			
			int rc = RandomFunctions.random(4, 9);
			boolean door = false;
			for (int room = 1; room <= rc;room++)
			{
				for (int rx = px; rx < px+sizex;rx++)
				{
					for (int rz = pz; rz < pz+sizez;rz++)
					{
						for (int ry = 10*room;ry < 10*(room+1);ry++) set(rx,ry,rz,RandomFunctions.get(pillar));
					}
				}
				for (int rx = px+1; rx < px+sizex-1;rx++)
				{
					for (int rz = pz+1; rz < pz+sizez-1;rz++)
					{
						for (int ry = (10*room) + 1;ry < (10*(room+1))-1;ry++) set(rx,ry,rz,Material.AIR);
					}
				}
				if (room > rc/2)
				{
					Dungeons.w.getBlockAt(px+2,(10*(room+1))-1,pz+2).setType(Material.SEA_LANTERN);
					Dungeons.w.getBlockAt(px+2,(10*(room+1))-1,pz+sizez-3).setType(Material.SEA_LANTERN);
					Dungeons.w.getBlockAt(px+sizex-3,(10*(room+1))-1,pz+2).setType(Material.SEA_LANTERN);
				}
				
				
				
				if (10*room >= height(px+1,pz) && !door)
				{
					door = true;
					set(px+1,(10*room) + 1,pz,Material.AIR);
					set(px+2,(10*room) + 1,pz,Material.AIR);
					set(px+1,(10*room) + 2,pz,Material.AIR);
					set(px+2,(10*room) + 2,pz,Material.AIR);
					set(px+1,(10*room) + 3,pz,Material.AIR);
					set(px+2,(10*room) + 3,pz,Material.AIR);
				}
				if (room == rc) for (int ry = 10*room;ry < 10*(room+1)-1;ry++) set(px+sizex-2,ry,pz+sizez-2,Material.LADDER);
				else if (room == 1) for (int ry = (10*room)+1;ry < 10*(room+1);ry++) set(px+sizex-2,ry,pz+sizez-2,Material.LADDER);
				else for (int ry = 10*room;ry < 10*(room+1);ry++) set(px+sizex-2,ry,pz+sizez-2,Material.LADDER);
			}
		}
	}

	private int r()
	{
		int r = RandomFunctions.random(1, 2);
		if (r == 1) return -1;
		else return 1;
	}
	private int manhatten(int x,int z)
	{
		return Math.abs(x) + Math.abs(24000-z);
	}
	
	public int ceiling(int x, int z)
	{
		double weight = 0;
		
		weight = (noise.noise2(x*SCALE*7, z*SCALE*7)+1) / 2;
		
		return 150 - (int)(34*weight);
	}
	public int height(int x, int z)
	{
		double weight = 0;
		
		for (int i = 1; i < 6;i++)
		{
			weight += (noise.noise2(x*SCALE*i, z*SCALE*i)+1) / 2 / i;
		}
		weight += manhatten(x,z)/300d;
		return 6 + (int)Math.max(42, 43*weight);
	}
	
	public void end()
	{
		for (int x = -200;x < 200;x++)
		{
			for (int z = 23800;z < 24200;z++)
			{
				for (int y = 0; y < 150;y++) 
				{
					if (Dungeons.w.getBlockAt(x, y, z).getType() == Material.CHEST) ((Chest)Dungeons.w.getBlockAt(x, y, z).getState()).getBlockInventory().clear();
					set(x,y,z,Material.AIR);
				}
			}
		}
	}
	
	private void set(double x,double y,double z,Material m)
	{
		StdUtils.setBlock((int)x, (int)y, (int)z, m, (byte)0);
	}
	private void set(double x,double y,double z,Material m,byte d)
	{
		StdUtils.setBlock((int)x, (int)y, (int)z, m, d);
	}
}
