package com.carterz30cal.crypts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.Mafs;
import com.carterz30cal.utility.RandomFunctions;

public class CryptGenerator
{
	public static final boolean floorPlan = false;
	
	public boolean complete;
	public int lx;
	public int ly;
	public int lz;
	
	public int sizex;
	public int sizez;
	
	public Location spawn;
	public int[][] crypt;
	// 0 = solid
	// 1 = passageway
	// 2.. = room
	
	public ArrayList<Block> removal;
	public CryptMobs mobs;
	public CryptLootTable table;
	public CryptBlocks blocks;
	public HashMap<Integer,CryptRoom> rooms;
	
	public boolean hasRune;
	public boolean hasGrave;
	public boolean hasMini;
	public boolean ready;
	public boolean started;
	
	private int step = 0; // this tells the generator what to do
	private int room = 2; // which room we're on
	private int attempts = 0; // failsafe so server doesn't crash lol
	
	private int centerx = 0;
	private int centerz = 0;
	
	// crypt specific parameters
	private int roomamount;
	private int lowerb;
	private int upperb;
	private int lfreq;
	private int deadends;
	private boolean[] valid;
	
	public CryptSettings settings;
	public CryptVariant variant;
	public int difficulty;
	
	private int generation;
	
	public CryptRoom entrance;
	
