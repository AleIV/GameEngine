package me.aleiv.gameengine.globalUtilities.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class Region {

    private final int x1;
    private final int y1;
    private final int z1;
    private final int x2;
    private final int y2;
    private final int z2;
    private final String worldName;

    public Region(int x1, int y1, int z1, int x2, int y2, int z2, String worldName) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.worldName = worldName;
    }

    public Region(Location loc1, Location loc2) {
        this.x1 = loc1.getBlockX();
        this.y1 = loc1.getBlockY();
        this.z1 = loc1.getBlockZ();
        this.x2 = loc2.getBlockX();
        this.y2 = loc2.getBlockY();
        this.z2 = loc2.getBlockZ();
        this.worldName = loc1.getWorld().getName();
    }

    public boolean contains(Entity e) {
        return this.contains(e.getLocation());
    }

    public boolean contains(Location loc) {
        return loc.getBlockX() >= x1 && loc.getBlockX() <= x2 &&
                loc.getBlockY() >= y1 && loc.getBlockY() <= y2 &&
                loc.getBlockZ() >= z1 && loc.getBlockZ() <= z2 &&
                loc.getWorld().getName().equals(worldName);
    }

    public String getWorldName() {
        return worldName;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public Location getCenter() {
        return new Location(
                this.getWorld(),
                x1 + ((x2 - x1) / 2),
                y1 + ((y2 - y1) / 2),
                z1 + ((z2 - z1) / 2)).add(0.5, 0, 0.5);
    }

    public Location getLoc1() {
        return new Location(getWorld(), x1, y1, z1);
    }

    public Location getLoc2() {
        return new Location(getWorld(), x2, y2, z2);
    }

}
