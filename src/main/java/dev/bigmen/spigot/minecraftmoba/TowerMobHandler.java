package dev.bigmen.spigot.minecraftmoba;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.*;

import java.util.function.Predicate;

import org.bukkit.Location;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.goal.PathfinderMoveTowardsRestriction;
import me.gamercoder215.mobchip.ai.goal.PathfinderRandomStrollLand;
import me.gamercoder215.mobchip.ai.goal.target.PathfinderNearestAttackableTarget;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.scoreboard.Team;


public class TowerMobHandler extends CustomMobHandler {


    public TowerMobHandler(Plugin plugin) {
        super(plugin);
    }

    
    protected ItemStack[] getDrops() {
        return new ItemStack[]{};
    }


    public void initializeMob(Creature mob, Location restrictionArea, Team team,DamageTracker damTrack, int radius) {
        Predicate <LivingEntity> towerTest= i -> i.hasMetadata("is_tower") && i.getMetadata("is_tower").size()>0 && i.getMetadata("is_tower").get(0).asBoolean()==true;
        Predicate <LivingEntity> sameTeamTest=Predicate.not(towerTest).and( i -> (!(i.getType()==EntityType.VILLAGER) && !team.hasEntry(i.getUniqueId().toString())));
        Predicate <LivingEntity> recentlyAttacked= i -> ((!team.hasEntry(i.getUniqueId().toString())) && (damTrack.hasDamagedRecently(i)));

        initializeAttrs(mob);
        EntityBrain brain = BukkitBrain.getBrain(mob);
        EntityAI target=brain.getTargetAI();
        target.clear();

        mob.setCollidable(false);
        FixedMetadataValue tower_meta_value= new FixedMetadataValue(this.plugin, true);
        mob.setMetadata("is_tower", tower_meta_value);

        PathfinderNearestAttackableTarget<Player> nearestGuiltyAttackPlayer=new PathfinderNearestAttackableTarget<Player>(mob, Player.class,10,true,true);
        nearestGuiltyAttackPlayer.setCondition(recentlyAttacked);
        target.put(nearestGuiltyAttackPlayer,4);

        PathfinderNearestAttackableTarget<Mob> nearestAttack=new PathfinderNearestAttackableTarget<Mob>(mob, Mob.class,10,true,true);
        nearestAttack.setCondition(sameTeamTest);
        target.put(nearestAttack,3);

        PathfinderNearestAttackableTarget<Player> nearestAttackPlayer=new PathfinderNearestAttackableTarget<Player>(mob, Player.class,10,true,true);
        nearestAttackPlayer.setCondition(sameTeamTest);
        target.put(nearestAttackPlayer,2);

        brain.getScheduleManager().clear();

        brain.setRestrictionArea(restrictionArea, radius);

        PathfinderMoveTowardsRestriction goBackHome = new PathfinderMoveTowardsRestriction(mob, 1.5);
        target.put(goBackHome,1);


        PathfinderRandomStrollLand randomStroll= new PathfinderRandomStrollLand(mob);
        target.put(randomStroll,0);
    }
}