	public void step()
	{
		switch (step)
		{
		case 0:
			for (int c = 0; c < sizex;c++)
			{
				for (int v = 0; v < sizez;v++)
				{
					if (c == 0 || c == sizex-1 || v == 0 || v == sizez-1) crypt[c][v] = -3;
					else crypt[c][v] = 0;
				}
			}
			// generate pillars
			for (int p = 0; p < 8;p++)
			{
				int px = RandomFunctions.random(3, sizex - 3);
				int pz = RandomFunctions.random(3, sizez - 3);
				
				if (crypt[px][pz] == 0 && allowedCardinal(px,pz,0))
				{
					for (int kx = px-1;kx <= px+1;kx++) for (int kz = pz-1;kz <= pz+1;kz++) crypt[kx][kz] = -3;
				}
			}
			break;
		case 1:
			while (room-2 < roomamount && attempts < 750)
			{
				int rsx = RandomFunctions.random(lowerb, upperb);
				int rsz = RandomFunctions.random(lowerb, upperb);
				
				if (room == 3)
				{
					rsx = upperb + 2;
					rsz = upperb + 2;
				}
				else if ((room-2) % lfreq == 0)
				{
					rsx += 2;
					rsz += 2;
				}
				
				int rx = RandomFunctions.random(1, sizex-(rsx+2));
				int rz = RandomFunctions.random(1, sizez-(rsz+2));
				
				if (Mafs.manhatten(rx, rz, centerx, centerz) < 5) continue;
				
				boolean failed = false;
				for (int ix = rx; ix < rx+rsx && !failed;ix++)
				{
					for (int iz = rz; iz < rz+rsz && !failed;iz++)
					{
						failed = !allowed(ix,iz);
					}
				}
				if (failed) attempts++;
				else
				{
					CryptRoom r;
					if (room > 3 && RandomFunctions.random(0, 9) == 4 && !hasRune && settings.canHaveRune) 
					{
						r = new RuneRoom(room,lx,lz,new int[] {rx,rz},new int[] {rx+rsx-1,rz+rsz-1},ly+1,table,this);
						hasRune = true;
					}
					else if (room > 3 && RandomFunctions.random(1, 12) == 6 && !hasGrave)
					{
						r = new GraveRoom(room,lx,lz,new int[] {rx,rz},new int[] {rx+rsx-1,rz+rsz-1},ly+1,table);
						hasGrave = true;
					}
					else if (room == 3)
					{
						r = new MinibossRoom(room,lx,lz,new int[] {rx,rz},new int[] {rx+rsx-1,rz+rsz-1},ly+1,table);
						hasMini = true;
					}
					else r = new CryptRoom(room,lx,lz,new int[] {rx,rz},new int[] {rx+rsx-1,rz+rsz-1},ly+1,table);
					
					r.register(this);
					if (room == 2) 
					{
						spawn = new Location(Dungeons.w,lx + rx + (rsx/2),ly,lz + rz + (rsz/2));
						r.type = CryptRoomType.ENTRANCE;
						entrance = r;
						r.cleared = true;
					}
					else if (r.type == CryptRoomType.RUNE
							|| r.type == CryptRoomType.MINIBOSS
							|| r.type == CryptRoomType.GRAVESTONE) {}
					else if ((room-2) % lfreq == 0) r.type = CryptRoomType.LOOT;
					else
					{
						int roommm = RandomFunctions.random(1, 9);
						if (roommm == 1) r.type = CryptRoomType.NEST;
						else if (roommm == 2) r.type = CryptRoomType.WET;
					}
					//for (int ix = -1; ix <= rsx;ix++) for (int iz = -1; iz <= rsz;iz++) crypt[ix+rx][iz+rz] = -2;
					for (int ix = 0; ix < rsx;ix++) for (int iz = 0; iz < rsz;iz++) crypt[ix+rx][iz+rz] = room;
					rooms.put(room, r);
					room++;
				}
			}
			break;
		case 2:
			for (int fx = 1; fx < sizex-1;fx++)
			{
				for (int fz = 1; fz < sizez-1; fz++)
				{
					if (crypt[fx][fz] == 0 && allowed(fx,fz)) flood(fx,fz);
				}
			}
			break;
		case 3:
			// add doorways here
			HashMap<String,ArrayList<int[]>> connectors = new HashMap<String,ArrayList<int[]>>();
			valid = new boolean[room];
			for (int gx = 0; gx < sizex; gx++)
			{
				for (int gz = 0; gz < sizez; gz++)
				{
					if (crypt[gx][gz] != 0 && crypt[gx][gz] != -2) continue;
					String c = getCardinalConnection(gx,gz);
					if (c != null) 
					{
						valid[crypt[gx][gz]] = true;
						connectors.putIfAbsent(c, new ArrayList<int[]>());
						connectors.get(c).add(new int[] {gx,gz});
					}
				}
			}
			
			for (Entry<String,ArrayList<int[]>> connectionse : connectors.entrySet())
			{
				ArrayList<int[]> connections = connectionse.getValue();
				int count = 1;
				//if (connectionse.getKey().startsWith("1")) RandomFunctions.random(2, 5); a reminder of my own stupidity
				
				while (count > 0)
				{
					int[] connector = connections.get(RandomFunctions.random(0, connections.size()-1));
					crypt[connector[0]][connector[1]] = -1;
					
					connections.remove(connector);
					count--;
				}
				
			}
			break;
		case 4:
			for (int a = 0; a < deadends; a++)
			{
				for (int ax = 1; ax < sizex-1; ax++)
				{
					for (int az = 1; az < sizez-1; az++)
					{
						if (crypt[ax][az] == 1 && hasCardinal(ax,az) < 2) crypt[ax][az] = 0;
					}
				}
			}
			
			break;
		case 5:
			ArrayList<CryptRoom> bingbong = new ArrayList<CryptRoom>();
			for (CryptRoom r : rooms.values()) 
			{
				if (valid[r.id])
				{
					r.generateBarrels(lx,lz,removal);
				}
				else
				{
					System.out.println("[CRYPT] Removed invalid room");
					for (int kx = r.corner1[0]; kx <= r.corner2[0];kx++)
					{
						for (int kz = r.corner1[1]; kz <= r.corner2[1];kz++)
						{
							crypt[kx][kz] = 0;
						}
					}
					bingbong.add(r);
				}
			}
			for (CryptRoom r : bingbong) rooms.remove(r.id);
			break;
		case 6:
			for (int cax = 0; cax < sizex;cax++)
			{
				for (int caz = 0; caz < sizez;caz++)
				{
					if (hasPath(cax,caz,5)) crypt[cax][caz] = -4;
				}
			}
			break;
		case 7:
			generate();
			
			if (generation == sizex) break;
			else return;
		case 8:
			System.out.println("[CRYPT] Finished Generating!");
			System.out.println("[CRYPT] Rooms: " + (room-2));
			System.out.println("[CRYPT] Total Blocks: " + removal.size());
			if (variant != null) System.out.println("[CRYPT] Variant: " + variant.toString());
			for (CryptRoom r : rooms.values()) r.report();
			ready = true;
			return;
		}

		step++;
	}
	
	
	
