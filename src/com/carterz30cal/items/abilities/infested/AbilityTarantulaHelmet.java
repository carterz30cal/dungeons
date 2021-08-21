package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.areas.InfestedHunter;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;

public class AbilityTarantulaHelmet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Sight of the Hunt");
		d.add("Deal +10% damage to tarantulas.");
		d.add("Take 20% less damage from them.");
		d.add("Your boss glows.");
		return d;
	}

	@Override
	public void onTick  (DungeonsPlayer d) 
	{
		DMob mob = InfestedHunter.active.get(d);
		if (mob != null)
		{
			Entity e = ((CraftEntity) mob.entities.get(0)).getHandle();
			e.setFlag(6, true);
			
			PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(e.getId(),e.getDataWatcher(),true);
			((CraftPlayer)d.player).getHandle().playerConnection.sendPacket(packet);
		}
	} 
	@Override
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{
		if (mob == null) return 1;
		if (mob.tags.contains("hunter")) return 0.8;
		return 1;
	} 
	@Override
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		if (dMob.type.tags.contains("hunter")) return (int) (damage * 1.66);
		return damage;
	}
}
