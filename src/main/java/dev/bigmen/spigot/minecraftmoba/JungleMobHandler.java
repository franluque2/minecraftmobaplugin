package dev.bigmen.spigot.minecraftmoba;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;
import org.bukkit.Location;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.goal.target.PathfinderNearestAttackableTarget;
import me.gamercoder215.mobchip.ai.goal.PathfinderMoveTowardsRestriction;
import me.gamercoder215.mobchip.ai.goal.PathfinderRandomStrollLand;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;

public class JungleMobHandler extends CustomMobHandler {

    private Mob mob;

    public JungleMobHandler(Plugin plugin) {
        super(plugin);
    }

    
    protected ItemStack[] getDrops() {
        return new ItemStack[]{};
    }


    public void initializeMob(Creature mob, Location restrictionArea, int radius) {
        this.mob = mob;

        initializeAttrs(mob);
        EntityBrain brain = BukkitBrain.getBrain(mob);
        EntityAI target=brain.getTargetAI();

        target.clear();

        brain.getScheduleManager().clear();

        PathfinderNearestAttackableTarget<Player> nearestAttackPlayer=new PathfinderNearestAttackableTarget<Player>(this.mob, Player.class,3,true,true);
        target.put(nearestAttackPlayer,2);

        brain.setRestrictionArea(restrictionArea, radius);
        PathfinderMoveTowardsRestriction goBackHome = new PathfinderMoveTowardsRestriction(mob, 1.5);
        target.put(goBackHome,1);

        PathfinderRandomStrollLand randomStroll= new PathfinderRandomStrollLand(mob);
        target.put(randomStroll,0);

    }
}
