package fr.maxairfrance.factions.commands;

import fr.maxairfrance.factions.gestionfactions.FPlayer;
import fr.maxairfrance.factions.gestionfactions.Factions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class FCreateCommand extends FCommand {

    public FCreateCommand() {
        super("create", "Créer une nouvelle faction", true, false);
        addAlias("creer", "c");
    }

    @Override
    public boolean executeCommand(CommandSender sender, Player player, FPlayer fPlayer, String[] args) {
        if (fPlayer.hasFaction()) {
            player.sendMessage(color("&cVous appartenez déjà à une faction! Quittez-la d'abord avec /f leave"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(color("&cUsage: /f create <nom>"));
            return true;
        }

        String factionName = args[0];

        if (factionName.length() < 3) {
            player.sendMessage(color("&cLe nom de la faction doit contenir au moins 3 caractères."));
            return true;
        }

        if (factionName.length() > 16) {
            player.sendMessage(color("&cLe nom de la faction ne peut pas dépasser 16 caractères."));
            return true;
        }

        if (!factionName.matches("^[a-zA-Z0-9_]+$")) {
            player.sendMessage(color("&cLe nom de la faction ne peut contenir que des lettres, chiffres et underscores."));
            return true;
        }

        if (Factions.getInstance().exists(factionName)) {
            player.sendMessage(color("&cUne faction avec ce nom existe déjà!"));
            return true;
        }

        Factions.getInstance().createFaction(factionName, player.getUniqueId());

        player.sendMessage(color("&a&l━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(color("&6&lFACTION CRÉÉE"));
        player.sendMessage(color("&e"));
        player.sendMessage(color("&aVous avez créé la faction &f" + factionName + "&a!"));
        player.sendMessage(color("&aVous en êtes maintenant le chef."));
        player.sendMessage(color("&a&l━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
