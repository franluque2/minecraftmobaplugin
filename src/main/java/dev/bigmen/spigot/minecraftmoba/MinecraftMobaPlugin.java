package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.EntityType;

import java.util.Map;

public class MinecraftMobaPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MinecraftMobaPlugin enabled!");

        LaneMobHandler laneZombieHandler = new LaneZombieHandler(this);
        LaneMobHandler laneSkeletonHandler = new LaneSkeletonHandler(this);
        CustomMobHandler jingleVindicatorHandler = new JungleVindicatorHandler(this);
        CustomMobHandler ironGolemHandler = new TowerIronGolemHandler(this);

        DamageTracker damageTracker=new DamageTracker();


        this.getCommand("spawnlanemob").setExecutor(new SpawnLaneMobCommand(this, damageTracker));
        this.getCommand("spawnjunglemob").setExecutor(new SpawnJungleMobCommand(this));
        this.getCommand("spawntowermob").setExecutor(new SpawnTowerMobCommand(this, damageTracker));

        Map<EntityType, CustomMobHandler> handlers = Map.of(
            EntityType.ZOMBIE, laneZombieHandler,
            EntityType.SKELETON, laneSkeletonHandler,
            EntityType.VINDICATOR, jingleVindicatorHandler,
            EntityType.IRON_GOLEM,ironGolemHandler

        );

        getServer().getPluginManager().registerEvents(new CustomMobListener(handlers, damageTracker, this), this);

    }


    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }

}
