package fr.maxairfrance.factions;

import fr.maxairfrance.factions.commands.FCreateCommand;
import fr.maxairfrance.factions.commands.FDisbandCommand;
import fr.maxairfrance.factions.commands.FPowerCommand;
import fr.maxairfrance.factions.commands.FShowCommand;
import fr.maxairfrance.factions.gestionfactions.FPlayers;
import fr.maxairfrance.factions.gestionfactions.Factions;
import fr.maxairfrance.factions.listener.player.PlayerDeathListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private Factions factions;
    private FPlayers fPlayers;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        factions = new Factions();
        fPlayers = new FPlayers();

        factions.loadAll();
        fPlayers.loadAll();

        getCommand("f").setExecutor(new FShowCommand());
        getCommand("f").setTabCompleter(new FShowCommand());

        new FCreateCommand();
        new FShowCommand();
        new FDisbandCommand();
        new FPowerCommand();

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    @Override
    public void onDisable() {
        if (factions != null) {
            factions.saveAll();
        }
        if (fPlayers != null) {
            fPlayers.saveAll();
        }

        getLogger().info("Plugin Factions disabled!");
    }

    public static Main getInstance() {
        return instance;
    }

    public Factions getFactions() {
        return factions;
    }

    public FPlayers getFPlayers() {
        return fPlayers;
    }
}
