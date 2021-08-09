package com.carterz30cal.areas;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.ArmourstandFunctions;
import com.carterz30cal.utility.BoundingBox;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.Mafs;
import com.carterz30cal.utility.ParticleFunctions;
import com.carterz30cal.utility.RandomFunctions;
import com.carterz30cal.utility.Square;
import com.carterz30cal.utility.StdUtils;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public class InfestedTemple extends AbsDungeonEvent
{
	public BoundingBox area = new BoundingBox(new Location(Dungeons.w,37,109,22980),new Location(Dungeons.w,44,106,22976));
	public BoundingBox triggerBox = new BoundingBox(new Location(Dungeons.w,33,10,22976),new Location(Dungeons.w,33,13,22978));
	public BoundingBox triggerBox2 = new BoundingBox(new Location(Dungeons.w,63,10,22987),new Location(Dungeons.w,62,14,22987));
	public BoundingBox portal = new BoundingBox(new Location(Dungeons.w,107,28,22996),new Location(Dungeons.w,107,24,22992));
	public List<DungeonsPlayer> messages = new ArrayList<>();
	public List<DungeonsPlayer> triggered = new ArrayList<>();
	public List<DungeonsPlayer> triggered2 = new ArrayList<>();
	public List<DungeonsPlayer> teleporting = new ArrayList<>();
	//63,10,22987
	public ArmorStand display;
	public ArmorStand description;
	public int flameTick;
	
	
	/*
	 * BOSS STUFF BELOW HERE
	 * 
	 * 
	 * 
	 * 
	 */
	
	// basic details
	public BoundingBox bossRoom = new BoundingBox(new Location(Dungeons.w,79,3,22976),new Location(Dungeons.w,103,30,22950));
	public boolean alive;
	public int aliveTick;
	public List<DungeonsPlayer> inBoss = new ArrayList<>();
	public int stage;
	public BossBar sathlar;
	
	public DMob eye1;
	public DungeonsPlayer eye1Target;
	public DMob eye2;
	public DungeonsPlayer eye2Target;
	
	public DMob construct;
	public List<Tentacle> tentacles = new ArrayList<>();
	public List<DMob> servants = new ArrayList<>();
	public List<DMob> spirits = new ArrayList<>();
	
	public static List<Reward> rewards = new ArrayList<>();
	
	
	
	public BlockData button;
	
	
	public InfestedTemple()
	{
		display = ArmourstandFunctions.create(new Location(Dungeons.w,38.5,107,22978.5));
		display.setCustomName(ChatColor.DARK_RED + "THE TEMPLE");
		description = ArmourstandFunctions.create(new Location(Dungeons.w,38.5,106.5,22978.5));
		description.setCustomName(ChatColor.DARK_RED + "Enter if ye dare..");
		
		sathlar = Bukkit.createBossBar(ChatColor.RED + "Sathlar", BarColor.RED, BarStyle.SOLID, new BarFlag[] {});
	}
	//107,28,22996
	//107,25,22992
	public void tick()
	{
		flameTick++;
		List<DungeonsPlayer> players = area.getWithin();
		players.removeIf((DungeonsPlayer d) -> teleporting.contains(d));
		if (flameTick >= 30)
		{
			for (DungeonsPlayer d : players)
			{
				String s = ItemBuilder.getItem(d.player.getInventory().getItemInMainHand());
				if (s != null && s.equals("temple_token"))
				{
					Location l = new Location(Dungeons.w,38.1,107.7,22980.5);
					for (int i = 0; i < 10;i++)
					{
						ParticleFunctions.stationary(l.clone().add(d.player.getEyeLocation().subtract(0,0.6,0).subtract(l.clone()).multiply(i/10d)), Particle.FLAME, 1);
					}
					l = new Location(Dungeons.w,38.1,107.7,22976.5);
					for (int i = 0; i < 10;i++)
					{
						ParticleFunctions.stationary(l.clone().add(d.player.getEyeLocation().subtract(0,0.6,0).subtract(l.clone()).multiply(i/10d)), Particle.FLAME, 1);
					}
				}
			}
			flameTick = 0;
		}
		
		
		players.removeIf((DungeonsPlayer d ) -> messages.contains(d));
		for (DungeonsPlayer d : players)
		{
			d.player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "The temple whispers....");
			d.player.playSound(d.player.getLocation(), Sound.AMBIENT_CAVE, 0.8f, 1);
			messages.add(d);
		}
		List<DungeonsPlayer> t = triggerBox.getWithin();
		t.removeIf((DungeonsPlayer d ) -> triggered.contains(d));
		for (DungeonsPlayer d : t)
		{
			d.player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "I'll see you soon..");
			triggered.add(d);
		}
		t = triggerBox2.getWithin();
		t.removeIf((DungeonsPlayer d ) -> triggered2.contains(d));
		for (DungeonsPlayer d : t)
		{
			d.player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "Nearly there... :)");
			triggered2.add(d);
		}
		t = portal.getWithin();
		t.removeIf((DungeonsPlayer d ) -> !d.tutorials.contains("Chamber Access"));
		for (DungeonsPlayer d : t)
		{
			d.player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "Welcome adventurer..");
			if (alive)
			{
				inBoss.add(d);
				sathlar.addPlayer(d.player);
			}
			d.player.teleport(new Location(Dungeons.w,80,4,22963,-90,0));
		}
		
		if (alive) aliveTick();
	}
	//
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().equals(Dungeons.w.getBlockAt(64,12,22994)))
			{
				d.player.teleport(new Location(Dungeons.w,87,25,22994,-90,0));
				return true;
			}
			else if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().equals(Dungeons.w.getBlockAt(93,5,22963)))
			{
				new BukkitRunnable()
				{

					@Override
					public void run() {
						start();
						
					}
					
				}.runTaskLater(Dungeons.instance, 2);
				
				return true;
			}
			String s = ItemBuilder.getItem(e.getPlayer().getInventory().getItemInMainHand());
			
			if (s == null || !s.equals("temple_token") || !area.getWithin().contains(d) || teleporting.contains(d)) return false;
			
			new TeleportTask(d,teleporting);
			return true;
		}
		
		return false;
	}
	public void onPlayerDeath(DungeonsPlayer died)
	{
		messages.remove(died);
		triggered.remove(died);
		triggered2.remove(died);
		inBoss.remove(died);
		
		eye1Target = null;
		eye2Target = null;
	}
	
	public void end()
	{
		display.remove();
		description.remove();
		
		for (Reward r : rewards) r.cancel();
		reset();
	}
	
	// TO DECLUTTER tick()!!!
	public void aliveTick()
	{
		aliveTick++;
		if (inBoss.size() == 0) reset();
		if (stage == 1)
		{
			double thp = 700000 + eye1.health + eye2.health;
			sathlar.setProgress(thp / 1000000d);
			
			if (aliveTick % 40 == 0)
			{
				if (eye1Target == null)
				{
					inBoss.sort((a,b) -> a.getHealth() * a.stats.armour >= b.getHealth() * b.stats.armour ? -1 : 1);
					eye1Target = inBoss.get(0);
				}
				if (eye2Target == null && inBoss.size() > 1)
				{
					inBoss.sort((a,b) -> a.getHealth() * a.stats.armour >= b.getHealth() * b.stats.armour ? -1 : 1);
					eye2Target = inBoss.get(1);
				}
				else if (eye1.health < 1 && eye2Target == null)
				{
					inBoss.sort((a,b) -> a.getHealth() * a.stats.armour >= b.getHealth() * b.stats.armour ? -1 : 1);
					eye2Target = inBoss.get(0);
				}
				else if (eye1.health < 1 && eye2.health < 1) stage2();
				if (eye1Target != null && eye1.health > 0)
				{
					eye1Target.damage(150, false);
					StdUtils.drawLaser(eye1Target.player.getLocation(), new Location(Dungeons.w,101,10.5,22959.5), 50, 0.6f, Color.ORANGE);
					StdUtils.damageAnim(eye1Target);
				}
				if (eye2Target != null && eye2.health > 0)
				{
					eye2Target.damage(150, false);
					StdUtils.drawLaser(eye2Target.player.getLocation(), new Location(Dungeons.w,101,10.5,22967.5), 50, 0.6f, Color.ORANGE);
					StdUtils.damageAnim(eye2Target);
				}
			}
		}
		else if (stage == 2)
		{
			if (construct.health <= 0) stage3();
			else
			{
				double thp = 500000 + construct.health;
				sathlar.setProgress(thp / 1000000d);
			}
		}
		else if (stage == 3)
		{
			double hp = 0;
			for (DMob s : spirits) hp += s.health;
			spirits.removeIf((DMob m ) -> m.health < 1);
			sathlar.setProgress(hp / 1000000d);
			if (spirits.size() == 0) completed();
		}
		
		// 102.5,4,22951.5
		if (aliveTick % 300 == 0 && servants.size() < 25) 
		{
			
			for (int i = 0; i < 5 && servants.size() < 25;i++) 
			{
				servants.add(DMobManager.spawn("temple_boss_servant" + stage, new SpawnPosition(102.5,4,22951.5)));
			}
		}
		else if (servants.size() >= 25) servants.removeIf((DMob m) -> m.health < 1);
		
		
		if ((aliveTick+1300) % 1400 == 0 && tentacles.size() < stage && tentacles.size() < inBoss.size()) spawnTentacle();
		
		List<Tentacle> dead = new ArrayList<>();
		for (Tentacle t : tentacles)
		{
			if (t.head.health < 1)
			{
				dead.add(t);
			}
			Vector ro = t.anchor.toVector().subtract(t.target.player.getEyeLocation().subtract(0,0.5,0).toVector()).normalize();
			float yaw = 180 - toDegree(Math.atan2(ro.getX(), ro.getZ()));
			float pitch = 90 - toDegree(Math.acos(ro.getY()));
			EulerAngle res = new EulerAngle(Math.toRadians(yaw),Math.toRadians(pitch),0);
			
			if (t.state == TentacleState.GROWING )
			{
				int req = (int) (t.target.player.getLocation().distance(t.anchor) * 2) - 1;
				if (req > t.length.size() && aliveTick % 15 == 5)
				{
					ArmorStand a = ArmourstandFunctions.create(t.anchor);
					a.getEquipment().setHelmet(new ItemStack(Material.RED_CONCRETE));
					a.setCustomNameVisible(false);
					t.length.add(a);
				}
				//
				Location d = t.anchor.clone();
				Vector dir2 = t.target.player.getEyeLocation().subtract(0,0.5,0).toVector().subtract(t.anchor.toVector()).normalize().multiply(0.5);
				
				Vector dir = dir2.clone().multiply(2);
	
				for (ArmorStand a : t.length)
				{
					d.add(dir2);
					
					a.setHeadPose(res);
					a.teleport(d);
					if (d.distance(t.target.player.getEyeLocation()) < 0.6) 
					{
						t.y = t.target.player.getLocation().getY();
						t.state = TentacleState.HIT;
						break;
					}
				}
				t.head.entities.get(0).teleport(d.add(dir).subtract(0, 1, 0));
				((ArmorStand)t.head.entities.get(0)).setHeadPose(res);
			}
			else if (t.state == TentacleState.HIT)
			{
				if (aliveTick % 10 == 0) 
				{
					Location set = t.target.player.getLocation();
					Vector o = t.anchor.toVector().subtract(t.target.player.getEyeLocation().subtract(0,0.5,0).toVector()).normalize().multiply(0.6);
					set.add(o.getX(), 0.25, o.getZ());
					if (set.getY() > 14.5) 
					{
						t.head.health -= 650;
						set.setY(14.5);
					}
					t.target.player.teleport(set);
					
				}
				t.target.player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,2,255,true));
				
				Vector dir = t.target.player.getEyeLocation().subtract(0,0.5,0).toVector().subtract(t.anchor.toVector()).multiply(1d / (t.length.size()+1));
				Location d = t.anchor.clone();
				
				for (ArmorStand a : t.length)
				{
					d.add(dir);
					a.setHeadPose(res);
					a.teleport(d);
				}
				t.head.entities.get(0).teleport(d.add(dir).subtract(0, 1, 0));
				((ArmorStand)t.head.entities.get(0)).setHeadPose(res);
				
				if (aliveTick % 10 == 0)
				{
					StdUtils.damageAnim(t.target);
					t.target.damage(70, false);
					t.target.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,11,2,true));
				}
				
			}
			
		}
		for (Tentacle t : dead)
		{
			t.head.remove();
			for (ArmorStand a : t.length) 
			{
				a.setGravity(true);
				new BukkitRunnable()
				{

					@Override
					public void run() {
						a.setHealth(0);
					}
					
				}.runTaskLater(Dungeons.instance, 20);
			}
			tentacles.remove(t);
		}
		
		if (stage > 1 && aliveTick % 1200 == 400)
		{
			int side = RandomFunctions.random(1, 2);
			new BukkitRunnable()
			{
				int c = 0;

				@Override
				public void run() {
					if (c == 5)
					{
						if (side == 1) servants.add(DMobManager.spawn("temple_boss_archer",new SpawnPosition(84.5,4,22954.5)));
						else servants.add(DMobManager.spawn("temple_boss_archer",new SpawnPosition(84.5,4,22972.5)));
						
						cancel();
					}
					Color col = c % 2 == 0 ? Color.RED : Color.WHITE;
					if (side == 1)
					{
						StdUtils.drawLaser(new Location(Dungeons.w,83,4.1,22953), new Location(Dungeons.w,86,4.1,22953), 10, 1.3f, col);
						StdUtils.drawLaser(new Location(Dungeons.w,83,4.1,22956), new Location(Dungeons.w,86,4.1,22956), 10, 1.3f, col);
						StdUtils.drawLaser(new Location(Dungeons.w,83,4.1,22953), new Location(Dungeons.w,83,4.1,22956), 10, 1.3f, col);
						StdUtils.drawLaser(new Location(Dungeons.w,86,4.1,22953), new Location(Dungeons.w,86,4.1,22956), 10, 1.3f, col);
					}
					else
					{
						//22971
						StdUtils.drawLaser(new Location(Dungeons.w,83,4.1,22971), new Location(Dungeons.w,86,4.1,22971), 10, 1.3f, col);
						StdUtils.drawLaser(new Location(Dungeons.w,83,4.1,22974), new Location(Dungeons.w,86,4.1,22974), 10, 1.3f, col);
						StdUtils.drawLaser(new Location(Dungeons.w,83,4.1,22971), new Location(Dungeons.w,83,4.1,22974), 10, 1.3f, col);
						StdUtils.drawLaser(new Location(Dungeons.w,86,4.1,22971), new Location(Dungeons.w,86,4.1,22974), 10, 1.3f, col);
					}
					c++;
				}
				
			}.runTaskTimer(Dungeons.instance, 0, 10);
		}
		
		
		// burping
		if ((aliveTick + 600) % 1100 == 0)
		{
			new BukkitRunnable()
			{
				int c = 0;
				@Override
				public void run() {
					if (c == 6)
					{
						BoundingBox burp = new BoundingBox(new Location(Dungeons.w,103,3,22958),new Location(Dungeons.w,79,16,22968));
						List<DungeonsPlayer> affected = burp.getWithin();
						
						
						for (DungeonsPlayer d : affected)
						{
							StdUtils.damageAnim(d);
							int dmg = 160;
							if (stage < 3) d.player.setVelocity(new Vector(-2.75,1,0));
							else
							{
								dmg = 220;
								d.player.setVelocity(new Vector(-3.5,1.35,0));
							}
							d.damage(dmg, true);
							d.player.sendMessage(ChatColor.RED + "Sathlar used" + ChatColor.DARK_RED + " Gigaburp " + ChatColor.RED + "on you and dealt " + dmg + " true damage.");
							
						}
						if (stage != 3) say("Ugh.. that was rotten.");
						for (DungeonsPlayer d : inBoss) d.player.playSound(new Location(Dungeons.w,98,4,22963), Sound.ENTITY_ENDER_DRAGON_GROWL, 2f, 0.7f);
						cancel();
						return;
					}
					if (c % 2 == 0)
					{
						StdUtils.drawLaser(new Location(Dungeons.w,80,4,22958.5), new Location(Dungeons.w,100,4,22958.5), 100, 1.3f, Color.RED);
						StdUtils.drawLaser(new Location(Dungeons.w,80,4,22968.5), new Location(Dungeons.w,100,4,22968.5), 100, 1.3f, Color.RED);
					}
					else
					{
						StdUtils.drawLaser(new Location(Dungeons.w,80,4.1,22958.5), new Location(Dungeons.w,100,4.1,22958.5), 100, 1.2f, Color.WHITE);
						StdUtils.drawLaser(new Location(Dungeons.w,80,4.1,22968.5), new Location(Dungeons.w,100,4.1,22968.5), 100, 1.2f, Color.WHITE);
					}
					c++;
				}
				
			}.runTaskTimer(Dungeons.instance, 0, 10);
		}
		
	}
	
	private float toDegree(double angle)
	{
		return (float) Math.toDegrees(angle);
	}
	public void stage2()
	{
		stage = 2;
		say("ARGGHHHH!! YOU BLINDED ME!");
		construct = DMobManager.spawn("temple_boss_construct", new SpawnPosition(91.5,4,22963.5));
	}
	public void stage3()
	{
		stage = 3;
		say("Fight my spirits instead. I'll be back.");
		say("...Maybe.");
		spirits.add(DMobManager.spawn("temple_boss_spirit", new SpawnPosition(88.5,4,22960.5)));
		spirits.add(DMobManager.spawn("temple_boss_spirit", new SpawnPosition(88.5,4,22966.5)));
		
		sathlar.setTitle(ChatColor.BLUE + "Sathlar's Spirits");
		sathlar.setColor(BarColor.BLUE);
	}
	public void completed()
	{
		stage = 4;
		System.out.println("SATHLAR FIGHT COMPLETE!");
		say("Midas won't be happy with me.");
		say("..Let me rest, vile humans.");
		for (DungeonsPlayer player : inBoss)
		{
			rewards.add(new Reward(player));
			player.player.teleport(new Location(Dungeons.w,83.5,4,22963.5,-90,0));
			//player.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,5,5,true));
		}
		
		reset();
	}
	
	public void spawnTentacle()
	{
		Square spawns = new Square(95,22969,82,22957,17);
		Location rsp = spawns.getRandom();
		DMob head = DMobManager.spawn("temple_boss_tentacle" + stage, new SpawnPosition(rsp));
		ArmorStand h = (ArmorStand)head.entities.get(0);
		h.setHeadPose(new EulerAngle(3.14159,0,3.14159));
		
		List<DungeonsPlayer> claimed = new ArrayList<>();
		for (Tentacle t : tentacles) claimed.add(t.target);
		List<DungeonsPlayer> options = new ArrayList<>(inBoss);
		options.removeAll(claimed);
		
		DungeonsPlayer target = (DungeonsPlayer) RandomFunctions.get(options.toArray());
		
		int r = RandomFunctions.random(0, 5);
		String n =  target.player.getName();
		
		if (stage == 3);
		else if (r == 0) say("You're dead meat, " + n + "!");
		else if (r == 1) say("Heheh, " + n + ", you haven't got long left!");
		else if (r == 2) say("A true test of strength! How will " + n + " fare?");
		else if (r == 3) say(n + " stands no chance against my tentacle!");
		else if (r == 4) say("Ohhh you're dead now " + n + ".");
		else say("This one is my favourite! Do you like it, " + n + "?");
		
		
		// initial length is 3 and it grows until it reaches the target
		List<ArmorStand> length = new ArrayList<>();
		for (int i = 0; i < 3;i++)
		{
			ArmorStand a = ArmourstandFunctions.create(rsp);
			a.getEquipment().setHelmet(new ItemStack(Material.RED_CONCRETE));
			a.setMarker(true);
			a.setCustomNameVisible(false);
			length.add(a);
		}
		
		Tentacle construct = new Tentacle();
		construct.head = head;
		construct.length = length;
		construct.target = target;
		construct.state = TentacleState.GROWING;
		construct.anchor = rsp;
		
		tentacles.add(construct);
	}
	
	public void start()
	{
		if (alive) return;
		alive = true;
		aliveTick = 0;
		stage = 1;
		inBoss = bossRoom.getWithin();
		for (DungeonsPlayer i : inBoss) sathlar.addPlayer(i.player);
		Dungeons.w.getBlockAt(94,4,22962).setType(Material.AIR); // BEDROCK
		Dungeons.w.getBlockAt(94,4,22963).setType(Material.AIR);
		Dungeons.w.getBlockAt(94,4,22964).setType(Material.AIR);
		Dungeons.w.getBlockAt(94,5,22962).setType(Material.AIR);
		Dungeons.w.getBlockAt(94,5,22963).setType(Material.AIR);
		Dungeons.w.getBlockAt(94,5,22964).setType(Material.AIR);
		
		button = Dungeons.w.getBlockAt(93,5,22963).getBlockData();
		Dungeons.w.getBlockAt(93,5,22963).setType(Material.AIR); // BUTTON
		
		eye1 = DMobManager.spawn("temple_boss_eye", new SpawnPosition(new Location(Dungeons.w,100.7,9,22959.5)));
		eye2 = DMobManager.spawn("temple_boss_eye", new SpawnPosition(new Location(Dungeons.w,100.7,9,22967.5)));
	}
	public void say(String msg)
	{
		for (DungeonsPlayer d : inBoss) d.player.sendMessage(ChatColor.DARK_RED + "[Sathlar]: " + ChatColor.RED + msg);
	}
	public void reset()
	{
		if (!alive) return;
		alive = false;
		aliveTick = 0;
		stage = 0;
		
		Dungeons.w.getBlockAt(94,4,22962).setType(Material.BEDROCK);
		Dungeons.w.getBlockAt(94,4,22963).setType(Material.BEDROCK);
		Dungeons.w.getBlockAt(94,4,22964).setType(Material.BEDROCK);
		Dungeons.w.getBlockAt(94,5,22962).setType(Material.BEDROCK);
		Dungeons.w.getBlockAt(94,5,22963).setType(Material.BEDROCK);
		Dungeons.w.getBlockAt(94,5,22964).setType(Material.BEDROCK);
		
		Block b = Dungeons.w.getBlockAt(93,5,22963);
		b.setBlockData(button);
		
		button = null;
		
		eye1.remove();
		eye1Target = null;
		eye2.remove();
		eye2Target = null;
		
		sathlar.removeAll();
		sathlar = Bukkit.createBossBar(ChatColor.RED + "Sathlar", BarColor.RED, BarStyle.SOLID, new BarFlag[] {});
		
		for (Tentacle tentacle : tentacles)
		{
			tentacle.head.remove();
			for (ArmorStand a : tentacle.length) a.remove();
		}
		tentacles.clear();
		
		for (DMob m : servants) m.remove();
		servants.clear();
	}
}
class Tentacle 
{
	public DMob head;
	public List<ArmorStand> length;
	public DungeonsPlayer target;
	public Location anchor;
	public TentacleState state;
	public double y;
}
enum TentacleState
{
	GROWING,
	HIT;
}
class TeleportTask extends BukkitRunnable
{
	DungeonsPlayer d;
	List<DungeonsPlayer> blacklist;
	int time = 0;
	
