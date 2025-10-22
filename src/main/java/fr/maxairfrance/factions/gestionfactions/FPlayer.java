package fr.maxairfrance.factions.gestionfactions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FPlayer {

    private final UUID uuid;
    private String name;
    private String factionId;
    private Role role;
    private int power;
    private int maxPower;

    public FPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.factionId = null;
        this.role = Role.RECRUE;
        this.power = 10;
        this.maxPower = 10;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFactionId() {
        return factionId;
    }

    public void setFactionId(String factionId) {
        this.factionId = factionId;
    }

    public boolean hasFaction() {
        return factionId != null;
    }

    public Faction getFaction() {
        if (factionId == null) return null;
        return Factions.getInstance().getById(factionId);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = Math.max(0, Math.min(power, maxPower));
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public void sendMessage(String message) {
        Player p = getPlayer();
        if (p != null) {
            p.sendMessage(message);
        }
    }
}
