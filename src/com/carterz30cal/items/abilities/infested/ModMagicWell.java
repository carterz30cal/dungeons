package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.items.magic.ProjectileParticle;
import com.carterz30cal.player.DungeonsPlayer;

public class ModMagicWell extends AbsAbility
{

	
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(modifier + "Well of Magic");
		d.add("Deal one more damage for every 4");
		d.add("maximum mana you have.");
		d.add("Spells cost 9 more mana.");
		d.add("They also pierce two more enemies.");
		return d;
	}
	@Override
	public void onMagic(DungeonsPlayer d,ProjectileParticle p) 
	{
		p.damage += (int)((double)d.stats.mana / 4d);
		p.pierces += 2;
	}
	@Override
	public int magicCost(ItemMeta wand) 
	{
		return 9;
	} 

}
