package com.carterz30cal.mobs.packet;

import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import com.carterz30cal.dungeons.Dungeons;

import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EntityZombie;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.World;

public class EntitySoul extends EntityZombie implements CustomEntity
{
	public static net.minecraft.server.v1_16_R3.Entity spawn()
	{
		return new EntitySoul(EntityTypes.ZOMBIE,((CraftWorld)Dungeons.w).getHandle());
	}

	public EntitySoul(EntityTypes<? extends EntityZombie> entitytypes, World world) {
		super(entitytypes, world);
		
		//getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(1);
		// TODO Auto-generated constructor stub
		
	}
	
	@Override// <- not necessary but I like putting it here
	public void initPathfinder()
	{
		
		goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1d,true));
        
        goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	}
}
