package fr.maxairfrance.factions.gestionfactions;

public enum Role {
    CHEF("Chef", 4),
    OFFICIER("Officier", 3),
    MEMBRE("Membre", 2),
    RECRUE("Recrue", 1);

    private final String displayName;
    private final int power;

    Role(String displayName, int power) {
        this.displayName = displayName;
        this.power = power;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPower() {
        return power;
    }

    public static Role fromString(String str) {
        for (Role role : values()) {
            if (role.name().equalsIgnoreCase(str)) {
                return role;
            }
        }
        return RECRUE;
    }
}
