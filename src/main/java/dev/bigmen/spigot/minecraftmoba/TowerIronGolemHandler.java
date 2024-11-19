package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;

public class TowerIronGolemHandler extends TowerMobHandler {

    public TowerIronGolemHandler(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected ItemStack[] getDrops() {
        return new ItemStack[]{new ItemStack(Material.IRON_BARS, 4)};
    }

    @Override
    protected int getExperience() {
        return 20;
    }

    @Override
    protected void initializeAttrs(LivingEntity mob)
    {
        mob.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(3);
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
        mob.setHealth(500);
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
        mob.setGlowing(true);
        mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);

        mob.setRemoveWhenFarAway(false);
    }
}
