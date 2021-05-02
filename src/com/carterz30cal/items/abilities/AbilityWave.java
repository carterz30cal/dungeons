package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class AbilityWave extends AbsAbility
{

	public static Map<DungeonsPlayer,Integer> spawning = new HashMap<>();
	
	public static SpawnPosition[] spawns = {
			new SpawnPosition(-31, 100, 21046),new SpawnPosition(-24, 100, 21046),
			new SpawnPosition(-20, 97, 21057), new SpawnPosition(-37, 93, 21054),
			new SpawnPosition(-28, 91, 21045), new SpawnPosition(-24, 91, 21049),
			new SpawnPosition(-42, 94, 21049), new SpawnPosition(-40, 101, 21050),
			new SpawnPosition(-47, 105, 21054)
	};
	public static String[] mobs = {"fishing_sandguardian","fishing_husk","fishing_hydra"};
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Wave");
		desc.add("Deals 1 + 5% of your damage to fish");
		desc.add("The sands are reawakened while you hold this");
		return desc;
	}

	@Override
	public void onTick  (DungeonsPlayer d) 
	{
		int t = spawning.getOrDefault(d, 0);
		if (t < 200) spawning.put(d, ++t);
		else
		{
			spawning.put(d, 0);
			
			for (SpawnPosition s : spawns)
			{
				if (s.mob != null || s.position.distance(d.player.getLocation()) > 8) continue;
				DMobManager.spawn(RandomFunctions.get(mobs),s);
			}
		}
	}
	@Override
	public int onAttack(DungeonsPlayer d,DMob mob,int damage)
	{
		if (mob.type.tags.contains("spearfish")) 
		{
			int extra = (int) Math.floor(damage*0.05);
			return 1 + extra;
		}
		else return damage;
	}
}
