package fr.maxairfrance.factions.commands;

import fr.maxairfrance.factions.gestionfactions.FPlayer;
import fr.maxairfrance.factions.gestionfactions.Faction;
import fr.maxairfrance.factions.gestionfactions.Factions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class FDisbandCommand extends FCommand {

    public FDisbandCommand() {
        super("disband", "Dissoudre votre faction", true, true);
        addAlias("supprimer", "delete", "dissolve");
    }

    @Override
    public boolean executeCommand(CommandSender sender, Player player, FPlayer fPlayer, String[] args) {
        Faction faction = fPlayer.getFaction();

        if (!faction.isLeader(player.getUniqueId())) {
            player.sendMessage(color("&cSeul le chef de la faction peut la dissoudre !"));
            return true;
        }

        String factionName = faction.getName();

        faction.broadcast(color("&cLa faction &f" + factionName + "&c a été dissoute par son chef."));

        Factions.getInstance().deleteFaction(faction.getId());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
