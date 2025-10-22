package fr.maxairfrance.factions.listener.player;

import fr.maxairfrance.factions.Main;
import fr.maxairfrance.factions.gestionfactions.FPlayer;
import fr.maxairfrance.factions.gestionfactions.FPlayers;
import fr.maxairfrance.factions.gestionfactions.Faction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);

        if (fPlayer == null) return;

        int powerLoss = Main.getInstance().getConfig().getInt("power.power-loss-on-death", 4);

        int oldPower = fPlayer.getPower();
        int newPower = Math.max(-10, oldPower - powerLoss);

        fPlayer.setPower(newPower);
        FPlayers.getInstance().save(fPlayer);

        if (fPlayer.hasFaction()) {
            Faction faction = fPlayer.getFaction();
            faction.updatePower();
            Main.getInstance().getFactions().save(faction);
        }
    }
}
