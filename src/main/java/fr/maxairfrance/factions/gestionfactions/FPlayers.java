package fr.maxairfrance.factions.gestionfactions;

import fr.maxairfrance.factions.Main;
import fr.maxairfrance.factions.database.DbFPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class FPlayers {

    private static FPlayers instance;
    private final Map<UUID, FPlayer> fPlayers;
    private final DbFPlayer db;

    public FPlayers() {
        instance = this;
        this.fPlayers = new HashMap<>();
        this.db = new DbFPlayer();
    }

    public static FPlayers getInstance() {
        return instance;
    }

    public FPlayer getByPlayer(Player player) {
        return getByUuid(player.getUniqueId());
    }

    public FPlayer getByUuid(UUID uuid) {
        return fPlayers.computeIfAbsent(uuid, k -> {
            FPlayer fp = db.load(uuid);
            if (fp == null) {
                fp = new FPlayer(uuid, Main.getInstance().getServer().getOfflinePlayer(uuid).getName());
                db.save(fp);
            }
            return fp;
        });
    }

    public FPlayer getByName(String name) {
        for (FPlayer fp : fPlayers.values()) {
            if (fp.getName().equalsIgnoreCase(name)) {
                return fp;
            }
        }
        return null;
    }

    public Collection<FPlayer> getAll() {
        return fPlayers.values();
    }

    public void save(FPlayer fPlayer) {
        db.save(fPlayer);
    }

    public void saveAll() {
        for (FPlayer fp : fPlayers.values()) {
            db.save(fp);
        }
    }

    public void loadAll() {
        List<FPlayer> loaded = db.loadAll();
        for (FPlayer fp : loaded) {
            fPlayers.put(fp.getUuid(), fp);
        }
    }
}
