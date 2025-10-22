package fr.maxairfrance.factions.database;

import fr.maxairfrance.factions.Main;
import fr.maxairfrance.factions.gestionfactions.Faction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DbFaction {

    private final File factionsFolder;

    public DbFaction() {
        this.factionsFolder = new File(Main.getInstance().getDataFolder(), "factions");
        if (!factionsFolder.exists()) {
            factionsFolder.mkdirs();
        }
    }

    public void save(Faction faction) {
        File file = new File(factionsFolder, faction.getId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("id", faction.getId());
        config.set("name", faction.getName());
        config.set("tag", faction.getTag());
        config.set("description", faction.getDescription());
        config.set("leader", faction.getLeader().toString());

        List<String> membersList = faction.getMembers().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());
        config.set("members", membersList);

        List<String> officersList = faction.getOfficers().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());
        config.set("officers", officersList);

        config.set("allies", new ArrayList<>(faction.getAllies()));
        config.set("enemies", new ArrayList<>(faction.getEnemies()));

        config.set("power", faction.getPower());
        config.set("maxPower", faction.getMaxPower());
        config.set("open", faction.isOpen());
        config.set("createdDate", faction.getCreatedDate());

        try {
            config.save(file);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Erreur lors de la sauvegarde de la faction: " + faction.getName());
            e.printStackTrace();
        }
    }

    public Faction load(String id) {
        File file = new File(factionsFolder, id + ".yml");
        if (!file.exists()) {
            return null;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String name = config.getString("name");
        UUID leader = UUID.fromString(config.getString("leader"));

        Faction faction = new Faction(id, name, leader);

        faction.setTag(config.getString("tag", name));
        faction.setDescription(config.getString("description", "Une faction créée avec passion"));

        List<String> membersList = config.getStringList("members");
        for (String uuidStr : membersList) {
            try {
                UUID uuid = UUID.fromString(uuidStr);
                if (!uuid.equals(leader)) {
                    faction.addMember(uuid);
                }
            } catch (IllegalArgumentException e) {
                Main.getInstance().getLogger().warning("UUID de membre invalide: " + uuidStr);
            }
        }

        List<String> officersList = config.getStringList("officers");
        for (String uuidStr : officersList) {
            try {
                UUID uuid = UUID.fromString(uuidStr);
                faction.addOfficer(uuid);
            } catch (IllegalArgumentException e) {
                Main.getInstance().getLogger().warning("UUID d'officier invalide: " + uuidStr);
            }
        }

        List<String> allies = config.getStringList("allies");
        for (String ally : allies) {
            faction.addAlly(ally);
        }

        List<String> enemies = config.getStringList("enemies");
        for (String enemy : enemies) {
            faction.addEnemy(enemy);
        }

        faction.setOpen(config.getBoolean("open", false));
        faction.setCreatedDate(config.getLong("createdDate", System.currentTimeMillis()));

        faction.updatePower();

        return faction;
    }

    public List<Faction> loadAll() {
        List<Faction> factions = new ArrayList<>();
        File[] files = factionsFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (files != null) {
            for (File file : files) {
                String id = file.getName().replace(".yml", "");
                Faction faction = load(id);
                if (faction != null) {
                    factions.add(faction);
                }
            }
        }

        return factions;
    }

    public void delete(String id) {
        File file = new File(factionsFolder, id + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }
}
