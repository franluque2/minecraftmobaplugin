package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;


public class LaneSkeletonHandler extends LaneMobHandler {

    public LaneSkeletonHandler(JavaPlugin plugin) {
        super(plugin);
    }

    protected ItemStack[] getDrops() {
        return new ItemStack[]{new ItemStack(Material.BONE, 1)};
    }

    @Override
    protected int getExperience() {
        return 8;
    }

    @Override
    public void initializeMob(Creature mob, Team team, DamageTracker damTrack) {
        super.initializeMob(mob, team, damTrack);
        ItemStack helmet=new ItemStack(Material.LEATHER_HELMET);
        ColorableArmorMeta helmetmeta=(ColorableArmorMeta)helmet.getItemMeta();
        helmetmeta.setColor(TranslateColor.translateChatColorToColor(team.getColor()));
        helmet.getItemMeta().setUnbreakable(true);
        helmet.setItemMeta(helmetmeta);
        mob.getEquipment().setItem(EquipmentSlot.HEAD, helmet, true);
    
        ItemStack bow=new ItemStack(Material.BOW);
        mob.getEquipment().setItem(EquipmentSlot.HAND, bow, true);

        mob.setSilent(true);
        mob.setRemoveWhenFarAway(false);

    }

}
