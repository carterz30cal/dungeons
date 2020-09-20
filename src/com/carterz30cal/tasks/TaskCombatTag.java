package com.carterz30cal.tasks;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.mobs.MobAction;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskCombatTag extends BukkitRunnable
{
	public DungeonsPlayer p;
	public DungeonMob m;
	
	public TaskCombatTag(Player player,DungeonMob mob)
	{
		p = DungeonsPlayerManager.i.get(player);
		m = mob;
	}
	// an action can occur once every ten seconds
	@Override
	public void run() {
		if (m == null || p == null || m.health <= 0) 
		{
			cancel();
			return;
		}
		// TODO Auto-generated method stub
		for (MobAction action : m.type.actions)
		{
			if (action == null) continue;
			
			boolean doing = false;
			switch (action.condAttr)
			{
			case "always":
				doing = true;
				break;
			case "distance":
				int dist = (int)Double.parseDouble(action.condAm.toString());
				if ((action.condTog && p.player.getLocation().distance(m.entity.getLocation()) >= dist) 
						|| (!action.condTog && p.player.getLocation().distance(m.entity.getLocation()) < dist)) doing = true;
				break;
			case "random":
				double d = (Double)(1/Double.parseDouble(action.condAm.toString())/100);
				int num = (int) Math.round(Math.random()*d);
				if (num == 0) doing = true;
				break;
			case "health":
				double h = (Double)(Double.parseDouble(action.condAm.toString())/100);
				// >= is true, < is false
				if ((action.condTog && m.health >= (m.type.maxHealth*h)) 
						|| (!action.condTog && m.health < (m.type.maxHealth*h))) doing = true;
				break;
			default:
				doing = false;
			}
			
			if (doing) 
			{
				if (action.p != null)
				{
					Particles.shape("cross", action.p, m.armourstand.getLocation().add(0,1.05,0));
					Particles.shape("cross", action.p, m.armourstand.getLocation().add(0,1.0,0));
					Particles.shape("cross", action.p, m.armourstand.getLocation().add(0,0.95,0));
				}
				((LivingEntity)m.entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 100));
				for (String effect : action.effects.keySet())
				{
					switch (effect)
					{
					case "health":
						int health = Integer.parseInt(action.effects.get(effect).toString());
						m.heal(health);
						break;
					case "weapon":
						ItemStack item = ItemBuilder.i.build(action.effects.get(effect).toString(),null);
						System.out.println("dis happen");
						((LivingEntity)m.entity).getEquipment().setItemInMainHand(item);
						break;
					case "summon":
						int amount = 1;
						String[] mo = action.effects.get(effect).toString().split(",");
						if (mo.length == 2) amount = Integer.parseInt(mo[1]);
						if (DungeonMobCreator.summons >= 30) doing = false;
						while (amount > 0 && doing)
						{
							if (!DungeonMobCreator.i.create(mo[0], new SpawnPosition(m.entity.getLocation().add(Math.random()*2, 0, Math.random()*2)),true)) break;
							amount--;
						}
						break;
					}
				}
				if (doing) p.player.sendMessage(format(action));
				break;
			}
			else continue;
		}

	}

	public String format(MobAction action)
	{
		String text = action.text;
		text = text.replaceAll("ENTITY", m.type.name);
		for (ChatColor c : ChatColor.values())
		{
			text = text.replaceAll("=" + c.name(), c.toString());
		}
		for (String ef : action.effects.keySet())
		{
			text = text.replaceAll(ef.toUpperCase(), action.effects.get(ef).toString());
		}
		return text;
	}
}
