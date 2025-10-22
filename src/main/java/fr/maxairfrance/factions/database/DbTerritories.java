package fr.maxairfrance.factions.database;

import fr.maxairfrance.factions.Main;
import fr.maxairfrance.factions.gestionfactions.FLocation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DbTerritories {

    private final File territoriesFile;

    public DbTerritories() {
        this.territoriesFile = new File(Main.getInstance().getDataFolder(), "territories.yml");
        if (!territoriesFile.exists()) {
            try {
                territoriesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(FLocation location, String factionId) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(territoriesFile);
        config.set(location.toString(), factionId);

        try {
            config.save(territoriesFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Erreur lors de la sauvegarde des territoires");
            e.printStackTrace();
        }
    }

    public void delete(FLocation location) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(territoriesFile);
        config.set(location.toString(), null);

        try {
            config.save(territoriesFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Erreur lors de la suppression d'un territoire");
            e.printStackTrace();
        }
    }

    public Map<FLocation, String> loadAll() {
        Map<FLocation, String> territories = new HashMap<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(territoriesFile);

        for (String key : config.getKeys(false)) {
            FLocation location = FLocation.fromString(key);
            String factionId = config.getString(key);

            if (location != null && factionId != null) {
                territories.put(location, factionId);
            }
        }

        return territories;
    }
}
