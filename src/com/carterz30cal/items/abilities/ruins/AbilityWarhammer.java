package com.carterz30cal.items.abilities.ruins;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.BoundingBox;


public class AbilityWarhammer extends AbsAbility
{
	private static BoundingBox box = new BoundingBox(new Location(Dungeons.w,-200,0,23800),new Location(Dungeons.w,200,255,24200));
	public static List<DMob> hit = new ArrayList<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Punishing Strike");
		d.add("First hit deals 1.75x damage.");
		d.add("If in the abyss, the attack deals 2.5x damage and EXPLODES!");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		if (!hit.contains(dMob))
		{
			hit.add(dMob);
			d.player.playSound(d.player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
			if (box.isInside(d.player.getLocation()))
			{
				if (d.stats.settype != null && d.stats.settype.set_ability.equals("nightlord")) Dungeons.w.createExplosion(dMob.entities.get(0).getLocation(), 5, false);
				else Dungeons.w.createExplosion(dMob.entities.get(0).getLocation(), 2, false);
				return (int) (damage * 2.5);
			}
			else return (int) (damage * 1.75);
		}
		else return damage;
	}
	
	public void onKill2 (DungeonsPlayer d,DMob mob) {hit.remove(mob);} 

}
