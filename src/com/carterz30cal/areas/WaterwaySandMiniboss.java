package com.carterz30cal.areas;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.tasks.TaskSendMsg;
import com.carterz30cal.utility.BoundingBox;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.Mafs;
import com.carterz30cal.utility.RandomFunctions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import net.md_5.bungee.api.ChatColor;

public class WaterwaySandMiniboss extends AbsDungeonEvent 
{
	public boolean alive;
	public int health;
	
	private final int maxhp = 10;
	
	public BoundingBox area = new BoundingBox(new Location(Dungeons.w,-47,92,21062),new Location(Dungeons.w,-33,104,21075));
	
	private List<Block> modified = new ArrayList<>();
	private Location tele = new Location(Dungeons.w,-40, 93, 21064);
	private Location minions = new Location(Dungeons.w,-40,93,21069);
	private int tick = 0;
	
	private BossBar bar;
	
	private List<HydraHead> heads = new ArrayList<>();
	private List<DMob> minionLi = new ArrayList<>();
	
	private Location[] hydra_lime = 
	{
			new Location(Dungeons.w,-39,93,21074),new Location(Dungeons.w,-39,93,21073),new Location(Dungeons.w,-39,94,21073),
			new Location(Dungeons.w,-39,94,21072),new Location(Dungeons.w,-39,95,21072),new Location(Dungeons.w,-39,94,21071),
			new Location(Dungeons.w,-39,95,21071),
			new Location(Dungeons.w,-40,93,21074),new Location(Dungeons.w,-40,93,21073),new Location(Dungeons.w,-40,94,21073),
			new Location(Dungeons.w,-40,94,21072),new Location(Dungeons.w,-40,95,21072),new Location(Dungeons.w,-40,94,21071),
			new Location(Dungeons.w,-40,95,21071),
			new Location(Dungeons.w,-41,93,21074),new Location(Dungeons.w,-41,93,21073),new Location(Dungeons.w,-41,94,21073),
			new Location(Dungeons.w,-41,94,21072),new Location(Dungeons.w,-41,95,21072),new Location(Dungeons.w,-41,94,21071),
			new Location(Dungeons.w,-41,95,21071)
	};
	private Location[] hydra_blue = {
			new Location(Dungeons.w,-39,93,21072),new Location(Dungeons.w,-41,93,21072)
	};
	
	public void spawn()
	{
		alive = true;
		health = maxhp;
		bar = Bukkit.createBossBar(ChatColor.GOLD + "Sand Hydra", BarColor.RED, BarStyle.SOLID, new BarFlag[0]);

		//-42,93,21062 corner 1 door
		//-37,98,21062 corner 2 door
		for (DungeonsPlayer p : area.getWithin()) 
		{
			p.player.teleport(tele);
			bar.addPlayer(p.player);
		}
		
		heads.add(new HydraHead(new Location(Dungeons.w,-39.5,95,21069),new Location(Dungeons.w,-39.5,94.5,21071)));
		heads.add(new HydraHead(new Location(Dungeons.w,-36.5,94,21069),new Location(Dungeons.w,-38.5,94.5,21071)));
		heads.add(new HydraHead(new Location(Dungeons.w,-42.5,94,21069),new Location(Dungeons.w,-40.5,94.5,21071)));
		
		
		for (int x = -42; x <= -37;x++)
		{
			for (int y = 93; y <= 98;y++)
			{
				Block c = Dungeons.w.getBlockAt(x, y, 21062);
				if (c.getType() == Material.AIR)
				{
					modified.add(c);
					c.setType(Material.RED_TERRACOTTA);
				}
			}
		}
		for (Location l : hydra_lime)
		{
			Block b = Dungeons.w.getBlockAt(l);
			b.setType(Material.LIME_TERRACOTTA);
			modified.add(b);
		}
		for (Location l : hydra_blue)
		{
			Block b = Dungeons.w.getBlockAt(l);
			b.setType(Material.BLUE_TERRACOTTA);
			modified.add(b);
		}
	}

