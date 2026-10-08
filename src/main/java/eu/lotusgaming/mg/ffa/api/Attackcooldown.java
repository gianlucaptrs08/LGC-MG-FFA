package eu.lotusgaming.mg.ffa.api;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class Attackcooldown {

 public static final float VANILLA_ATTACK_SPEED = 4.0f;
	 
	 public static final float MAX_ATTACK_SPEED = 1024.0f;
	
	 public static float attackCooldown = MAX_ATTACK_SPEED;

	  public static void setAttackCooldown(Player p, float amount) {
	        AttributeInstance i = p.getAttribute(Attribute.ATTACK_SPEED);
	        if (i != null) {
	            i.setBaseValue(amount);
	        } else {
	            System.err.println("Der Cooldown konnte nicht gesetzt werden für: " + p.getDisplayName());
	        }
	    }
	
}
