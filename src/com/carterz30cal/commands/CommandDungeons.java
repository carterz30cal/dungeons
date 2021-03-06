package com.carterz30cal.commands;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.bosses.BossManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.gui.GUI;
import com.carterz30cal.gui.MenuType;
import com.carterz30cal.gui.ShopGUI;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ShopManager;
import com.carterz30cal.items.magic.ItemWand;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.ListenerBlockEvents;
import com.carterz30cal.player.ListenerEntityDamage;

public class CommandDungeons implements CommandExecutor
{
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 0) return false;
		if (sender instanceof Player && !sender.isOp()) return true;
		switch (args[0].toLowerCase()) {
			case "version":
				sender.sendMessage("currently running v1.0.0");
				break;
			case "boss":
				if (args.length == 1) return false;
				BossManager.summon(args[1]);
				break;
			case "updates":
				if (sender.isOp()) ListenerBlockEvents.allowBlockPhysics = !ListenerBlockEvents.allowBlockPhysics;
				break;
			case "resetquests":
				if (args.length == 1) return false;
				DungeonsPlayer dp = DungeonsPlayerManager.i.get(Bukkit.getPlayer(args[1]));
				for (String quest : dp.questProgress.keySet()) dp.questProgress.put(quest, "start");
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
			case "pvp":
				ListenerEntityDamage.pvp = !ListenerEntityDamage.pvp;
				sender.sendMessage("pvp is now: " + ListenerEntityDamage.pvp);
				break;
			case "shop":
				new ShopGUI(ShopManager.shops.get(args[1].toLowerCase()),(Player)sender);
				break;
			case "clearbackpack":
				if (args.length == 1) return false;
				Player pl = Bukkit.getPlayer(args[1]);
				DungeonsPlayer dpl = DungeonsPlayerManager.i.get(pl);
				dpl.backpackb = new ArrayList<BackpackItem[]>();
				dpl.backpackb.add(new BackpackItem[45]);
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
			case "level":
				if (args.length == 1) return false;
				DungeonsPlayer d = DungeonsPlayerManager.i.get((Player)sender);
				if (args.length == 3) d = DungeonsPlayerManager.i.get(Bukkit.getPlayer(args[2]));
				
				int request = Integer.parseInt(args[1]);
				d.level.experience = 0;
				d.level.pointAllocation.clear();
				d.level.points = 0;
				if (request > 0) d.level.give(CharacterSkill.requirement(request)+1);
				break;
			case "xp":
				if (args.length < 2) return false;
				DungeonsPlayer dn = DungeonsPlayerManager.i.get((Player)sender);
				
				int requests = Integer.parseInt(args[1]);
				dn.level.experience = 0;
				dn.level.pointAllocation.clear();
				dn.level.points = 0;
				if (requests > 0) dn.level.give(requests);
				break;
			case "wand":
				DungeonsPlayer du = DungeonsPlayerManager.i.get((Player)sender);
				ItemStack wand = ItemBuilder.i.build(args[1], du);
				ItemMeta wme = wand.getItemMeta();
				wme.getPersistentDataContainer().set(ItemWand.kSpell, PersistentDataType.STRING, args[2]);
				if (args.length == 4) wme.getPersistentDataContainer().set(ItemWand.kModifier, PersistentDataType.STRING, args[3]);
				ItemBuilder.i.updateMeta(wme, du);
				wand.setItemMeta(wme);
				du.player.getInventory().addItem(wand);
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
			case "spawn":
				if (args.length < 2) return false;
				if (sender instanceof Player)
				{
					int amount = 1;
					if (args.length == 3) amount = Integer.parseInt(args[2]);
					while (amount > 0)
					{
						new BukkitRunnable()
						{

							@Override
							public void run()
							{
								new DMob(DMobManager.types.get(args[1]),null,((Player) sender).getLocation().clone(),true);
							}
							
						}.runTaskLater(Dungeons.instance, amount-1);
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
