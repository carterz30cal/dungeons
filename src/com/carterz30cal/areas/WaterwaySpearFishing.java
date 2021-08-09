package com.carterz30cal.areas;

import java.util.ArrayList;

import org.bukkit.Location;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.BoundingBox;

import net.md_5.bungee.api.ChatColor;

public class WaterwaySpearFishing extends AbsDungeonEvent
{
	public static final int spawnTick = 200;
	
	
	public BoundingBox area;
	ArrayList<SpawnPosition> spawns_f1;
	ArrayList<SpawnPosition> spawns_f2;
	SpawnPosition boss;
	private int tick;
	private int killsToBoss;
	public WaterwaySpearFishing()
	{
		super();
		area = new BoundingBox(new Location(Dungeons.w,-55,103,21012),new Location(Dungeons.w,-35,93,21044));
		
		spawns_f1 = new ArrayList<SpawnPosition>();
		spawns_f2 = new ArrayList<SpawnPosition>();
		spawns_f1.add(new SpawnPosition(-42,96,21038));
		spawns_f1.add(new SpawnPosition(-43,95,21037));
		spawns_f1.add(new SpawnPosition(-41,94,21039));
		spawns_f1.add(new SpawnPosition(-48,94,21032));
		spawns_f1.add(new SpawnPosition(-53,96,21035));
		spawns_f1.add(new SpawnPosition(-48,97,21029));
		spawns_f1.add(new SpawnPosition(-38,97,21032));
		
		spawns_f2.add(new SpawnPosition(-44,96,21030));
		spawns_f2.add(new SpawnPosition(-45,97,21029));
		spawns_f2.add(new SpawnPosition(-42,97,21032));
		
		boss = new SpawnPosition(-37,94,21042);
		
		killsToBoss = -(spawns_f1.size() + spawns_f2.size());
	}
	
	
	
	@Override
	public void tick()
	{
		if (tick >= spawnTick && area.getWithin().size() > 0)
		{
			for (SpawnPosition p : spawns_f1) 
			{
				if (p.mob != null) continue;
				new DMob(DMobManager.types.get("fish1"),p,p.position,false);
				killsToBoss++;
			}
			for (SpawnPosition p : spawns_f2) 
			{
				if (p.mob != null) continue;
				new DMob(DMobManager.types.get("fish2"),p,p.position,false);
				killsToBoss++;
			}
			tick = 0;
		}
		if (killsToBoss >= 20 && area.getWithin().size() > 0) 
		{
			if (boss.mob == null)
			{
				new DMob(DMobManager.types.get("fish3"),boss,boss.position,false);
				//for (DungeonsPlayer d : area.getWithin()) d.player.sendMessage(ChatColor.GOLD + "Fishin: " + ChatColor.GRAY + "A guardian has appeared!");
			}
			killsToBoss = 0;
		}
		tick++;
	}
}
