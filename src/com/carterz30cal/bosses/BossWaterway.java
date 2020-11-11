package com.carterz30cal.bosses;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.mobs.DungeonMobType;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.tasks.TaskSendMsg;
import com.carterz30cal.utility.BoundingBox;
import com.carterz30cal.utility.InventoryHandler;

import net.md_5.bungee.api.ChatColor;

public class BossWaterway extends AbsBoss
{
	public static DungeonMobType ty;
	private BoundingBox area = new BoundingBox(new Location(Dungeons.w,-94, 112, 20985),new Location(Dungeons.w, -112, 96, 21011));
	
	public ArrayList<DungeonMob> alle;
	public ArrayList<DungeonMob> s1_drenched;
	public ArrayList<DungeonMob> s2_guardians;
	public ArrayList<Block> changed;
	private int tick;
	
	private boolean hasWarned;
	private boolean guardians;
	
	public static String[] lightningPhrases = {"Eat lightning, X!","Do you like lightning X?","Fear my power, X!"};
	
	private BossBar bar;
	public BossWaterway()
	{
		if (ty != null) return;
		ty = new DungeonMobType();
		ty.name = ChatColor.GOLD + "Dried Drench";
		ty.xp = 600;
		ty.baby = false;
		ty.type = EntityType.HUSK;
		ty.maxHealth = 7500;
		ty.damage = 120;
		ty.knockbackResist = 1;
		ty.boss = true;
		ty.main = new ItemStack(Material.NETHERITE_AXE);
		ty.wearing = new ItemStack[4];
		
		bar = Bukkit.createBossBar(ChatColor.GOLD + "Dried Drench", BarColor.RED, BarStyle.SEGMENTED_20, new BarFlag[0]);
		
	}
	
	
	
	
	@Override
	public int end()
	{
		return 2;
	}

