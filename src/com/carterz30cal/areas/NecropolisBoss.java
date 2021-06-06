package com.carterz30cal.areas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.quests.TutorialManager;
import com.carterz30cal.quests.TutorialTrigger;
import com.carterz30cal.utility.BoundingBox;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.RandomFunctions;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class NecropolisBoss extends AbsDungeonEvent 
{
	public boolean alive;
	
	private DMob boss;
	private int phase;

	private List<DMob> phase1 = new ArrayList<>();
	private List<DMob> defenders = new ArrayList<>();

	private BoundingBox area = new BoundingBox(new Location(Dungeons.w,7,118,22028),new Location(Dungeons.w, 26,93,22067));
	
	private BossBar bar;
	private int tick;
	
	private String[] mobs = {"necropolis_minion1","necropolis_minion2","necropolis_minion3","necropolis_minion4"};
	private Location[] locations = {new Location(Dungeons.w,23.5,99,22045.5),new Location(Dungeons.w,10.5,99,22045.5)};
	
	private Map<Block,Material> replaced = new HashMap<>();
	
	private String[] taunts = {"Give up already!","I'm too strong!","Why don't you just die?","Nobody can beat me!","I'm bored. Die. Please."};
	
	@Override
	public void tick()
	{
		if (!alive) return;
		
		ArrayList<DungeonsPlayer> players = area.getWithin();
		if (boss.health < 1 || players.size() == 0)
		{
			if (phase == 4 && players.size() > 0)
			{
				send(players,"...",10);
				send(players,"How did you beat me?",50);
				
				phase = 5;
				for (DungeonsPlayer d : players) 
				{
					InventoryHandler.addItem(d, ItemBuilder.i.build("loot_necropolis",null), false);
					TutorialManager.fireEvent(d, TutorialTrigger.KILL_ENEMY, "necropolis_boss");
				}
			}
			reset();
			return;
		}
		
		List<DMob> remove = new ArrayList<>();
		for (DMob m : phase1) if (m.health < 1) remove.add(m);
		phase1.removeAll(remove);
		
		tick++;
		if (phase < 4)
		{
			if (tick % 20 == 0 && tick <= 400)
			{
				phase1.add(DMobManager.spawn(mobs[phase-1], new SpawnPosition(RandomFunctions.get(locations))));
			}
			bar.setProgress(phase1.size() / 20d);
			
			if (tick > 420 && phase1.size() == 0) advance();
		}
		else 
		{
			if (tick % 40 == 0 && phase1.size() < 18) phase1.add(DMobManager.spawn(mobs[phase-1], new SpawnPosition(RandomFunctions.get(locations))));
			bar.setProgress(boss.health / 100000d);
			
			if ((tick+50) % 300 == 0) send(players,RandomFunctions.get(taunts),0);
		}
		if (phase > 1 && players.size() > 0 && tick % 40 == 0)
		{
			players.sort((a,b) -> a.stats.damage > b.stats.damage ? -1 : 1);
			
			DungeonsPlayer chosen = players.get(0);
			chosen.damage(5*phase, true);
			Location la = chosen.player.getEyeLocation().clone().add(0,0.1,0);

			CraftPlayer c = (CraftPlayer)chosen.player;
			PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (c.getHandle(),(byte)2);
			for (Player k : Bukkit.getOnlinePlayers())
			{
				c = (CraftPlayer)k;
				c.getHandle().playerConnection.sendPacket(packet);
			}

			Location l = new Location(Dungeons.w,20.5,114,22045.5);
			double dx = (la.getX() - l.getX()) / 80;
			double dy = (la.getY() - l.getY()) / 80;
			double dz = (la.getZ() - l.getZ()) / 80;
			for (int x = 0; x < 80; x++) 
			{
				l.add(dx, dy, dz);
				Dungeons.w.spawnParticle(Particle.REDSTONE, l, 1, 0d, 0d, 0d, new DustOptions(Color.RED,0.7f));
			}
			l = new Location(Dungeons.w,13.5,114,22045.5);
			dx = (la.getX() - l.getX()) / 80;
			dy = (la.getY() - l.getY()) / 80;
			dz = (la.getZ() - l.getZ()) / 80;
			for (int x = 0; x < 80; x++) 
			{
				l.add(dx, dy, dz);
				Dungeons.w.spawnParticle(Particle.REDSTONE, l, 1, 0d, 0d, 0d, new DustOptions(Color.RED,0.7f));
			}
			
		}
	}
	private void advance()
	{
		ArrayList<DungeonsPlayer> players = area.getWithin();
		phase++;
		tick = 0;
		System.out.println("- PHASE " + phase + " REACHED");
		if (phase == 2) 
		{
			send(players,"Who deals the most damage out of you?",10);
			send(players,"They'll taste my laser!",30);
		}
		else if (phase == 3)
		{
			send(players,"Defenders! Guard me!",10);
			defenders.add(DMobManager.spawn("necropolis_defender", new SpawnPosition(new Location(Dungeons.w,19.5,98,22055.5,90,30))));
			defenders.add(DMobManager.spawn("necropolis_defender", new SpawnPosition(new Location(Dungeons.w,14.5,98,22055.5,-90,30))));
		}
		else if (phase == 4)
		{
			if (!defenders.isEmpty()) for (DMob d : defenders) d.remove();
			defenders.clear();
			send(players,"You beat some of my strongest warriors.",20);
			send(players,"It is time for me to enter the battle..",40);
			send(players,"I will destroy you myself...",60);
			
			boss.setMoving();
			boss.setVulnerable();
		}
		
	}


	
	private void reset()
	{
		ArrayList<DungeonsPlayer> players = area.getWithin();
		for (DungeonsPlayer d : players) d.player.removePotionEffect(PotionEffectType.SLOW);
		
		if (bar != null) bar.removeAll();
		bar = null;
		
		if (phase1 != null && !phase1.isEmpty()) for (DMob m : phase1) if (m != null) m.remove();
		if (!defenders.isEmpty()) for (DMob d : defenders) d.remove();
		phase1.clear();
		defenders.clear();
		
		if (boss != null) boss.remove();
		boss = null;
		
		
		
		for (Block b : replaced.keySet()) b.setType(replaced.get(b));
		replaced.clear();
		
		if (alive)
		{
			if (phase == 5)
			{
				System.out.println("NECROPOLIS BOSS DEFEATED");
				String list = "";
				for (DungeonsPlayer d : players) list = list + d.player.getName() + ", ";
				list = list.substring(0, list.length() - 2);
				System.out.println("- Got Loot: " + list);
			}
			else System.out.println("NECROPOLIS ATTEMPT UNSUCCESSFUL");
		}
		
		alive = false;
	}
	private void spawn()
	{
		//13,111,22032
		//20,101,22031
		ArrayList<DungeonsPlayer> players = area.getWithin();
		
		String list = "";
		bar = Bukkit.createBossBar(ChatColor.GOLD + "The Grave Lord", BarColor.RED, BarStyle.SOLID, new BarFlag[0]);
		for (DungeonsPlayer d : players) 
		{
			list = list + d.player.getName() + ", ";
			bar.addPlayer(d.player);
			d.player.teleport(new Location(Dungeons.w,17,101,22035.5,0,6));
		}
		list = list.substring(0, list.length() - 2);
		System.out.println("NECROPOLIS BOSS ATTEMPT");
		System.out.println("- Players: " + list);
		
		for (int x = 13; x <= 20;x++)
		{
			for (int y = 101; y <= 111;y++)
			{
				Block b = Dungeons.w.getBlockAt(x, y, 22031);
				replaced.put(b, b.getType());
				b.setType(Material.OAK_WOOD);
			}
		}
		
		boss = DMobManager.spawn("necropolis_boss", new SpawnPosition(17,99,22058.5,-180,0));
		boss.setStatic();
		boss.setInvulnerable();
		
		
		send(players,"You dare challenge me?",10);
		send(players,"You will DIE!",30);
		
		tick = 0;
		phase = 1;
		alive = true;
	}
	
	public void send(ArrayList<DungeonsPlayer> players,String msg,int ticks)
	{
		if (ticks == 0) for (DungeonsPlayer p : players) p.player.sendMessage(ChatColor.RED + "Grave Lord: " + ChatColor.WHITE + msg);
		else
		{
			new BukkitRunnable()
			{

				@Override
				public void run() {
					for (DungeonsPlayer p : players) p.player.sendMessage(ChatColor.RED + "Grave Lord: " + ChatColor.WHITE + msg);
					
				}
				
			}.runTaskLater(Dungeons.instance, ticks);
		}
	}
	
	@Override
	public void end()
	{
		reset();
	}
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (!e.hasItem() || e.getItem() == null || !e.getItem().hasItemMeta()) return false;

		String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"");
		
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		
		if (it.equals("summonkey_necropolis"))
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
