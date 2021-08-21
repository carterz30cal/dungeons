package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.items.magic.ProjectileParticle;
import com.carterz30cal.player.DungeonsPlayer;

public class ModSpellConcentration extends AbsAbility
{

	
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(modifier + "Spell Concentration");
		d.add("Your spells deal 10 more damage");
		d.add("but cost an extra 6 mana and");
		d.add("move 10% slower");
		return d;
	}
	@Override
	public void onMagic(DungeonsPlayer d,ProjectileParticle p) 
	{
		p.damage = p.damage + 10;
		p.speed = p.speed / 1.1;
	}
	@Override
	public int magicCost(ItemMeta wand) 
	{
		return 6;
	} 

}
