package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.entity.EntityType;
public class SpawnTowerMobCommand implements CommandExecutor {

    private final MinecraftMobaPlugin plugin;
    protected DamageTracker damTrack;

    public SpawnTowerMobCommand(MinecraftMobaPlugin plugin, DamageTracker damTrack) {
        this.plugin = plugin;
        this.damTrack = damTrack;

    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 6) {
            sender.sendMessage("Usage: /spawntowermob <mobType> <team> <spawnX> <spawnY> <spawnZ> <radius>");
            return false;
        }

        String mobType = args[0].toLowerCase();
        String teamName = args[1];

        double spawnX, spawnY, spawnZ;
        int radius;

        try {
            spawnX = Double.parseDouble(args[2]);
            spawnY = Double.parseDouble(args[3]);
            spawnZ = Double.parseDouble(args[4]);
            radius = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid coordinates or radius. Please provide valid numbers.");
            return false;
        }

        EntityType entityType = parseMobType(mobType);
        if (entityType == null) {
            sender.sendMessage("Invalid mob type. Valid types: irongolem.");
            return false;
        }
        Location spawnLocation = new Location(sender.getServer().getWorlds().get(0), spawnX, spawnY, spawnZ);

        Creature mob = (Creature) spawnLocation.getWorld().spawnEntity(spawnLocation, entityType, false);

        TowerMobHandler mobHandler = getMobHandler(entityType);

        Team mobTeam = getOrCreateTeam(teamName);
        if (mobTeam == null) {
            sender.sendMessage("Failed to create or retrieve the team.");
            return false;
        }

        mobTeam.addEntry(mob.getUniqueId().toString());

        mobHandler.initializeMob(mob, spawnLocation,mobTeam,this.damTrack, radius);

        sender.sendMessage("Spawned " + mobType + " at " + spawnLocation + " with a home radius of " + radius);
        return true;
    }

    private EntityType parseMobType(String mobType) {
        switch (mobType) {
            case "irongolem":
                return EntityType.IRON_GOLEM;
            default:
                return null;
        }
    }

    private TowerMobHandler getMobHandler(EntityType entityType) {
        switch (entityType) {
            case IRON_GOLEM:
                return new TowerIronGolemHandler(plugin);
            default:
                return null;
        }
    }

        private Team getOrCreateTeam(String teamName) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setAllowFriendlyFire(false);

        }
        return team;
    }

}