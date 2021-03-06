package com.carterz30cal.mobs.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;

public class MobSwap extends DMobAbility
{
	public int distance;
	public ItemStack swap;
	
	public List<DMob> swapped = new ArrayList<>();
	public MobSwap(FileConfiguration data, String path)
	{
		super(data, path);
		
		distance = data.getInt(path + ".distance", 10);
		swap = ItemBuilder.i.build(data.getString(path + ".swap", "BEDROCK"), 1);
		
	}
	
	public void tick(DMob mob)
	{
		Mob entity = (Mob)mob.entities.get(0);
		if (!(entity.getTarget() instanceof Player) || swapped.contains(mob)) return;
		
		if (entity.getLocation().distance(entity.getTarget().getLocation()) < distance)
		{
			entity.getEquipment().setItemInMainHand(swap);
			swapped.add(mob);
		}
	}
}