	public CryptGenerator(int x,int difficulty,CryptSettings settings)
	{
		mobs = settings.mobs;
		table = settings.loot;
		blocks = settings.blocks;
		rooms = new HashMap<Integer,CryptRoom>();
		lx = x;
		ly = settings.y;
		lz = settings.z;
		sizex = settings.sizex;
		sizez = settings.sizez;
		crypt = new int[sizex][sizez];
		centerx = sizex / 2;
		centerz = sizez / 2;
		
		roomamount = settings.room_amount;

		lowerb = settings.room_lb;
		upperb = settings.room_ub;
		lfreq = settings.room_lootfreq;
		deadends = settings.deadends;
		
		removal = new ArrayList<>();
		this.difficulty = difficulty;
		this.settings = settings;
		
		if (RandomFunctions.random(1, 5) == 3) variant = RandomFunctions.get(CryptVariant.values());
		
		CryptBlocks updated;
		CryptMobs nmobs;
		CryptLootTable inject;
		Material[] walls;
		if (variant == null) return;
		switch (variant)
		{
		case CORRUPT:
			updated = new CryptBlocks(blocks);
			walls = new Material[updated.walls.length+3];
			for (int i = 0; i < updated.walls.length;i++) walls[i] = updated.walls[i];
			walls[walls.length-1] = Material.MAGENTA_TERRACOTTA;
			walls[walls.length-2] = Material.PURPLE_TERRACOTTA;
			walls[walls.length-3] = Material.CYAN_TERRACOTTA;

			updated.walls = walls;
			blocks = updated;
			
			nmobs = new CryptMobs(mobs);
			nmobs.mobs.put(CryptRoomType.NORMAL, new String[] {"crypt_corrupt_regular" + difficulty,"crypt_corrupt_soldier" + difficulty});
			nmobs.mobs.put(CryptRoomType.RUNE, new String[] {"crypt_corrupt_rune" + difficulty});
			
			mobs = nmobs;
			inject = new CryptLootTable(table);
			inject.add("corrupt_goo", 160);
			inject.itemsPerChest = new int[] {8,17};
			inject.init();
			table = inject;
			break;
		case LIVING:
			updated = new CryptBlocks(blocks);
			walls = new Material[updated.walls.length+3];
			for (int i = 0; i < updated.walls.length;i++) walls[i] = updated.walls[i];
			walls[walls.length-1] = Material.GREEN_TERRACOTTA;
			walls[walls.length-2] = Material.DIRT;
			walls[walls.length-3] = Material.COARSE_DIRT;
			updated.path = Material.ACACIA_PLANKS;
			updated.walls = walls;
			blocks = updated;
			
			nmobs = new CryptMobs(mobs);
			String[] addm = new String[nmobs.mobs.get(CryptRoomType.NORMAL).length+1];
			for (int a = 0; a < nmobs.mobs.get(CryptRoomType.NORMAL).length;a++) addm[a] = nmobs.mobs.get(CryptRoomType.NORMAL)[a];
			addm[addm.length-1] = "crypt_living_zombie" + difficulty;
			nmobs.mobs.put(CryptRoomType.NORMAL, addm);
			
			addm = new String[nmobs.mobs.get(CryptRoomType.LOOT).length+1];
			for (int a = 0; a < nmobs.mobs.get(CryptRoomType.LOOT).length;a++) addm[a] = nmobs.mobs.get(CryptRoomType.LOOT)[a];
			addm[addm.length-1] = "crypt_living_zombie" + difficulty;
			nmobs.mobs.put(CryptRoomType.LOOT, addm);
			
			roomamount += 2;
			mobs = nmobs;
			inject = new CryptLootTable(table);
			inject.add("crypts_livingamulet", 2);
			inject.add("book", 6,"growth,1");
			inject.add("book", 3,"vitals,2");
			inject.add("book", 1,"empowered,1");
			inject.add("gel", 250);
			inject.itemsPerChest = new int[] {11,24};
			inject.init();
			table = inject;
			break;
		default:
			break;
		}
	}
	private String getCardinalConnection(int x,int z)
	{
		// output should look like 1,2
		int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
		int first = 0;
		int second = 0;
		for (int[] d : directions)
		{
			int dx = x + d[0];
			int dz = z + d[1];
			if (dx < 0 || dz < 0 || dx == sizex || dz == sizez) continue;
			if (crypt[dx][dz] == 1 && first == 0) first = 1;
			else if (crypt[dx][dz] > 1 && second == 0) second = crypt[dx][dz];
			else if (crypt[dx][dz] > 1 && first == 0) first = crypt[dx][dz];
		}
		if (first != 0 && second != 0) return first + "," + second;
		else return null;
	}

