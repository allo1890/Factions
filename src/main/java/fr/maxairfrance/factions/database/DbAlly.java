package fr.maxairfrance.factions.database;

import fr.maxairfrance.factions.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DbAlly {

    private final File alliesFile;

    public DbAlly() {
        this.alliesFile = new File(Main.getInstance().getDataFolder(), "allies.yml");
        if (!alliesFile.exists()) {
            try {
                alliesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(String factionId, Set<String> allies) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(alliesFile);
        config.set(factionId, new ArrayList<>(allies));

        try {
            config.save(alliesFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Erreur lors de la sauvegarde des alliances");
            e.printStackTrace();
        }
    }

    public Set<String> load(String factionId) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(alliesFile);
        List<String> allies = config.getStringList(factionId);
        return new HashSet<>(allies);
    }

    public void delete(String factionId) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(alliesFile);
        config.set(factionId, null);

        try {
            config.save(alliesFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Erreur lors de la suppression des alliances");
            e.printStackTrace();
        }
    }
}
