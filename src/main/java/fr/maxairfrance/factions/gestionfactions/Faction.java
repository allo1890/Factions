package fr.maxairfrance.factions.gestionfactions;

import java.util.*;

public class Faction {

    private final String id;
    private String name;
    private String tag;
    private String description;
    private UUID leader;
    private final Set<UUID> members;
    private final Set<UUID> officers;
    private final Set<String> allies;
    private final Set<String> enemies;
    private int power;
    private int maxPower;
    private boolean open;
    private long createdDate;

    public Faction(String id, String name, UUID leader) {
        this.id = id;
        this.name = name;
        this.tag = name;
        this.description = "-";
        this.leader = leader;
        this.members = new HashSet<>();
        this.officers = new HashSet<>();
        this.allies = new HashSet<>();
        this.enemies = new HashSet<>();
        this.power = 0;
        this.maxPower = 0;
        this.open = false;
        this.createdDate = System.currentTimeMillis();

        members.add(leader);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public Set<UUID> getMembers() {
        return new HashSet<>(members);
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
        updatePower();
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
        officers.remove(uuid);
        updatePower();
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public Set<UUID> getOfficers() {
        return new HashSet<>(officers);
    }

    public void addOfficer(UUID uuid) {
        officers.add(uuid);
    }

    public void removeOfficer(UUID uuid) {
        officers.remove(uuid);
    }

    public boolean isOfficer(UUID uuid) {
        return officers.contains(uuid);
    }

    public boolean isLeader(UUID uuid) {
        return leader.equals(uuid);
    }

    public Role getRole(UUID uuid) {
        if (isLeader(uuid)) return Role.CHEF;
        if (isOfficer(uuid)) return Role.OFFICIER;
        if (isMember(uuid)) return Role.MEMBRE;
        return Role.RECRUE;
    }

    public Set<String> getAllies() {
        return new HashSet<>(allies);
    }

    public void addAlly(String factionId) {
        allies.add(factionId);
    }

    public void removeAlly(String factionId) {
        allies.remove(factionId);
    }

    public Set<String> getEnemies() {
        return new HashSet<>(enemies);
    }

    public void addEnemy(String factionId) {
        enemies.add(factionId);
    }

    public void removeEnemy(String factionId) {
        enemies.remove(factionId);
    }

    public int getPower() {
        return power;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void updatePower() {
        int totalPower = 0;
        int totalMaxPower = 0;

        for (UUID uuid : members) {
            FPlayer fp = FPlayers.getInstance().getByUuid(uuid);
            if (fp != null) {
                totalPower += fp.getPower();
                totalMaxPower += fp.getMaxPower();
            }
        }

        this.power = totalPower;
        this.maxPower = totalMaxPower;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public int getMemberCount() {
        return members.size();
    }

    public List<FPlayer> getOnlineMembers() {
        List<FPlayer> online = new ArrayList<>();
        for (UUID uuid : members) {
            FPlayer fp = FPlayers.getInstance().getByUuid(uuid);
            if (fp != null && fp.isOnline()) {
                online.add(fp);
            }
        }
        return online;
    }

    public void broadcast(String message) {
        for (FPlayer fp : getOnlineMembers()) {
            fp.sendMessage(message);
        }
    }
}
