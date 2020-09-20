package com.carterz30cal.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class RewardsGUI extends GUI
{

	public RewardsGUI(Player p) {
		super(MenuType.REWARDS,p);
	
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		ItemStack[] contents = new ItemStack[27];
		for (int i = 0; i < 27; i++)
		{
			if (d.rewardEligible()) contents[i] = GUICreator.pane(Material.GREEN_STAINED_GLASS_PANE);
			else contents[i] = GUICreator.pane(Material.RED_STAINED_GLASS_PANE);
		}
		inventory.setContents(contents);
	}

}
