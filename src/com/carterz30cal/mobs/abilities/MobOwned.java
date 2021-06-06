package com.carterz30cal.mobs.abilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Mob;

import com.carterz30cal.areas.InfestedHunter;
import com.carterz30cal.gui.MonsterHunterGUI;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class MobOwned extends DMobAbility
{
	public static Map<DMob,DungeonsPlayer> owner = new HashMap<>();

	public int cost;
	public int bounty;
	public int xp;
	public MobOwned(FileConfiguration data, String path) {
		super(data, path);
		
		cost = data.getInt(path + ".cost", 0);
		bounty = data.getInt(path + ".bounty",0);
		xp = data.getInt(path + ".xp",0);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void tick(DMob mob)
	{
		DungeonsPlayer own = owner.get(mob);
		if (own == null) return;
		Mob m = ((Mob) mob.entities.get(0));
		if (m.getTarget() == own.player || own.player.getGameMode() != GameMode.SURVIVAL) return;
		else if (m.getLocation().distance(own.player.getLocation()) < 20) m.setTarget(own.player);
		else m.setTarget(null);
	}
	public void killed(DMob mob)
	{
		DungeonsPlayer own = owner.get(mob);
		InfestedHunter.active.remove(own);
		owner.remove(mob);
		
		if (mob.health != -1)
		{
			own.player.sendMessage(ChatColor.GREEN + "You beat the tarantula! +" + bounty + " coins!");
			own.coins += bounty;
			
			int currentlvl = MonsterHunterGUI.level(own.level.hunter);
			own.level.hunter += xp;
			if (currentlvl < MonsterHunterGUI.level(own.level.hunter))
			{
				own.player.sendMessage(ChatColor.GOLD +""+ ChatColor.BOLD + "Hunter Level " + (currentlvl+1) + "!");
				own.player.sendMessage(ChatColor.GOLD + "+1 skill point!");
			}
		}
	}
	public int damaged(DMob mob,DungeonsPlayer player,int damage)
	{
		if (owner.containsKey(mob) && owner.get(mob) != player) 
		{
			return 0;
		}
		return damage;
	}
	

}
