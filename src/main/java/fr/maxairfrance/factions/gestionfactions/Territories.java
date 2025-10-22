package fr.maxairfrance.factions.gestionfactions;

import fr.maxairfrance.factions.database.DbTerritories;
import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;

public class Territories {

    private static Territories instance;
    private final Map<FLocation, String> territories;
    private final DbTerritories db;

    public Territories() {
        instance = this;
        this.territories = new HashMap<>();
        this.db = new DbTerritories();
    }

    public static Territories getInstance() {
        return instance;
    }

    public void claim(FLocation location, String factionId) {
        territories.put(location, factionId);
        db.save(location, factionId);
    }

    public void unclaim(FLocation location) {
        territories.remove(location);
        db.delete(location);
    }

    public String getFactionAt(FLocation location) {
        return territories.get(location);
    }

    public String getFactionAt(Location location) {
        return getFactionAt(new FLocation(location));
    }

    public boolean isClaimed(FLocation location) {
        return territories.containsKey(location);
    }

    public boolean isClaimedBy(FLocation location, String factionId) {
        String owner = territories.get(location);
        return owner != null && owner.equals(factionId);
    }

    public int getClaimCount(String factionId) {
        return (int) territories.values().stream()
                .filter(id -> id.equals(factionId))
                .count();
    }

    public void loadAll() {
        Map<FLocation, String> loaded = db.loadAll();
        territories.putAll(loaded);
    }

    public void saveAll() {
        for (Map.Entry<FLocation, String> entry : territories.entrySet()) {
            db.save(entry.getKey(), entry.getValue());
        }
    }
}
