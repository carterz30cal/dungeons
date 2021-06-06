package com.carterz30cal.mobs.packet;

import com.carterz30cal.dungeons.Dungeons;

import net.minecraft.server.v1_16_R3.EntityCow;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R3.World;

import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

public class EntityBull extends EntityCow implements CustomEntity
{
	
	public static net.minecraft.server.v1_16_R3.Entity spawn()
	{
		return new EntityBull(EntityTypes.COW,((CraftWorld)Dungeons.w).getHandle());
	}

	public EntityBull(EntityTypes<? extends EntityCow> entitytypes, World world) {
		super(entitytypes, world);
		
		getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE);
		//getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(1);
		// TODO Auto-generated constructor stub
		
	}
	
	protected void initAttributes() {

        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(4.0D);
    }
	
	/*
	@Override
	public boolean attackEntity(Entity entity)
	{
		System.out.print("ok");
		if (entity instanceof EntityPlayer)
		{
			
			EntityPlayer player = (EntityPlayer)entity;
			DungeonsPlayer d = DungeonsPlayerManager.i.get((Player)player.getBukkitEntity());
			
			Bukkit.getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent((org.bukkit.entity.Entity)entity.getBukkitEntity(),d.player,DamageCause.ENTITY_ATTACK,1));
		}
		return true;
	}
	*/
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override// <- not necessary but I like putting it here
	public void initPathfinder()
	{
		
		goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1d,true));
        goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        
        targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
        
	}
	
	
	


}