	private void flood(int x,int y)
	{
		ArrayList<int[]> directions = new ArrayList<int[]>();
		if (RandomFunctions.random(1, 10) < 7)
		{
			directions.add(new int[] {1,0});
			directions.add(new int[] {-1,0});
			directions.add(new int[] {0,1});
			directions.add(new int[] {0,-1});
		}
		else
		{
			directions.add(new int[] {0,1});
			directions.add(new int[] {0,-1});
			directions.add(new int[] {1,0});
			directions.add(new int[] {-1,0});
			
		}
		//directions = RandomFunctions.shuffle(directions);
		
		if (x < 1 || y < 1 || x == sizex-1 || y == sizez-1 || crypt[x][y] != 0) return;
		crypt[x][y] = 1;
		while (directions.size() > 0)
		{
			int[] d = directions.get(0);
			int dx = x+d[0];
			int dz = y+d[1];
			if (dx < 0 || dz < 0 || dx >= sizex || dz >= sizez)
			{
				directions.remove(0);
				continue;
			}
			if (allowedCardinal(dx,dz,1) && allowed(dx,dz,3) && allowedIgnorePath(dx,dz))
			{
				flood(dx,dz);
			}
			directions.remove(0);
		}
		
	}
	private boolean allowedCardinal(int x,int z,int threshold)
	{
		int t = 0;
		if (x > 0) if (crypt[x-1][z] != 0) t++;
		if (x < sizex-1) if (crypt[x+1][z] != 0) t++;
		if (z > 0) if (crypt[x][z-1] != 0) t++;
		if (z < sizez-1) if (crypt[x][z+1] != 0) t++;
		return t <= threshold;
	}
	
	private int hasCardinal(int x,int z)
	{
		int t = 0;
		if (x > 0) if (crypt[x-1][z] != 0) t++;
		if (x < sizex-1) if (crypt[x+1][z] != 0) t++;
		if (z > 0) if (crypt[x][z-1] != 0) t++;
		if (z < sizez-1) if (crypt[x][z+1] != 0) t++;
		return t;
	}
	
