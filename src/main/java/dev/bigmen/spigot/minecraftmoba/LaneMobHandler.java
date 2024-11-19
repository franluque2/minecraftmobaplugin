package dev.bigmen.spigot.minecraftmoba;

import java.util.List;
import java.util.function.Predicate; 
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.goal.target.PathfinderNearestAttackableTarget;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;


public class LaneMobHandler extends CustomMobHandler {

    public LaneMobHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public void setWaypoints(List<Location> waypoints, Mob mob) {
        WaypointSystem waypointsystem = new WaypointSystem(mob, waypoints);
        waypointsystem.startMovingThroughWaypoints();
    }

    protected ItemStack[] getDrops() {
        return new ItemStack[]{};
    }


    public void initializeMob(Creature mob, Team team, DamageTracker damTrack) {
        Predicate <LivingEntity> towerTest= i -> i.hasMetadata("is_tower") && i.getMetadata("is_tower").size()>0 && i.getMetadata("is_tower").get(0).asBoolean()==true;        
        Predicate <LivingEntity> sameTeamTest=Predicate.not(towerTest).and( i -> (!(i.getType()==EntityType.VILLAGER) && !team.hasEntry(i.getUniqueId().toString())));
        Predicate <LivingEntity> recentlyAttacked= i -> ((!team.hasEntry(i.getUniqueId().toString())) && (damTrack.hasDamagedRecently(i)));
        initializeAttrs(mob);
        EntityBrain brain = BukkitBrain.getBrain(mob);
        EntityAI target=brain.getTargetAI();

        target.clear();

        PathfinderNearestAttackableTarget<Player> nearestGuiltyAttackPlayer=new PathfinderNearestAttackableTarget<Player>(mob, Player.class,10,true,true);
        nearestGuiltyAttackPlayer.setCondition(recentlyAttacked);
        target.put(nearestGuiltyAttackPlayer,3);

        PathfinderNearestAttackableTarget<Mob> nearestAttack=new PathfinderNearestAttackableTarget<Mob>(mob, Mob.class,10,true,true);
        nearestAttack.setCondition(sameTeamTest);
        target.put(nearestAttack,2);

        PathfinderNearestAttackableTarget<Player> nearestAttackPlayer=new PathfinderNearestAttackableTarget<Player>(mob, Player.class,10,true,true);
        nearestAttackPlayer.setCondition(sameTeamTest);
        target.put(nearestAttackPlayer,1);


        PathfinderNearestAttackableTarget<Mob> nearestTower=new PathfinderNearestAttackableTarget<Mob>(mob, Mob.class,10,true,true);
        nearestAttack.setCondition(towerTest);
        target.put(nearestTower,0);

        brain.getScheduleManager().clear();

    }

}
