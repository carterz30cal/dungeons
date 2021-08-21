package com.carterz30cal.items.abilities.ruins;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.StdUtils;

import net.md_5.bungee.api.ChatColor;

public class AbilityAbyssalHopper extends AbsAbility
{
	public static List<DungeonsPlayer> onCooldown = new ArrayList<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Eye for an Eye");
		d.add("Right click to turn to the abyss, consuming 25%");
		d.add("of your life to regain all of your mana.");
		d.add(ChatColor.RED + "15s cooldown.");
		return d;
	}

	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && !onCooldown.contains(d))
		{
			d.setHealth(d.getHealthPercent()-0.25);
			d.damage(1, true);
			StdUtils.damageAnim(d);
			d.gainMana(1000000);
			onCooldown.add(d);
			new BukkitRunnable()
			{

				@Override
				public void run() {
					onCooldown.remove(d);
					d.player.sendMessage(ChatColor.GREEN + "You may turn to the abyss once more.");
				}
				
			}.runTaskLater(Dungeons.instance, 300);
			return true;
		}
		return false;
	}

}
