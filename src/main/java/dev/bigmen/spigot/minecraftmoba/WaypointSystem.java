package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;

import me.gamercoder215.mobchip.ai.controller.EntityController;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;


public class WaypointSystem {

    private Mob mob; 
    private List<Location> waypoints; 
    private EntityController controller;
    private int currentWaypointIndex = 0;

    public WaypointSystem(Mob mob, List<Location> waypoints) {
        this.mob = mob;
        this.waypoints = waypoints;
        this.controller = BukkitBrain.getBrain(mob).getController();
    }

    public void startMovingThroughWaypoints() {
        moveToNextWaypoint();
    }

    private void moveToNextWaypoint() {
        if (currentWaypointIndex >= waypoints.size()) {
            return;
        }

        Location targetWaypoint = waypoints.get(currentWaypointIndex);
        
        moveToWaypoint(targetWaypoint);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (mob.getLocation().distance(targetWaypoint) <= 3) {
                    currentWaypointIndex++;
                    moveToNextWaypoint();
                    cancel();
                } else {
                    moveToNextWaypoint();
                    cancel();
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("MinecraftMoba"), 60L);
    }

    private void moveToWaypoint(Location waypoint) {
        controller.moveTo(waypoint);
    }

}
