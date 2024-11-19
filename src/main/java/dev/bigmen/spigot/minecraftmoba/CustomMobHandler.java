package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class CustomMobHandler {

    protected final Plugin plugin;
    private final Map<UUID, Long> lastDamageTimes = new HashMap<>();

    public CustomMobHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public void recordDamage(Entity entity, Player damager) {
        lastDamageTimes.put(entity.getUniqueId(), System.currentTimeMillis());
    }

    protected boolean isEligibleForDrops(Entity entity) {
        Long lastDamageTime = lastDamageTimes.get(entity.getUniqueId());
        return lastDamageTime != null && (System.currentTimeMillis() - lastDamageTime) <= 2000;
    }

    public void cleanup(Entity entity) {
        lastDamageTimes.remove(entity.getUniqueId());
    }

    protected abstract ItemStack[] getDrops();

    public void handleDeath(LivingEntity entity) {
        if (!isEligibleForDrops(entity)) {
            cleanup(entity);
            return;
        }

        for (ItemStack drop : getDrops()) {
            var droppedItem = entity.getWorld().dropItemNaturally(entity.getLocation(), drop);
            scheduleDropRemoval(droppedItem);
        }

        entity.getWorld().spawn(entity.getLocation(), org.bukkit.entity.ExperienceOrb.class).setExperience(getExperience());

        cleanup(entity);
    }

    protected int getExperience() {
        return 0; 
    }

    protected void initializeAttrs(LivingEntity mob)
    {
        return;
    }

    private void scheduleDropRemoval(Entity drop) {
        new BukkitRunnable() {
            @Override
            public void run() {
                drop.remove();
            }
        }.runTaskLater(plugin, 100L);
    }
}
