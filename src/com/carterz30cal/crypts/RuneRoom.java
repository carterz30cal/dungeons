package com.carterz30cal.crypts;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.tasks.TaskRuneRoom;
import com.carterz30cal.utility.RandomFunctions;

public class RuneRoom extends CryptRoom
{
	public static final String[] runes = {"rune_blood","rune_snake","rune_square","rune_hunt","rune_tap"
			,"rune_sweat","rune_snow","rune_space"};
	public static final String[] runes_rare = {"rune_rich","rune_acid","rune_slayer","rune_holy","rune_steel"};
	public static final String[] runes_incredible = {"rune_void"};
	public ItemStack rune;
	public ArmorStand display;
	public TaskRuneRoom task;
	
	public boolean rare;
	public boolean incredible;
	
	private CryptGenerator owner;
	
	public RuneRoom(int id, int ox, int oz, int[] c1, int[] c2, int ylevel, CryptLootTable l,CryptGenerator owner)
	{
		super(id, ox, oz, c1, c2, ylevel, l);
		
		type = CryptRoomType.RUNE;
		task = new TaskRuneRoom(this);
		this.owner = owner;
		
		rare = RandomFunctions.random(0, 6) == 0;
		incredible = !rare && RandomFunctions.random(0, 20) == 10;
		
		if (owner.variant == CryptVariant.CORRUPT) rune = ItemBuilder.i.build("rune_space", 1);
		else if (owner.variant == CryptVariant.LIVING)
		{
			if (incredible) rune = ItemBuilder.i.build("rune_greaterhealing", 1);
			else rune = ItemBuilder.i.build("rune_healing", 1);
		}
		else if (rare) rune = ItemBuilder.i.build(RandomFunctions.get(runes_rare), 1);
		else if (incredible) rune = ItemBuilder.i.build(RandomFunctions.get(runes_incredible), 1);
		else rune = ItemBuilder.i.build(RandomFunctions.get(runes), 1);
	}

	@Override
	public void report()
	{
		System.out.println("[CRYPT] Rune: " + rune.getItemMeta().getDisplayName());
	}
	@Override
	public void addMobs(int ox,int oz,CryptMobs m)
	{
		if (activated || cleared) return;

		int mobc = RandomFunctions.random(2, 3);

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
	@Override
	public void check()
	{
		for (int m = 0; m < mobs.size();m++) if (mobs.get(m).health < 1) mobs.remove(m);
		
		if (mobs.size() == 0 && activated && !cleared) 
		{
			if (activated && mobs.size() == 0) cleared = true;
			Dungeons.w.dropItem(display.getEyeLocation(), rune);
		}
	}
	@Override
	public void destroy()
	{
		task.cancel();
		display.remove();
	}
	@Override
	public void removeMobs()
	{
		if (cleared) return;
		if (mobs != null) for (DMob m : mobs) m.destroy(null);
		
		activated = false;
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
		
		int[] ys = {0,3,4};
		Material m = Material.QUARTZ_BLOCK;
		if (owner.variant == CryptVariant.CORRUPT) m = Material.OBSIDIAN;
		else if (rare) m = Material.NETHER_BRICKS;
		else if (incredible) m = Material.OBSIDIAN;
		for (int n : ys)
		{
			set(cx  ,y+n,cz  ,m,removal);
			set(cx  ,y+n,cz+1,m,removal);
			set(cx+1,y+n,cz  ,m,removal);
			set(cx+1,y+n,cz+1,m,removal);
		}
	}
}
