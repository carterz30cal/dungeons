package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.RandomFunctions;

import net.md_5.bungee.api.ChatColor;

public class AbilityEggHat extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Eggs");
		d.add("Sometimes gives you a spider egg");
		d.add("when killing a spider egg.");
		return d;
	}

	public void onKill (DungeonsPlayer d,DMobType mob) 
	{
		if (mob.tags.contains("spideregg") && RandomFunctions.random(1, 100) == 50)
		{
			InventoryHandler.addItem(d, ItemBuilder.i.build("spider_egg",1));
			d.player.sendMessage(ChatColor.GREEN + "Your Egg Hat gave you a spider egg!");
		}
	}
}
