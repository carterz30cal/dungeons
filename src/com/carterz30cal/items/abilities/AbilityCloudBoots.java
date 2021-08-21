package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.tasks.TaskBlockReplace;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;

public class AbilityCloudBoots extends AbsAbility
{
	public static HashMap<DungeonsPlayer,Boolean> enabled;

	public static HashMap<DungeonsPlayer,Integer> manas;
	public AbilityCloudBoots ()
	{
		if (enabled == null) enabled = new HashMap<DungeonsPlayer,Boolean>();
		if (manas == null) manas = new HashMap<DungeonsPlayer,Integer>();
	}
	
	
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> des = new ArrayList<String>();
		des.add(prefix + "Feet of Clouds");
		des.add("Solid clouds form below your feet!");
		des.add("Uses " + ChatColor.BLUE + "5% of your mana" + ChatColor.GRAY + " per second");
		des.add(ChatColor.RED + "Crouch" + ChatColor.GRAY + " to toggle");
		return des;
	}
	@Override
	public void onSneak(DungeonsPlayer d)
	{
		enabled.put(d, !enabled.getOrDefault(d, true));
	}
	@Override
	public void onTick (DungeonsPlayer d)
	{
		if (d.player.getLocation().getBlockY() > 110) return;
		if (DungeonManager.i.hash(d.player.getLocation().getBlockZ()) == 1
				&& d.player.getLocation().getBlockX() > -1) return;
		if (d.inCrypt) return;
		if (!enabled.getOrDefault(d, true)) return;
		if (!d.playerHasMana() && d.player.getGameMode() == GameMode.SURVIVAL) return;
		if (d.getManaPercent() < 0.02) return;
		int mana = manas.getOrDefault(d, 0);
		if (mana == 20) 
		{
			int mn = (int) (d.stats.mana * 0.05);
			mn = Math.max(2, mn);
			d.useMana(mn);
			manas.put(d, 0);
		}
		else manas.put(d, ++mana);
		
		for (int x = -1; x <= 1; x++)
		{
			for (int z = -1; z <= 1; z++)
			{
				Block b = d.player.getLocation().subtract(x, 1, z).getBlock();
				
				if (!Dungeons.instance.blocks.containsKey(b) && b.getType() == Material.AIR)
				{
					TaskBlockReplace tbr = new TaskBlockReplace(b,Material.AIR);
					b.setType(Material.WHITE_WOOL);
					tbr.runTaskLater(Dungeons.instance, 12);
					
					Dungeons.instance.blocks.put(b, tbr);
				}
				else if (Dungeons.instance.blocks.containsKey(b) && b.getType() == Material.WHITE_WOOL)
				{
					TaskBlockReplace t = Dungeons.instance.blocks.get(b);
					t.cancel();
					
					Dungeons.instance.blocks.remove(b);
					TaskBlockReplace tbr = new TaskBlockReplace(b,Material.AIR);
					tbr.runTaskLater(Dungeons.instance, 12);
					Dungeons.instance.blocks.put(b, tbr);
				}
			}
		}
		if (d.player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR) d.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,10,10,true));
		
	} 
}
