package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
public class SpawnJungleMobCommand implements CommandExecutor {

    private final MinecraftMobaPlugin plugin;

    public SpawnJungleMobCommand(MinecraftMobaPlugin plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 5) {
            sender.sendMessage("Usage: /spawnjunglemob <mobType> <spawnX> <spawnY> <spawnZ> <radius>");
            return false;
        }

        String mobType = args[0].toLowerCase();
        double spawnX, spawnY, spawnZ;
        int radius;

        try {
            spawnX = Double.parseDouble(args[1]);
            spawnY = Double.parseDouble(args[2]);
            spawnZ = Double.parseDouble(args[3]);
            radius = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid coordinates or radius. Please provide valid numbers.");
            return false;
        }

        EntityType entityType = parseMobType(mobType);
        if (entityType == null) {
            sender.sendMessage("Invalid mob type. Valid types: vindicator.");
            return false;
        }
        Location spawnLocation = new Location(sender.getServer().getWorlds().get(0), spawnX, spawnY, spawnZ);

        Creature mob = (Creature) spawnLocation.getWorld().spawnEntity(spawnLocation, entityType, false);

        JungleMobHandler mobHandler = getMobHandler(entityType);
        mobHandler.initializeMob(mob, spawnLocation, radius);

        sender.sendMessage("Spawned " + mobType + " at " + spawnLocation + " with a home radius of " + radius);
        return true;
    }

    private EntityType parseMobType(String mobType) {
        switch (mobType) {
            case "vindicator":
                return EntityType.VINDICATOR;
            default:
                return null;
        }
    }

    private JungleMobHandler getMobHandler(EntityType entityType) {
        switch (entityType) {
            case VINDICATOR:
                return new JungleVindicatorHandler(plugin);
            default:
                return null;
        }
    }
}