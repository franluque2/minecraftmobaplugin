package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;

public class CustomMobListener implements Listener {

    private final Map<EntityType, CustomMobHandler> handlers;
    private DamageTracker damTracker;
    private Plugin plugin;
    public CustomMobListener(Map<EntityType, CustomMobHandler> handlers, DamageTracker damTracker, Plugin plugin) {
        this.handlers = handlers;
        this.damTracker=damTracker;
        this.plugin=plugin;

        this.plugin.getLogger().info(String.format("Handlers: %s", handlers.toString()));

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity)
        {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            String name1;
            String name2;

            if (event.getDamager() instanceof Player)
            {
                name1=event.getDamager().getName();
            }
            else
            {
                name1=event.getDamager().getUniqueId().toString();
            }

            if (event.getEntity() instanceof Player)
            {
                name2=event.getEntity().getName();
            }
            else
            {
                name2=event.getEntity().getUniqueId().toString();
            }
            
            Team team1=scoreboard.getEntryTeam(name1);
            Team team2=scoreboard.getEntryTeam(name2);

            if ((team1 != null) && ( team2 != null))
            {
                if ( team1.getName()==team2.getName())
                {
                    event.setCancelled(true);
                    return;    
                }    
            }
        }

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            damTracker.recordDamage((Player)event.getDamager());
            return;
        }

        if (event.getDamager() instanceof Player player && event.getEntity() instanceof LivingEntity entity) {
            CustomMobHandler handler = this.handlers.get(entity.getType());
            if (handler != null) {
                handler.recordDamage(entity, player);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        CustomMobHandler handler = this.handlers.get(entity.getType());
        if (handler != null) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            handler.handleDeath(entity);
        }

    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event)
    {
        Entity entity = event.getHitEntity();
        ProjectileSource launcher = event.getEntity().getShooter();

        if ((entity != null) && (launcher != null))
        {
            if ((entity instanceof LivingEntity) && (launcher instanceof LivingEntity) )
            {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                String name1;
                String name2;
    
                if (launcher instanceof Player)
                {
                    name1=((Player)launcher).getName();
                }
                else
                {
                    name1=((LivingEntity)launcher).getUniqueId().toString();
                }
    
                if (entity instanceof Player)
                {
                    name2=entity.getName();
                }
                else
                {
                    name2=entity.getUniqueId().toString();
                }
                
                Team team1=scoreboard.getEntryTeam(name1);
                Team team2=scoreboard.getEntryTeam(name2);
    
                if ((team1 != null) && ( team2 != null))
                {
                    if ( team1.getName()==team2.getName())
                    {
                        event.setCancelled(true);
                        return;    
                    }    
                }
    
            }
        }
    }
}
