package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class CustomPlayerListener implements Listener {


    private DamageTracker damTrack;
    public CustomPlayerListener(DamageTracker damTrack) {
        this.damTrack=damTrack;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damagePlayer=(Player)event.getDamager();
            damTrack.recordDamage(damagePlayer);
        }
    }

}