	@Override
	public void entry()
	{
		abh = new AliveBossHandler();
		abh.boss = this;
		abh.rep = DungeonMobCreator.i.create(ty, new SpawnPosition(new Location(Dungeons.w,-102.5, 100.5, 21006.5,-180, 17)),true);
		abh.rep.entity.setInvulnerable(true);
		abh.rep.entity.setGravity(false);
		((LivingEntity)abh.rep.entity).setAI(false);
		players = area.getWithin();
		s1_drenched = new ArrayList<DungeonMob>();
		s2_guardians = new ArrayList<DungeonMob>();
		alle = new ArrayList<DungeonMob>();
		alle.add(abh.rep);
		changed = new ArrayList<Block>();
		new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "Welcome, puny adventurers."   ,2);
		new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "I hope you're ready for this!",4);
		new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "Here, fight these."           ,6);
		
		health = -1;
		stage = 1;
		if (bar == null) bar = Bukkit.createBossBar(ChatColor.GOLD + "Dried Drench", BarColor.RED, BarStyle.SEGMENTED_20, new BarFlag[0]);
		bar.setProgress(0);
		for (int i = 0; i < players.size();i++)
		{
			players.get(i).player.teleport(new Location(Dungeons.w,-104 + (i % 5), 97, 20996 + (i / 5),0,-10));
			bar.addPlayer(players.get(i).player);
			players.get(i).heal(players.get(i).stats.health);
		}
		
		//-101, 104, 20988
		//-105, 107, 20988
		for (int x = -105; x <= -101; x++)
		{
			for (int y = 104; y <= 107; y++) 
			{
				Block b = Dungeons.w.getBlockAt(x, y, 20988);
				if (b.getType() == Material.AIR) 
				{
					b.setType(Material.IRON_BARS,true);
					b.getState().update(true, true);
					changed.add(b);
				}
			}
		}
	}

	@Override
	public void transition()
	{
		stage = 2;
		tick = 0;
		new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "Huh. You're stronger than i first thought.",0);
		new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "Nevermind, i'll just kill you myself."     ,2);
		new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "Try these!"                                ,4);
		abh.rep.entity.setInvulnerable(false);
		abh.rep.entity.setGravity(true);
		bar.setStyle(BarStyle.SOLID);
		
	}

	@Override
	public void exit()
	{
		abh.cancel();
		bar.removeAll();
		
		if (health > 0)
		{
			for (Player d : DungeonManager.i.warps.get("waterway").players)
			{
				d.sendMessage(ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "You have all failed to defeat me.");
				
			}
		}
		else
		{
			new TaskSendMsg(players, ChatColor.GOLD + "You have been rewarded with a Dried Drench Lootbox",0);
			for (DungeonsPlayer d : players) InventoryHandler.addItem(d, ItemBuilder.i.build("loot_waterway", null));
		}
		
		for (DungeonMob m : alle) if (m.entity != null) m.destroy(null);
		BossManager.bosses.remove("summonkey_waterway");
		
		for (Block b : changed)
		{
			b.setType(Material.AIR);
			b.getState().update(true, true);
		}
	}

	@Override
	public void tick() 
	{
		if (stage == 1)
		{
			for (int i = 0; i < s1_drenched.size(); i++) 
			{
				if (s1_drenched.get(i).health < 1) 
				{
					bar.setProgress(Math.max(0, bar.getProgress() - 0.05));
					s1_drenched.remove(i);
				}
			}
			if (tick == (7*20) + 5)
			{
				alle.add(DungeonMobCreator.i.create("sniperskeleton", new SpawnPosition(-96.3, 103, 21010.5),true));
				alle.add(DungeonMobCreator.i.create("sniperskeleton", new SpawnPosition(-108.5, 103, 21010.5),true));
			}
			if (tick > 6*20 && tick <= (6*20) + 10*20 & tick % 10 == 0) 
			{
				DungeonMob m = DungeonMobCreator.i.create("drenched5", new SpawnPosition(-102.5, 111, 21002.5),true);
				bar.setProgress(Math.min(1, bar.getProgress() + 0.05));
				s1_drenched.add(m);
				alle.add(m);
			}
			if (tick > 10*20 && s1_drenched.size() == 0) transition();
			if (tick > 20*30 && tick % 120 == 119)
			{
				int pid = (int) Math.floor(Math.random()*players.size());
				Dungeons.w.strikeLightning(players.get(pid).player.getLocation());
				new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + 
						lightningPhrases[(int)Math.floor(Math.random()*lightningPhrases.length)].replace("X", players.get(pid).player.getDisplayName()),0);
				players.get(pid).damage(110,false);
			}
		}
		else
		{
			if (guardians && s2_guardians != null) for (int k = 0; k < s2_guardians.size();k++) if (s2_guardians.get(k).health < 1) s2_guardians.remove(k);
			double projess = ((double)abh.rep.health) / ((double)abh.rep.health());
			bar.setProgress(Math.min(1, Math.max(0,projess)));
			if (tick == 20*2) ((LivingEntity)abh.rep.entity).setAI(true);
			else if (tick == 20*4) for (DungeonsPlayer d : players) new BossProjectile(this,abh.rep.entity.getLocation(),d.player,12,Particle.SOUL);
			else if (tick > 20*10 && tick % 240 == 239) for (DungeonsPlayer d : players) new BossProjectile(this,abh.rep.entity.getLocation(),d.player,8,Particle.SOUL);
			
			if (!guardians && tick >= 20*8 && abh.rep.health < 4000)
			{
				guardians = true;
				new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "I'd watch out, my guardians hit hard.",0);
				((LivingEntity)abh.rep.entity).setAI(false);
				for (int i = 0; i < 5; i++)
				{
					DungeonMob m = DungeonMobCreator.i.create("driedguardian", new SpawnPosition(-102.5, 111, 21002.5),true);
					s2_guardians.add(m);
					alle.add(m);
				}
			}
			else if (s2_guardians != null && guardians && s2_guardians.size() == 0)
			{
				new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "Back to fighting i guess.",0);
				((LivingEntity)abh.rep.entity).setAI(true);
				s2_guardians = null;
			}
		}
		
		if (abh.rep.health < 1 || players.size() == 0) exit();
		for (int i = 0; i < players.size();i++) 
		{
			DungeonsPlayer d = players.get(i);
			if (!area.isInside(d.player.getLocation())) 
			{
				bar.removePlayer(players.get(i).player);
				players.remove(i);
			}
			if (stage == 2) 
			{
				if (d.player.getLocation().getBlockZ() < 20991) 
				{
					if (!hasWarned) 
					{
						new TaskSendMsg(players, ChatColor.RED + "[DRENCH] " + ChatColor.GRAY + "Get back here " + d.player.getDisplayName() + "!",1);
						hasWarned = true;
					}
					if (tick % 5 == 0)
					{
						d.player.damage(4,abh.rep.entity);
						d.damage(4, true);
					}
					
				}
			}
		}
		
		tick++;
	}




	@Override
	public void projectileHit(BossProjectile p, Player t) {
		t.damage(5, abh.rep.entity);
		t.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,140,0,true));
	}
	
	
}
