package fr.maxairfrance.factions.gestionfactions;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.Objects;

public class FLocation {

    private final String worldName;
    private final int x;
    private final int z;

    public FLocation(String worldName, int x, int z) {
        this.worldName = worldName;
        this.x = x;
        this.z = z;
    }

    public FLocation(Location location) {
        this(location.getWorld().getName(),
                location.getChunk().getX(),
                location.getChunk().getZ());
    }

    public FLocation(Chunk chunk) {
        this(chunk.getWorld().getName(),
                chunk.getX(),
                chunk.getZ());
    }

    public String getWorldName() {
        return worldName;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getCoordString() {
        return x + "," + z;
    }

    public String toString() {
        return worldName + ";" + x + ";" + z;
    }

    public static FLocation fromString(String str) {
        String[] parts = str.split(";");
        if (parts.length != 3) {
            return null;
        }

        try {
            String worldName = parts[0];
            int x = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            return new FLocation(worldName, x, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FLocation fLocation = (FLocation) o;
        return x == fLocation.x &&
                z == fLocation.z &&
                Objects.equals(worldName, fLocation.worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldName, x, z);
    }
}
