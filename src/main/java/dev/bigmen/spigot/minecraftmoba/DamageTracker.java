package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DamageTracker{

    private final Map<String, Long> damageHistory = new HashMap<>();

    public void recordDamage(Player player) {
        long currentTime = System.currentTimeMillis();
        damageHistory.put(player.getUniqueId().toString(), currentTime);
    }

    public boolean hasDamagedRecently(LivingEntity player) {
        if (!Player.class.isInstance(player)){
            return false;
        }
        long currentTime = System.currentTimeMillis();
        Long lastDamageTime = damageHistory.get(player.getUniqueId().toString());
        if (lastDamageTime == null || currentTime - lastDamageTime > 5000) { 
            return false;
        }
        return true;
    }

    public void cleanupOldRecords() {
        long currentTime = System.currentTimeMillis();
        damageHistory.entrySet().removeIf(entry -> currentTime - entry.getValue() > 5000); 
    }
}
