package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;

public class JungleVindicatorHandler extends JungleMobHandler {

    public JungleVindicatorHandler(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected ItemStack[] getDrops() {
        return new ItemStack[]{new ItemStack(Material.ARROW, 1)};
    }

    @Override
    protected int getExperience() {
        return 8;
    }
}
