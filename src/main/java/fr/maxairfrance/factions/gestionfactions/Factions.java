package fr.maxairfrance.factions.gestionfactions;

import fr.maxairfrance.factions.database.DbFaction;

import java.util.*;

public class Factions {

    private static Factions instance;
    private final Map<String, Faction> factionsById;
    private final Map<String, Faction> factionsByName;
    private final DbFaction db;

    public Factions() {
        instance = this;
        this.factionsById = new HashMap<>();
        this.factionsByName = new HashMap<>();
        this.db = new DbFaction();
    }

    public static Factions getInstance() {
        return instance;
    }

    public Faction createFaction(String name, UUID leader) {
        String id = UUID.randomUUID().toString();
        Faction faction = new Faction(id, name, leader);

        factionsById.put(id, faction);
        factionsByName.put(name.toLowerCase(), faction);

        FPlayer fp = FPlayers.getInstance().getByUuid(leader);
        fp.setFactionId(id);
        fp.setRole(Role.CHEF);
        FPlayers.getInstance().save(fp);

        db.save(faction);

        return faction;
    }

    public void deleteFaction(String id) {
        Faction faction = factionsById.get(id);
        if (faction != null) {
            for (UUID uuid : faction.getMembers()) {
                FPlayer fp = FPlayers.getInstance().getByUuid(uuid);
                fp.setFactionId(null);
                fp.setRole(Role.RECRUE);
                FPlayers.getInstance().save(fp);
            }

            factionsById.remove(id);
            factionsByName.remove(faction.getName().toLowerCase());
            db.delete(id);
        }
    }

    public Faction getById(String id) {
        return factionsById.get(id);
    }

    public Faction getByName(String name) {
        return factionsByName.get(name.toLowerCase());
    }

    public boolean exists(String name) {
        return factionsByName.containsKey(name.toLowerCase());
    }

    public Collection<Faction> getAll() {
        return factionsById.values();
    }

    public void save(Faction faction) {
        db.save(faction);
    }

    public void saveAll() {
        for (Faction faction : factionsById.values()) {
            db.save(faction);
        }
    }

    public void loadAll() {
        List<Faction> loaded = db.loadAll();
        for (Faction faction : loaded) {
            factionsById.put(faction.getId(), faction);
            factionsByName.put(faction.getName().toLowerCase(), faction);
        }
    }
}
