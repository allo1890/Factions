package fr.maxairfrance.factions.database;

import fr.maxairfrance.factions.Main;
import fr.maxairfrance.factions.gestionfactions.FLocation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DbSpecialTerritories {

    private final File specialFile;

    public DbSpecialTerritories() {
        this.specialFile = new File(Main.getInstance().getDataFolder(), "special-territories.yml");
        if (!specialFile.exists()) {
            try {
                specialFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveSpawn(Set<FLocation> locations) {
        save("spawn", locations);
    }

    public Set<FLocation> loadSpawn() {
        return load("spawn");
    }

    public void saveSafezone(Set<FLocation> locations) {
        save("safezone", locations);
    }

    public Set<FLocation> loadSafezone() {
        return load("safezone");
    }

    public void saveWarzone(Set<FLocation> locations) {
        save("warzone", locations);
    }

    public Set<FLocation> loadWarzone() {
        return load("warzone");
    }

    private void save(String type, Set<FLocation> locations) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(specialFile);

        List<String> locationStrings = locations.stream()
                .map(FLocation::toString)
                .collect(java.util.stream.Collectors.toList());

        config.set(type, locationStrings);

        try {
            config.save(specialFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Erreur lors de la sauvegarde des territoires spéciaux");
            e.printStackTrace();
        }
    }

    private Set<FLocation> load(String type) {
        Set<FLocation> locations = new HashSet<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(specialFile);

        List<String> locationStrings = config.getStringList(type);
        for (String locStr : locationStrings) {
            FLocation location = FLocation.fromString(locStr);
            if (location != null) {
                locations.add(location);
            }
        }

        return locations;
    }
}
