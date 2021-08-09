package com.carterz30cal.areas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.tasks.TaskSendMsg;
import com.carterz30cal.utility.BoundingBox;
import com.carterz30cal.utility.ParticleFunctions;
import com.carterz30cal.utility.RandomFunctions;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class WaterwayBoss extends AbsDungeonEvent 
{
	public boolean alive;
	
	private DMob boss;
	private int phase;
	// phase 1 - stationary, spawns 10 low damage drenched. (8k health). also those sniper bois.
	// phase 2 - moves, strikes the strongest player in the arena (calculated using health*armour) with lightning.
	//           spawns 2 iron golems. (8k health -> 3k health)
	// phase 3 - spawns another 10 drenched minions, gives everyone slowness I. (3k health till dead)
	private List<DMob> phase1 = new ArrayList<>();
	private double[][] phase1pos = {{-107.5,97,20998,5,-90,0},{-107.5,97,20999,5,-90,0},{-107.5,97,21000,5,-90,0},{-107.5,97,21001,5,-90,0},{-107.5,97,21002,5,-90,0},
			{-97.5,97,20998,90,0},{-97.5,97,20999,90,0},{-97.5,97,21000,90,0},{-97.5,97,21001,90,0},{-97.5,97,21002,90,0}};
	private double[][] phase2 = {{-101,97,21004,180,0},{-104,97,21004,180,0}};
	private BoundingBox area = new BoundingBox(new Location(Dungeons.w,-94, 112, 20985),new Location(Dungeons.w, -112, 96, 21011));
	
	private String[] phase1deaths = {"Never liked that one.","Was gonna fire them anyways.","Hey! I liked that one!","Fine. They were annoying."
			,"Ok.","Cool.","You're stronger than i thought!","They were weak.","Aw, you're no fun.","What did they ever do to you?","Rude.","I see."
			,"Clean them up, will you?","Eh.","Whatever.",":("};
	private BossBar bar;
	private int tick;
	private boolean onLastStand; // when first goes below 300 health
	
	private DungeonsPlayer laser;
	
	
	@Override
	public void tick()
	{
		if (!alive) return;
		
		ArrayList<DungeonsPlayer> players = area.getWithin();
		if (boss.health < 1)
		{
			if (phase == 3)
			{
				new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "Ah well.",1);
				new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "You'll never beat Midas.",2);
			}
			reset();
			return;
		}
		if (phase == 1)
		{
			List<DMob> remove = new ArrayList<>();
			for (DMob m : phase1)
			{
				if (m.health < 1)
				{
					remove.add(m);
					if (RandomFunctions.random(1, 10) < 5) new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + RandomFunctions.get(phase1deaths),1);
					// reduce chat spam somewhat
				}
			}
			
			phase1.removeAll(remove);
			bar.setProgress(phase1.size() / (double)10);
			if (phase1.size() == 0) phase2();
		}
		else if (phase == 2)
		{
			tick++;
			bar.setProgress(boss.health / (double)boss.health());
			if (boss.health <= 3500) 
			{
				phase3();
				return;
			}
			
			if (laser != null && !players.contains(laser)) laser = null;
			else if (laser != null && tick % 15 == 0)
			{
				laser.damage(5, false);
				Location la = laser.player.getEyeLocation().clone().add(0,0.1,0);

				CraftPlayer c = (CraftPlayer)laser.player;
				PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (c.getHandle(),(byte)2);
				for (Player k : Bukkit.getOnlinePlayers())
				{
					c = (CraftPlayer)k;
					c.getHandle().playerConnection.sendPacket(packet);
				}

				Location l = new Location(Dungeons.w,-102.5,110,20999.5);
				double dx = (la.getX() - l.getX()) / 80;
				double dy = (la.getY() - l.getY()) / 80;
				double dz = (la.getZ() - l.getZ()) / 80;
				for (int x = 0; x < 80; x++) 
				{
					l.add(dx, dy, dz);
					Dungeons.w.spawnParticle(Particle.REDSTONE, l, 1, 0d, 0d, 0d, new DustOptions(Color.RED,0.7f));
				}
			}
			else if (laser == null && tick % 225 == 0 && players.size() > 0)
			{
				laser = (DungeonsPlayer) RandomFunctions.get(players.toArray());
				
				new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + laser.player.getDisplayName() + " will die to my laser!",1);
			}
			if (tick % 300 == 0 && players.size() > 0)
			{
				players.sort((a,b) -> a.getHealth() > b.getHealth() ? -1 : 1);
				DungeonsPlayer strongest = players.get(0);
				
				if (strongest.getHealth() < 120)
				{
					new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "You're all so weak!",1);
				}
				else
				{
					new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + strongest.player.getDisplayName() + " is too strong! Let's zap them!",1);
					strongest.lightning(105);
				}
			}
			else if (tick == 450)
			{
				new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "Say hi to my snipers!",1);
				phase1.add(DMobManager.spawn("waterwayboss_sniper", new SpawnPosition(-96.3, 103, 21010.5),false));
				phase1.add(DMobManager.spawn("waterwayboss_sniper", new SpawnPosition(-108.5, 103, 21010.5),false));
			}
		}
		else if (phase == 3)
		{
			tick++;
			bar.setProgress(boss.health / 3500d);
			
			List<DMob> remove = new ArrayList<>();
			for (DMob m : phase1) if (m.health < 1) remove.add(m);
			phase1.removeAll(remove);
			
			if (boss.health < 1200 && !onLastStand)
			{
				onLastStand = true;
				
				for (DungeonsPlayer d : players)
				{
					d.player.sendMessage(ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "I will NOT be defeated by YOU!");
					d.player.sendMessage(ChatColor.RED + "Dried Drench " + ChatColor.GRAY + "used "+ChatColor.DARK_RED + "Wrath " +ChatColor.GRAY + "on you and reduced your health by 55%!");
					d.player.playSound(boss.entities.get(0).getLocation(), Sound.ENTITY_WITHER_DEATH, 0.4f, 1);
					
					d.lightning(0);
					d.setHealth(d.getHealthPercent()*0.45);
				}
				phase1.add(DMobManager.spawn("waterwayboss_sniper", new SpawnPosition(-96.3, 103, 21010.5),false));
				phase1.add(DMobManager.spawn("waterwayboss_sniper", new SpawnPosition(-108.5, 103, 21010.5),false));
				for (double[] pos : phase2)
				{
					DMob mob = DMobManager.spawn("waterwayboss_golem2", new SpawnPosition(pos[0],pos[1],pos[2],pos[3],pos[4]));
					phase1.add(mob);
				}
			}
			
			if (tick % 350 == 0)
			{
				new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "Lightning storm!",1);
				for (DungeonsPlayer d : players)
				{
					for (int i = 0; i < 5;i++)
					{
						new BukkitRunnable()
						{

							@Override
							public void run() {
								d.lightning(18);
							}
							
						}.runTaskLater(Dungeons.instance, i*10);
					}
				}
			}
			else if (tick % 200 == 0 && phase1.size() <= 16)
			{
				new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "More servants!",1);
				phase1.add(DMobManager.spawn("waterwayboss_drenched2", new SpawnPosition(-98, 98, 20991)));
				phase1.add(DMobManager.spawn("waterwayboss_drenched2", new SpawnPosition(-98, 98, 20990)));
				phase1.add(DMobManager.spawn("waterwayboss_drenched2", new SpawnPosition(-107, 98, 20991)));
				phase1.add(DMobManager.spawn("waterwayboss_drenched2", new SpawnPosition(-107, 98, 20990)));
			}
			else if (tick % 300 == 0 && boss.health < 2500)
			{
				new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "OMEGA HEAL!",1);
				new BukkitRunnable()
				{
					private int times;
					@Override
					public void run() {
						times++;
						if (boss == null || times == 12 || boss.health < 1) cancel();
						
						for (int p = 0; p < 7;p++) ParticleFunctions.stationary(RandomFunctions.offset(boss.entities.get(0).getLocation().add(0,1,0), 0.8), Particle.VILLAGER_HAPPY, 1);
						boss.heal(44);
					}
					
				}.runTaskTimer(Dungeons.instance, 25, 15);
			}
		}
	}
	private void phase2()
	{
		ArrayList<DungeonsPlayer> players = area.getWithin();
		new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "So you beat my servants...",1);
		new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "That's unfortunate, but they are replaceable.",2);
		new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "However you will still pay...",3);
		
		tick = 0;
		phase = -1;
		new BukkitRunnable()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (double[] pos : phase2)
				{
					DMob mob = DMobManager.spawn("waterwayboss_golem", new SpawnPosition(pos[0],pos[1],pos[2],pos[3],pos[4]));
					phase1.add(mob);
				}
				phase = 2;
				bar.setStyle(BarStyle.SOLID);
				bar.setProgress(1);
				boss.setMoving();
				boss.setVulnerable();
				
			}
			
		}.runTaskLater(Dungeons.instance, 70);
		
	}
	private void phase3()
	{
		laser = null;
		ArrayList<DungeonsPlayer> players = area.getWithin();
		new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "Maybe i underestimated you.",1);
		new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "Nevermind, i still have a few tricks up my sleeve.",2);
		if (phase1 != null && !phase1.isEmpty()) for (DMob m : phase1) if (m != null) m.remove();
		phase = 3;
		tick = 0;
		
		for (DungeonsPlayer d : players) 
		{
			d.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,5205,0,true));
			d.player.teleport(new Location(Dungeons.w,-102.5,97,20997.5,0,-9),TeleportCause.PLUGIN);
		}
		
		boss.setStatic();
		boss.health = 3500;
		boss.entities.get(0).teleport(new Location(Dungeons.w,-102.5, 100.5,21006.5,-180,0));
	}
	
	private void reset()
	{
		ArrayList<DungeonsPlayer> players = area.getWithin();
		for (DungeonsPlayer d : players) d.player.removePotionEffect(PotionEffectType.SLOW);
		
		if (bar != null) bar.removeAll();
		bar = null;
		
		if (phase1 != null && !phase1.isEmpty()) for (DMob m : phase1) if (m != null) m.remove();
		phase1.clear();
		boss = null;
		
		alive = false;
		onLastStand = false;
		laser = null;
	}
	private void spawn()
	{
		boss = DMobManager.spawn("waterwayboss_boss", new SpawnPosition(-102.5, 100.5,21006.5,-180,0));
		boss.setStatic();
		boss.setInvulnerable();
		
		ArrayList<DungeonsPlayer> players = area.getWithin();
		bar = Bukkit.createBossBar(ChatColor.GOLD + "Dried Drench", BarColor.RED, BarStyle.SEGMENTED_10, new BarFlag[0]);
		for (DungeonsPlayer d : players) bar.addPlayer(d.player);
		for (double[] pos : phase1pos)
		{
			DMob mob = DMobManager.spawn("waterwayboss_drenched1", new SpawnPosition(pos[0],pos[1],pos[2],pos[3],pos[4]));
			phase1.add(mob);
		}
		
		
		
		new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "You shall not enter necropolis!",1);
		new TaskSendMsg(players,ChatColor.RED + "Dried Drench: " + ChatColor.WHITE + "You are weak, and i shall crush you.",4);
		
		phase = 1;
		alive = true;
	}
	@Override
	public void end()
	{
		reset();
	}
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (!e.hasItem() || e.getItem() == null) return false;

		String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"");
		
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		
		if (it.equals("summonkey_waterway"))
		{
			if (!area.getWithin().contains(d)) 
			{
				e.getPlayer().sendMessage(ChatColor.RED + "Not in boss room!");
				return true;
			}
			else if (alive) e.getPlayer().sendMessage(ChatColor.RED + "Boss is alive!");
			else
			{
				spawn();
				e.getItem().setAmount(e.getItem().getAmount()-1);
			}
			
		}
		return false;
	}
}
