package dev.bigmen.spigot.minecraftmoba;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Scoreboard;


import java.util.ArrayList;
import java.util.List;

public class SpawnLaneMobCommand implements CommandExecutor {

    private final MinecraftMobaPlugin plugin;
    protected DamageTracker damTrack;

    public SpawnLaneMobCommand(MinecraftMobaPlugin plugin, DamageTracker damTrack) {
        this.plugin = plugin;
        this.damTrack = damTrack;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 8) {
            sender.sendMessage("Usage: /spawnlanemob <mobType> <teamName> <spawnX> <spawnY> <spawnZ> <waypointX1> <waypointY1> <waypointZ1> ...");
            return false;
        }

        String mobType = args[0].toLowerCase();
        String teamName = args[1];

        EntityType entityType = parseMobType(mobType);

        if (entityType == null) {
            sender.sendMessage("Invalid mob type. Valid types: zombie, skeleton.");
            return false;
        }

        double spawnX = Double.parseDouble(args[2]);
        double spawnY = Double.parseDouble(args[3]);
        double spawnZ = Double.parseDouble(args[4]);

        Location spawnLocation = new Location(sender.getServer().getWorlds().get(0), spawnX, spawnY, spawnZ);

        List<Location> waypoints = new ArrayList<>();
        for (int i = 5; i < args.length; i += 3) {
            double waypointX = Double.parseDouble(args[i]);
            double waypointY = Double.parseDouble(args[i + 1]);
            double waypointZ = Double.parseDouble(args[i + 2]);

            waypoints.add(new Location(spawnLocation.getWorld(), waypointX, waypointY, waypointZ));
        }

        Creature mob = (Creature) spawnLocation.getWorld().spawnEntity(spawnLocation, entityType, false);
        LaneMobHandler mobHandler = getMobHandler(entityType);

        Team mobTeam = getOrCreateTeam(teamName);
        if (mobTeam == null) {
            sender.sendMessage("Failed to create or retrieve the team.");
            return false;
        }

        mobTeam.addEntry(mob.getUniqueId().toString());

        if (mobHandler != null) {
            mobHandler.initializeMob(mob, mobTeam, damTrack);
            mobHandler.setWaypoints(waypoints, mob);
            mob.addScoreboardTag(String.format("teamName: %s", mobTeam));
        }

        sender.sendMessage("Spawned " + mobType + " at " + spawnLocation + " with waypoints.");

        return true;
    }

    private EntityType parseMobType(String mobType) {
        switch (mobType) {
            case "zombie":
                return EntityType.ZOMBIE;
            case "skeleton":
                return EntityType.SKELETON;
            default:
                return null;
        }
    }

    private LaneMobHandler getMobHandler(EntityType entityType) {
        switch (entityType) {
            case ZOMBIE:
                return new LaneZombieHandler(plugin);
            case SKELETON:
                return new LaneSkeletonHandler(plugin);
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