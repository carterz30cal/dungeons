package com.carterz30cal.commands;


import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.areas.InfestedTimeShop;
import com.carterz30cal.bosses.BossManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.ListenerPlayerJoin;
import com.carterz30cal.gui.GUI;
import com.carterz30cal.gui.MenuType;
import com.carterz30cal.gui.MonsterHunterGUI;
import com.carterz30cal.gui.ShopGUI;
import com.carterz30cal.gui.TradeGUI;
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
import com.carterz30cal.player.PlayerDelivery;
import com.carterz30cal.player.PlayerRank;
import com.carterz30cal.utility.Square;
import com.carterz30cal.utility.StringManipulator;

public class CommandDungeons implements CommandExecutor
{
	@SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 0) return false;
		if (sender instanceof Player && !sender.isOp()) return true;
		switch (args[0].toLowerCase()) {
			case "version":
				sender.sendMessage("currently running " + Dungeons.instance.getDescription().getVersion());
				break;
			case "tutorialreset":
				DungeonsPlayer rest = DungeonsPlayerManager.i.get((Player)sender);
				rest.tutorials.clear();
				break;
			case "gui":
				if (args.length == 1) return true;
				if (args[1].equals("monsterhunter")) new MonsterHunterGUI((Player)sender);
				else if (args[1].equals("trade")) new TradeGUI((Player)sender);
				break;
			case "fastforward":
				InfestedTimeShop.tick = 35900;
				break;
			case "removevote":
				if (args.length == 1) return true;
				Player targ = Bukkit.getPlayerExact(args[1]);
				DungeonsPlayerManager.i.get(targ).voteBoost = null;
				break;
			case "boss":
				if (args.length == 1) return false;
				BossManager.summon(args[1]);
				break;
			case "setlevel":
				if (args.length == 1) return false;
				DungeonsPlayer dm = DungeonsPlayerManager.i.get((Player)sender);
				if (args.length == 3) dm = DungeonsPlayerManager.i.get(Bukkit.getPlayer(args[1]));
				int i = args.length == 3 ? Integer.parseInt(args[2]) : Integer.parseInt(args[1]);
				
				dm.level.level = i-1;
				dm.level.giveFlat(CharacterSkill.tonextlevel(dm.level.level) + 1);
				break;
			case "levelrequirement":
				if (args.length == 1) return false;
				int req = Integer.parseInt(args[1]);
				sender.sendMessage(CharacterSkill.prettyText(req) + " requires " + StringManipulator.truncateLess(CharacterSkill.xpforlevel(req)) + " xp.");
				break;
			case "archive":
				DungeonsPlayerManager.i.archive((Player)sender);
				break;
			case "restrict":
				Square sq = new Square(Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]),0);
				DungeonsPlayer restricted = DungeonsPlayerManager.i.get(Bukkit.getPlayer(args[1]));
				restricted.restriction = sq;
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
			case "xp":
				if (args.length < 2) return false;
				DungeonsPlayer dn = DungeonsPlayerManager.i.get((Player)sender);
				if (args.length == 3) dn = DungeonsPlayerManager.i.get(Bukkit.getPlayer(args[2]));
				
				int requests = Integer.parseInt(args[1]);
				dn.level.experience = 0;
				dn.level.pointAllocation.clear();
				dn.level.points = 0;
				dn.skills.clear();
				if (requests > 0) dn.level.giveFlat(requests);
				break;
			case "deliver":
				PlayerDelivery delivery = new PlayerDelivery();
				UUID recipient = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
				delivery.recipient = recipient;
				delivery.xp = Long.parseLong(args[2]);
				delivery.coins = Integer.parseInt(args[3]);
				delivery.items = args[4];
				
				if (ListenerPlayerJoin.deliveries.containsKey(recipient))
				{
					ListenerPlayerJoin.deliveries.get(recipient).combine(delivery);
				}
				else ListenerPlayerJoin.deliveries.put(recipient, delivery);
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
			case "rank":
				Player target = (Player)sender;
				if (args.length == 3) target = Bukkit.getPlayer(args[1]);
				PlayerRank rank = PlayerRank.valueOf(args[args.length-1]);
				DungeonsPlayerManager.i.get(target).rank = rank;
				DungeonsPlayerManager.i.get(target).updateRank();
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
					if (args[1].equals("list"))
					{
						String sendn = "";
						for (String s : DMobManager.types.keySet())
						{
							sendn = sendn + " " + s;
							if (sendn.length() > 40) sender.sendMessage(sendn);
						}
						break;
					}
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
