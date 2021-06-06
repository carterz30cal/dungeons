package com.carterz30cal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;

public class CommandMaxItem implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0 instanceof Player && arg0.isOp())
		{
			ItemStack i = ((Player)arg0).getInventory().getItemInMainHand();
			Item dung_item = ItemBuilder.get(i);
			
			String finished_enchants = "";
			for (String et : EnchantManager.enchantments.keySet())
			{
				AbsEnchant enchant = EnchantManager.get(et);
				if (enchant.type().equals(dung_item.type) && enchant.max() > 0)
				{
					finished_enchants += et + "," + enchant.level + ";";
				}
			}
			finished_enchants = finished_enchants.substring(0, finished_enchants.length()-1);
			ItemMeta meta = i.getItemMeta();
			meta.getPersistentDataContainer().set(ItemBuilder.kEnchants, PersistentDataType.STRING, finished_enchants);
			i.setItemMeta(ItemBuilder.i.updateMeta(meta, null));
			return true;
		}
		return false;
	}

}