	public TeleportTask(DungeonsPlayer o,List<DungeonsPlayer> bl)
	{
		d = o;
		blacklist = bl;
		bl.add(d);
		
		o.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,120,5,true));
		runTaskTimer(Dungeons.instance,1,1);
	}
	@Override
	public void run() {
		time++;
		if (time < 100)
		{
			ParticleFunctions.stationary(d.player.getLocation().add(Mafs.getCircleX(time, 0.6), 2 - (time/50d), Mafs.getCircleZ(time, 0.6)), Particle.SOUL_FIRE_FLAME, 1);
			ParticleFunctions.stationary(d.player.getLocation().add(Mafs.getCircleX(time, 0.8), time/50d, Mafs.getCircleZ(time, 0.8)), Particle.VILLAGER_HAPPY, 1);
		}
		else if (time == 100) ParticleFunctions.moving(d.player.getLocation().add(0,1,0), Particle.SOUL, 70, 0.15);
		else if (time == 110)
		{
			d.player.teleport(new Location(Dungeons.w,36.5,5,22978.5));
			blacklist.remove(d);
			cancel();
			
			d.player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "The temple growls..");
			d.player.playSound(d.player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.3f, 0.7f);
		}
		
	}
	
}

class Reward extends BukkitRunnable
{
	public static final String[] pool = {"mythical_templesword","mythical_templebow","mythical_saoref",
			"mythical_ingot","mythical_scale","mythical_soulingredient",
			"mythical_spell","armour_mythicaltemple_chestplate","temple_note1","temple_note2","book"};
	
