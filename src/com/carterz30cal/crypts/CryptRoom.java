package com.carterz30cal.crypts;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.gui.ListenerGUIEvents;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.utility.BoundingBox;
import com.carterz30cal.utility.RandomFunctions;

public class CryptRoom
{
	public int id;
	public int[] offset;
	public int[] corner1; // lower corner
	public int[] corner2; // upper corner
	public BoundingBox box;
	public int y; //where enemies and barrels spawn
	public CryptRoomType type;
	public CryptLootTable loot;
	// determines the enemies and loot available
	// NORMAL = decent loot, regular enemies
	// ENTRANCE = no loot or enemies
	// LOOT = extra barrels
	public boolean connected;
	public boolean activated;
	public boolean cleared;
	public ArrayList<DMob> mobs;
	public ArrayList<Inventory> barrels;
	
	public CryptRoom(int id,int ox,int oz,int[] c1,int[] c2,int ylevel,CryptLootTable l)
	{
		type = CryptRoomType.NORMAL;
		corner1 = c1;
		corner2 = c2;
		y = ylevel;
		loot = l;
		offset = new int[] {ox,oz};
		
		box = new BoundingBox(new Location(Dungeons.w,ox+c1[0],y,oz+c1[1]),new Location(Dungeons.w,ox+c2[0],y,oz+c2[1]));
		
		mobs = new ArrayList<DMob>();
		barrels = new ArrayList<Inventory>();
	}
	public void register(CryptGenerator crypt)
	{
		return; //this doesn't do anything with a base room, but can be used by others to obtain extra data.
	}
	public void destroy()
	{
		return;
	}
	public void report()
	{
		return;
	}
	private void setLoot(Inventory inv)
	{
		int itemsRemaining = RandomFunctions.random(loot.itemsPerChest[0], loot.itemsPerChest[1]);
		while (itemsRemaining > 0)
		{
			int r = RandomFunctions.random(0, loot.randomizer);
			CryptLoot pick = loot.get(r);
			if (pick == null) continue;
			ItemStack item;
			if (pick.enchants == null) item = ItemBuilder.i.build(pick.item, 1);
			else item = ItemBuilder.i.build(pick.item,pick.enchants, 1);
			
			inv.setItem(RandomFunctions.random(0, 26), item);
			itemsRemaining--;
		}
	}
	public Location midpoint() 
	{
		return new Location(Dungeons.w,offset[0] + (corner1[0] + ((corner2[0]-corner1[0])/2)),y,offset[1] + (corner1[1] + ((corner2[1]-corner1[1])/2)));
	}
	public void check()
	{
		for (int m = 0; m < mobs.size();m++) if (mobs.get(m).health < 1) mobs.remove(m);
		if (activated && mobs.size() == 0) cleared = true;
		if (mobs.size() == 0) for (Inventory b : barrels) ListenerGUIEvents.cancelled.remove(b);
	}
	public void activateMobs()
	{
		for (DMob mob : mobs)
		{
			((LivingEntity)mob.entities.get(0)).setAI(true);
			((LivingEntity)mob.entities.get(0)).setInvulnerable(false);
		}
	}
	public void removeMobs()
	{
		if (cleared) return;
		if (!activated) return;
		if (mobs.size() == 0) return;
		for (DMob m : mobs) m.destroy(null);
		activated = false;
	}
	public void addMobs(int ox,int oz,CryptMobs m)
	{
		if (activated) return;
		if (cleared) return;
		if (type == CryptRoomType.ENTRANCE) return;
		int mobc = RandomFunctions.random(3, 5);
		if (type == CryptRoomType.LOOT) mobc = RandomFunctions.random(5, 8);
		while (mobc > 0)
		{
			DMob mob = DMobManager.spawn(RandomFunctions.get(m.mobs.get(type)), new SpawnPosition(
					ox+RandomFunctions.random(corner1[0]+1, corner2[0]-1), y, oz+RandomFunctions.random(corner1[1]+1, corner2[1]-1)));
			mob.entities.get(0).setSilent(true);
			((LivingEntity)mob.entities.get(0)).setAI(false);
			((LivingEntity)mob.entities.get(0)).setInvulnerable(true);
			mobs.add(mob);
			
			mobc--;
		}
		activated = true;
	}
	public void generateBarrels(int ox,int oz,ArrayList<Block> removal)
	{
		if (type == CryptRoomType.ENTRANCE) return;
		int barrelCount = RandomFunctions.random(1,2);
		if (type == CryptRoomType.LOOT) barrelCount = RandomFunctions.random(3,5);
		else if (type == CryptRoomType.MINIBOSS) barrelCount = RandomFunctions.random(5,6);
		
		while (barrelCount > 0)
		{
			Block b = Dungeons.w.getBlockAt(ox+RandomFunctions.random(corner1[0]+1, corner2[0]-1), y, oz+RandomFunctions.random(corner1[1]+1, corner2[1]-1));
			if (b.getType() == Material.AIR)
			{
				b.setType(Material.BARREL);
				Barrel s = (Barrel)b.getState();
				Directional d = (Directional)b.getBlockData();
				d.setFacing(BlockFace.UP);
				b.setBlockData(d);
				setLoot(s.getInventory());
				removal.add(b);
				barrels.add(s.getInventory());
				barrelCount--;
				ListenerGUIEvents.cancelled.add(s.getInventory());
			}
		}
	}
}
