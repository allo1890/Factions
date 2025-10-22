package fr.maxairfrance.factions.database;

import fr.maxairfrance.factions.Main;
import fr.maxairfrance.factions.gestionfactions.FPlayer;
import fr.maxairfrance.factions.gestionfactions.Role;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DbFPlayer {

    private final File playersFolder;

    public DbFPlayer() {
        this.playersFolder = new File(Main.getInstance().getDataFolder(), "players");
        if (!playersFolder.exists()) {
            playersFolder.mkdirs();
        }
    }

    public void save(FPlayer fPlayer) {
        File file = new File(playersFolder, fPlayer.getUuid().toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("uuid", fPlayer.getUuid().toString());
        config.set("name", fPlayer.getName());
        config.set("faction", fPlayer.getFactionId());
        config.set("role", fPlayer.getRole().name());
        config.set("power", fPlayer.getPower());
        config.set("maxPower", fPlayer.getMaxPower());

        try {
            config.save(file);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Erreur lors de la sauvegarde du joueur: " + fPlayer.getName());
            e.printStackTrace();
        }
    }

    public FPlayer load(UUID uuid) {
        File file = new File(playersFolder, uuid.toString() + ".yml");
        if (!file.exists()) {
            return null;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String name = config.getString("name", "Unknown");
        FPlayer fPlayer = new FPlayer(uuid, name);

        fPlayer.setFactionId(config.getString("faction", null));
        fPlayer.setRole(Role.fromString(config.getString("role", "RECRUE")));
        fPlayer.setPower(config.getInt("power", 10));
        fPlayer.setMaxPower(config.getInt("maxPower", 10));

        return fPlayer;
    }

    public List<FPlayer> loadAll() {
        List<FPlayer> players = new ArrayList<>();
        File[] files = playersFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (files != null) {
            for (File file : files) {
                try {
                    String uuidStr = file.getName().replace(".yml", "");
                    UUID uuid = UUID.fromString(uuidStr);
                    FPlayer fPlayer = load(uuid);
                    if (fPlayer != null) {
                        players.add(fPlayer);
                    }
                } catch (IllegalArgumentException e) {
                    Main.getInstance().getLogger().warning("Fichier joueur invalide: " + file.getName());
                }
            }
        }

        return players;
    }

    public void delete(UUID uuid) {
        File file = new File(playersFolder, uuid.toString() + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }
}