	public DungeonsPlayer owner;
	public ArmorStand reward1;
	public ArmorStand reward2;
	public ArmorStand reward3;
	
	private int lifetime;
	private static final int pickup = 1;
	
	public Reward(DungeonsPlayer o)
	{
		owner = o;
		
		reward1 = ArmourstandFunctions.create(new Location(Dungeons.w,88.5,4.5,22960.5));
		reward2 = ArmourstandFunctions.create(new Location(Dungeons.w,91.5,4.5,22963.5));
		reward3 = ArmourstandFunctions.create(new Location(Dungeons.w,88.5,4.5,22966.5));
		reward1.getEquipment().setHelmet(choice(1));
		reward1.setCustomName(reward1.getEquipment().getHelmet().getItemMeta().getDisplayName());
		reward2.getEquipment().setHelmet(choice(2));
		reward2.setCustomName(reward2.getEquipment().getHelmet().getItemMeta().getDisplayName());
		reward3.getEquipment().setHelmet(choice(3));
		reward3.setCustomName(reward3.getEquipment().getHelmet().getItemMeta().getDisplayName());
		
		
		PacketPlayOutEntityDestroy packet1 = new PacketPlayOutEntityDestroy(reward1.getEntityId());
		PacketPlayOutEntityDestroy packet2 = new PacketPlayOutEntityDestroy(reward2.getEntityId());
		PacketPlayOutEntityDestroy packet3 = new PacketPlayOutEntityDestroy(reward3.getEntityId());
		
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p == owner.player) continue;
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet1);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet2);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet3);
		}
		lifetime = 2400;
		runTaskTimer(Dungeons.instance,1,1);
	}
	
	private ItemStack choice(int num)
	{
		String p = pick(num);
		if (p.equals("book")) return ItemBuilder.i.build("book", owner, "boost,3");
		else return ItemBuilder.i.build(p, owner);
	}
	@Override
	public void cancel()
	{
		reward1.remove();
		reward2.remove();
		reward3.remove();
		super.cancel();
		try
		{
			new BukkitRunnable()
			{
				int life = 0;
				@Override
				public void run() {
					life++;
					if (life == 40)
					{
						owner.player.teleport(new Location(Dungeons.w,0,103,23000));
						owner.player.playSound(owner.player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
						cancel();
					}
					else if (life % 10 == 0)
					{
						for (int n = 0; n < 4;n++)
						{
							for (int d = 0; d < 360;d += 4) ParticleFunctions.stationary(owner.player.getLocation().add(Mafs.getCircleX(d, 1),n*0.5,Mafs.getCircleZ(d, 1)), Particle.VILLAGER_HAPPY, 1);
						}
					}
					
				}
				
			}.runTaskTimer(Dungeons.instance, 1, 1);
		}
		catch (IllegalPluginAccessException e)
		{
			
		}
		
		
	}
	private String pick(int n)
	{
		String pick = RandomFunctions.get(pool);
		if (n == 2 && ItemBuilder.getItem(reward1.getEquipment().getHelmet()).equals(pick)) return pick(2);
		else if (n == 3 && (ItemBuilder.getItem(reward1.getEquipment().getHelmet()).equals(pick) || ItemBuilder.getItem(reward2.getEquipment().getHelmet()).equals(pick))) return pick(3);
		return pick;
	}

	@Override
	public void run() 
	{
		lifetime--;
		
		if (reward1.getLocation().distance(owner.player.getLocation()) < pickup)
		{
			InventoryHandler.addItem(owner, reward1.getEquipment().getHelmet(), false);
			cancel();
			System.out.println("SATHLAR: " + owner.player.getName() + " took " + reward1.getEquipment().getHelmet().getItemMeta().getDisplayName());
			InfestedTemple.rewards.remove(this);
			owner.player.playSound(owner.player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
		}
		else if (reward2.getLocation().distance(owner.player.getLocation()) < pickup)
		{
			InventoryHandler.addItem(owner, reward2.getEquipment().getHelmet(), false);
			cancel();
			System.out.println("SATHLAR: " + owner.player.getName() + " took " + reward2.getEquipment().getHelmet().getItemMeta().getDisplayName());
			InfestedTemple.rewards.remove(this);
			owner.player.playSound(owner.player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
		}
		else if (reward3.getLocation().distance(owner.player.getLocation()) < pickup)
		{
			InventoryHandler.addItem(owner, reward3.getEquipment().getHelmet(), false);
			cancel();
			System.out.println("SATHLAR: " + owner.player.getName() + " took " + reward3.getEquipment().getHelmet().getItemMeta().getDisplayName());
			InfestedTemple.rewards.remove(this);
			owner.player.playSound(owner.player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
		}
		
		
		if (lifetime == 0) cancel();
		
		int offset = 0;
		rotate(reward1,offset);
		offset += 30;
		rotate(reward2,offset);
		offset += 45;
		rotate(reward3,offset);
	}
	
	private void rotate(ArmorStand a,int of)
	{
		Location old = a.getLocation();
		old.setYaw(old.getYaw()+3.3f);
		old.setY(4.5 + (Math.sin((lifetime+of) * 0.05d)*0.4));
		a.teleport(old);
	}
}



