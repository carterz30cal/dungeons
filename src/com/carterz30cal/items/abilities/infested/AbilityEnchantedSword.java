package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityEnchantedSword extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Mana Burst");
		d.add("Right click to expell a directional");
		d.add("burst of magic energy which deals");
		d.add("3x your current mana.");
		d.add("This ability costs 100 mana to use.");
		d.add("Regular attacks deal 25% less regular damage");
		d.add("but deal 20% of your max mana as magic damage");
		return d;
	}
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (!d.useMana(100)) return false;
			new EnchantedSwordEnergyBurst(d);
			return true;
		}
		return false;
	}
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		dMob.damage(d.getMana() / 5, d, DamageType.MAGIC,false);
		return (int) (damage * 0.75d);
	} 

}

class EnchantedSwordEnergyBurst extends BukkitRunnable
{
	public DungeonsPlayer owner;
	public int damage;
	public Location l;
	private Location hitbox;
	public Vector direction;
	
	public int lifetime;
	
	
	private double cosX,cosY,sinX,sinY;
	public EnchantedSwordEnergyBurst (DungeonsPlayer o)
	{
		owner = o;
		damage = o.getMana() * 3;
		l = o.player.getEyeLocation().subtract(0,0.5,0);
		hitbox = l.clone();
		direction = o.player.getLocation().getDirection().normalize();
		runTaskTimer(Dungeons.instance,1,1);
		lifetime = 300;
		
		calculateDeviations(direction);
	}
	@Override
	public void run() 
	{
		lifetime--;
		if (lifetime == 0) cancel();
		for (Entity e : Dungeons.w.getNearbyEntities(hitbox, 1, 1, 1))
		{
			if (DMobManager.get(e) != null) 
			{
				DMobManager.get(e).damage(damage, owner, DamageType.MAGIC,false);
				cancel();
				return;
			}
		}

		int helix = 6;
		int speed = 15;
		for(int i = 0; i < helix; i++) {
            Location spawnLoc = l.clone().add(offsetFromCenter((lifetime*speed) + (i * (360/helix))));
            
            if (i % 3 == 0) spawnLoc.getWorld().spawnParticle(Particle.DRIP_WATER, spawnLoc, 1, 0, 0, 0, 0);
            else if (i % 3 == 1) spawnLoc.getWorld().spawnParticle(Particle.FLAME, spawnLoc, 1, 0, 0, 0, 0);
            else spawnLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, spawnLoc, 1, 0, 0, 0, 0);
        }
		hitbox = l.clone().add(offsetFromCenter((lifetime*speed) + ((helix/2) * (360/helix))));
		//ParticleFunctions.stationary(l, Particle.DRIP_WATER, 15);
	}
	 protected void calculateDeviations(Vector direction) {
	        Location directionalLoc = direction.toLocation(null).setDirection(direction);

	        double yaw = Math.toRadians(directionalLoc.getYaw());
	        double pitch = Math.toRadians(directionalLoc.getPitch()+90);

	        cosX = Math.cos(pitch);
	        sinX = Math.sin(pitch);
	        cosY = Math.cos(-yaw);
	        sinY = Math.sin(-yaw);
	    }
	 

	    protected Vector offsetFromCenter(int degrees) {
	    	double radius = 0.5;
	        double radians = Math.toRadians(degrees);
	        double x = Math.cos(radians)*radius;
	        double z = Math.sin(radians)*radius;

	        Vector v = new Vector(x, 300-lifetime, z);

	        rotateAroundAxisX(v, cosX, sinX);
	        rotateAroundAxisY(v, cosY, sinY);

	        return v;
	    }


	    private Vector rotateAroundAxisX(Vector v, double cos, double sin) {
	        double y = v.getY() * cos - v.getZ() * sin;
	        double z = v.getY() * sin + v.getZ() * cos;
	        return v.setY(y).setZ(z);
	    }

	    private Vector rotateAroundAxisY(Vector v, double cos, double sin) {
	        double x = v.getX() * cos + v.getZ() * sin;
	        double z = v.getX() * -sin + v.getZ() * cos;
	        return v.setX(x).setZ(z);
	    }
}
