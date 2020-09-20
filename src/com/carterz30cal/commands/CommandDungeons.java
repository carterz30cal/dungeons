package com.carterz30cal.commands;


import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.gui.GUI;
import com.carterz30cal.gui.MenuType;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.Dungeon;
import com.carterz30cal.mobs.DungeonManager;
import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class CommandDungeons implements CommandExecutor
{
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 0) return false;
		switch (args[0].toLowerCase()) {
			case "version":
				sender.sendMessage("currently running v1.0.0");
				break;
			case "menu":
				if (sender instanceof Player)
				{
					Player ps = (Player) sender;
					new GUI(MenuType.MAINMENU,ps);
				} else {
					sender.sendMessage("this command is executable by players only.");
				}
				break;
			case "clearbackpack":
				if (args.length == 1) return false;
				Player pl = Bukkit.getPlayer(args[1]);
				DungeonsPlayer dpl = DungeonsPlayerManager.i.get(pl);
				dpl.backpack = new BackpackItem[54];
				break;
			case "removeperk":
				if (args.length < 3) return false;
				Player pp = Bukkit.getPlayer(args[1]);
				DungeonsPlayer ppl = DungeonsPlayerManager.i.get(pp);
				ppl.perks.remove(args[2]);
				break;
			case "coins":
				if (args.length == 1) return false;
				if (args.length == 2)
				{
					if (!(sender instanceof Player)) return false;
					DungeonsPlayer d = DungeonsPlayerManager.i.get((Player)sender);
					d.coins += Integer.parseInt(args[1]);
				}
				else
				{
					DungeonsPlayer d = DungeonsPlayerManager.i.get(Bukkit.getPlayerExact(args[2]));
					d.coins += Integer.parseInt(args[1]);
				}
				break;
			case "item":
				if (args.length == 1) return false;
				if (sender instanceof Player)
				{
					Player p = (Player)sender;
					ItemStack item = null;
					switch (args.length)
					{
					case 2:
						item = ItemBuilder.i.build(args[1], DungeonsPlayerManager.i.get(p));
						break;
					case 3:
						item = ItemBuilder.i.build(args[1], DungeonsPlayerManager.i.get(p));
						item.setAmount(Integer.parseInt(args[2]));
						break;
					case 4:
						item = ItemBuilder.i.build(args[1], DungeonsPlayerManager.i.get(p),args[3]);
						item.setAmount(Integer.parseInt(args[2]));
						break;
					default:
						return false;
					}

					if (item != null) p.getInventory().addItem(item);
				}
				break;
			case "sharpen":
				ItemStack item = ((Player)sender).getInventory().getItemInMainHand();
				ItemMeta meta = item.getItemMeta();
				meta.getPersistentDataContainer().set(ItemBuilder.kSharps, PersistentDataType.STRING, args[1]);
				item.setItemMeta(meta);
				break;
			case "activate":
				if (args.length < 2) return false;
				Dungeon dungeon = DungeonManager.i.dungeons.get(Integer.parseInt(args[1]));
				
				for (Entry<SpawnPosition, String> spawn : dungeon.spawns.entrySet())
				{
					if (spawn.getKey().mob == null)
					{
						DungeonMobCreator.i.create(spawn.getValue().toLowerCase(), spawn.getKey());
					}
				}
				break;
			case "spawn":
				if (args.length < 2) return false;
				if (sender instanceof Player)
				{
					int amount = 1;
					if (args.length == 3) amount = Integer.parseInt(args[2]);
					while (amount > 0)
					{
						//MobCreator.i.summon(((Player) sender).getLocation(),args[1].toLowerCase());
						DungeonMobCreator.i.create(args[1].toLowerCase(), new SpawnPosition(((Player) sender).getLocation()));
						amount--;
					}
				}
				break;
			default:
				return false;
		}
		
		return true;
    }
}
