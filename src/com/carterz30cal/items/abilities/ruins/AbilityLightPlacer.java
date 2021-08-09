package com.carterz30cal.items.abilities.ruins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.BoundingBox;
import com.carterz30cal.dungeons.Dungeons;

public class AbilityLightPlacer extends AbsAbility 
{
	private static BoundingBox box = new BoundingBox(new Location(Dungeons.w,-200,0,23800),new Location(Dungeons.w,200,255,24200));
	private static Map<DungeonsPlayer,Integer> cooldown = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Light!");
		d.add("Right click to place a solid block of ");
		d.add("light on the ground. Only works in the ruins.");
		return d;
	}
	public void onTick  (DungeonsPlayer d) 
	{
		cooldown.put(d, cooldown.getOrDefault(d, 0) - 1);
	}
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (box.isInside(d.player.getLocation()) && e.getAction() == Action.RIGHT_CLICK_BLOCK && cooldown.getOrDefault(d, 0) <= 0)
		{
			Block b = Dungeons.w.getBlockAt(e.getClickedBlock().getLocation().add(e.getBlockFace().getDirection()));
			if (b.getType() != Material.AIR) return false;
			b.setType(Material.SEA_LANTERN);
			cooldown.put(d, 4);
		}
		return false;
	}
}