	@Override
	public void tick()
	{
		if (!alive) return;
		
		tick++;
		ArrayList<DungeonsPlayer> players = area.getWithin();
		
		int alive = 0;
		for (HydraHead h : heads)
		{
			if (h.mob == null && h.degrees == 200) h.respawn();
			else if (h.mob == null) h.degrees++;
			else if (h.mob.health < 1) 
			{
				new TaskSendMsg(players,ChatColor.RED + "Sand Hydra: " + ChatColor.WHITE + "Stop destroying my heads!",1);
				health--;
				minionLi.add(DMobManager.spawn("sands_hydrasoul", new SpawnPosition(h.mob.entities.get(0).getLocation())));
				h.kill();
			}
			else 
			{
				h.update();
				alive++;
			}
		}

		bar.setProgress((double)health / maxhp);
		if (players.size() == 0)
		{
			end();
		}
		else if (alive == 0 || health == 0)
		{
			new TaskSendMsg(players,ChatColor.RED + "Sand Hydra: " + ChatColor.WHITE + "How? I'm unstoppable!?",1);
			for (DungeonsPlayer d : players) InventoryHandler.addItem(d, ItemBuilder.i.build("loot_hydra", 1));
			end();
			return;
		}
		
		if (tick % 250 == 0)
		{
			for (int i = 0; i < 2;i++) minionLi.add(DMobManager.spawn("sands_hydraminion", new SpawnPosition(minions)));
		}
	}
	@Override
	public void end()
	{
		alive = false;
		health = 0;
		if (bar != null) bar.removeAll();
		
		for (Block c : modified)
		{
			c.setType(Material.AIR);
		}
		modified.clear();
		
		for (HydraHead h : heads) h.kill();
		heads.clear();
		for (DMob su : minionLi) su.remove();
		minionLi.clear();
	}
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (!e.hasItem() || e.getItem() == null || !e.getItem().hasItemMeta() || e.getClickedBlock() == null) return false;

		String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"");
		
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		
		if (it.equals("essence_of_sand") && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.END_PORTAL_FRAME)
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

class HydraHead
{
	public static final int neckLength = 15;
	public Location tether;
	public Location spawn;
	
	public double tx = 0;
	public double ty = 0;
	public double tz = 0;
	
	public List<ArmorStand> neck = new ArrayList<>();
	public DMob mob;
	public double degrees; // also timer for being realive.
	
	
	public void kill()
	{
		degrees = 0;
		if (mob != null)
		{
			
			mob.remove();
			mob = null;
		}
		for (ArmorStand a : neck) a.remove();
		neck.clear();
		
		
	}
	public HydraHead(Location s,Location t)
	{
		tx = t.getX();
		ty = t.getY();
		tz = t.getZ();
		
		spawn = s.clone();
		tether = t.clone();
		degrees = RandomFunctions.random(0, 359);
		
		mob = DMobManager.spawn("sands_hydrahead",new SpawnPosition(spawn));
		mob.entities.get(0).setGravity(false);
		Location l = tether.clone();
		Location dis = mob.entities.get(0).getLocation().add(0, 0, 0.25).clone().subtract(l.clone()).multiply(1d/neckLength);
		for (int n = 0; n < neckLength;n++)
		{
			ArmorStand a = (ArmorStand)Dungeons.w.spawnEntity(l.add(dis), EntityType.ARMOR_STAND);
			a.setInvisible(true);
			a.setMarker(true);
			a.getEquipment().setHelmet(new ItemStack(Material.LIME_TERRACOTTA));
			a.setGravity(false);
			a.setSmall(true);
			neck.add(a);
		}
	}
	public void respawn()
	{
		tether = new Location(Dungeons.w,tx,ty,tz);
		degrees = RandomFunctions.random(0, 359);
		
		
		
		mob = DMobManager.spawn("sands_hydrahead",new SpawnPosition(spawn.clone()));
		mob.entities.get(0).setGravity(false);
		
		Location l = tether.clone();
		Location dis = mob.entities.get(0).getLocation().clone().add(0, 0, 0.25).subtract(l.clone()).multiply(1d/neckLength);

		for (int n = 0; n < neckLength;n++)
		{
			ArmorStand a = (ArmorStand)Dungeons.w.spawnEntity(l.add(dis), EntityType.ARMOR_STAND);
			a.setInvisible(true);
			a.setInvulnerable(true);
			a.setMarker(true);
			a.getEquipment().setHelmet(new ItemStack(Material.LIME_TERRACOTTA));
			a.setGravity(false);
			a.setSmall(true);
			neck.add(a);
		}
	}
	
	public void update()
	{
		Location l = tether.clone();
		mob.entities.get(0).teleport(spawn.clone().add(Mafs.getCircleX((double)degrees, 0.5),Mafs.getCircleZ((double)degrees, 0.5),0));
		Location dis = mob.entities.get(0).getLocation().clone().add(0, 0, 0.25).subtract(tether.clone()).multiply(1d/neckLength);
		degrees += 0.05;
		tether = new Location(Dungeons.w,tx,ty,tz);
		for (ArmorStand a : neck) a.teleport(l.add(dis));
	}
}