	public void spawnNearby(DungeonsPlayer player)
	{
		if (entrance.box.isInside(player.player.getLocation().getBlockX(), player.player.getLocation().getBlockZ()))
		{
			player.canOpen = true;
			complete = false;
			return;
		}
		player.canOpen = false;
		complete = true;
		
		for (CryptRoom r : rooms.values())
		{
			r.check();
			if (player.player.getLocation().distance(r.midpoint()) < 20) r.addMobs(lx, lz, mobs);
			else if (player.player.getLocation().distance(r.midpoint()) > 35) r.removeMobs();
			
			if (!r.cleared) complete = false;
			
			if (r.box.isInside(player.player.getLocation().getBlockX(), player.player.getLocation().getBlockZ())) 
			{
				player.canOpen = r.cleared;
				r.activateMobs();
			}
		}
	}
	private boolean allowedIgnorePath(int x,int z)
	{
		for (int nx = x-1; nx <= x+1;nx++)
		{
			for (int nz = z-1;nz <= z+1;nz++)
			{
				if (nx >= 0 && nz >= 0 && nx < sizex && nz < sizez && crypt[nx][nz] > 1)
				{
					return false;
				}
			}
		}
		return true;
	}
	private boolean allowed(int x,int z,int threshold)
	{
		int t = 0;
		for (int nx = x-1; nx <= x+1;nx++)
		{
			for (int nz = z-1;nz <= z+1;nz++)
			{
				if (nx >= 0 && nz >= 0 && nx < sizex && nz < sizez && crypt[nx][nz] != 0) t++;
			}
		}
		return t <= threshold;
	}
	private boolean hasPath(int x,int z,int threshold)
	{
		int t = 0;
		for (int nx = x-1; nx <= x+1;nx++)
		{
			for (int nz = z-1;nz <= z+1;nz++)
			{
				if (nx >= 0 && nz >= 0 && nx < sizex && nz < sizez && crypt[nx][nz] == 1) t++;
			}
		}
		return t >= threshold;
	}
	private boolean allowed(int x,int z)
	{
		for (int nx = x-1; nx <= x+1;nx++)
		{
			for (int nz = z-1;nz <= z+1;nz++)
			{
				if (nx >= 0 && nz >= 0 && nx < sizex && nz < sizez && crypt[nx][nz] != 0)
				{
					return false;
				}
			}
		}
		return true;
	}
	public void generate()
	{
		int x = generation;
		for (int z = 0; z < sizez;z++)
		{
			if (floorPlan)
			{
				if (crypt[x][z] == 1) set(x,0,z,Material.OAK_PLANKS);
				else if (crypt[x][z] > 1) set(x,0,z,Material.STONE);
			}
			else
			{
				set(x,11-ly,z,Material.BLACK_CONCRETE);
				if (crypt[x][z] == 0) set(x,5,z,RandomFunctions.get(blocks.roof));
				else if (crypt[x][z] == -4) 
				{
					set(x,0,z,blocks.path);
					set(x,5,z,RandomFunctions.get(blocks.roof));
				}
				else if (crypt[x][z] == -3) for (int y = ly+5; y > 0; y--) set(x,y-ly,z,RandomFunctions.get(blocks.walls));
				else if (crypt[x][z] == -2) for (int y = 0; y < 6; y++) set(x,y,z,RandomFunctions.get(blocks.walls));
				else if (crypt[x][z] == -1)
				{
					set(x,0,z,RandomFunctions.get(blocks.floor));
					for (int y = ly-1; y > 0; y--) set(x,y-ly,z,RandomFunctions.get(blocks.walls));
					for (int y = 3; y < 6; y++) set(x,y,z,RandomFunctions.get(blocks.walls));
				}
				else if (crypt[x][z] == 1)
				{
					set(x,0,z,blocks.path);
					for (int y = ly-1; y >= 12; y--) set(x,y-ly,z,blocks.support);
					set(x,5,z,RandomFunctions.get(blocks.roof));
				}
				else 
				{
					CryptRoom r = rooms.get(crypt[x][z]);
					set(x,0,z,RandomFunctions.get(blocks.floor));
					if (r.type == CryptRoomType.WET && RandomFunctions.random(1, 6) == 1) set(x,0,z,Material.WATER);
					
					for (int chx = -1; chx <= 1;chx++) for (int chz = -1; chz <= 1;chz++) if (crypt[x+chx][z+chz] == 0) for (int y = ly+5; y > 0; y--) set(x+chx,y-ly,z+chz,RandomFunctions.get(blocks.walls));
					
					if (r.type == CryptRoomType.NEST)
					{
						for (int y = 1; y < 5;y++) if (RandomFunctions.random(1, 5) == 1
							&& Dungeons.w.getBlockAt(lx+x, ly+y, lz+z).getType() == Material.AIR) set(x,y,z,Material.COBWEB);
						set(x,3,z,RandomFunctions.get(blocks.roomroof));
					}
					else set(x,5,z,RandomFunctions.get(blocks.roomroof));
					for (int y = ly-1; y > 0; y--) set(x,y-ly,z,RandomFunctions.get(blocks.walls));
				}
			}
			
		}
		generation++;
	}
	private void set(int x,int y,int z,Material mat)
	{
		Block b = Dungeons.w.getBlockAt(lx+x,ly+y,lz+z);
		if (b.getType() == Material.AIR) b.setType(mat);
		removal.add(b);
	}
	public void endremove()
	{
		for (CryptRoom room : rooms.values()) 
		{
			room.check();
			room.removeMobs();
			room.destroy();
		}
		for (Block r : removal) r.setType(Material.AIR);
	}
	public void remove()
	{
		for (CryptRoom room : rooms.values()) 
		{
			room.check();
			room.removeMobs();
			room.destroy();
		}
		new BukkitRunnable()
		{
			int at = 0;

			@Override
			public void run() {
				int i;
				for (i = 0;i < 750 && i+at < removal.size()-1;i++)
				{
					Block r = removal.get(at+i);
					if (r.getType() == Material.BARREL) ((Barrel)r.getState()).getInventory().clear();
					r.setType(Material.AIR);
				}
				at += i;
				if (at + 1 == removal.size()) cancel();
			}
			
		}.runTaskTimer(Dungeons.instance, 1, 1);
	}
}
