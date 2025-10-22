package fr.maxairfrance.factions.commands;

import fr.maxairfrance.factions.gestionfactions.FPlayer;
import fr.maxairfrance.factions.gestionfactions.FPlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class FPowerCommand extends FCommand {

    public FPowerCommand() {
        super("power", "Afficher votre puissance ou celle d'un joueur", true, false);
        addAlias("p", "puissance");
    }

    @Override
    public boolean executeCommand(CommandSender sender, Player player, FPlayer fPlayer, String[] args) {
        FPlayer targetPlayer;
        String targetName;

        if (args.length > 0) {
            String searchName = args[0];
            Player target = Bukkit.getPlayer(searchName);

            if (target == null) {
                targetPlayer = FPlayers.getInstance().getByName(searchName);
                if (targetPlayer == null) {
                    player.sendMessage(color("&cLe joueur '" + searchName + "' n'a jamais joué sur le serveur."));
                    return true;
                }
                targetName = targetPlayer.getName();
            } else {
                targetPlayer = FPlayers.getInstance().getByPlayer(target);
                targetName = target.getName();
            }
        } else {
            targetPlayer = fPlayer;
            targetName = player.getName();
        }

        displayPower(player, targetPlayer, targetName);
        return true;
    }

    private void displayPower(Player viewer, FPlayer target, String targetName) {
        int currentPower = target.getPower();
        int maxPower = target.getMaxPower();

        double percentage = maxPower > 0 ? ((double) currentPower / maxPower) * 100 : 0;

        String powerColor;
        if (percentage >= 75) {
            powerColor = "&a";
        } else if (percentage >= 50) {
            powerColor = "&e";
        } else if (percentage >= 25) {
            powerColor = "&6";
        } else {
            powerColor = "&c";
        }

        int barLength = 20;
        int filledBars = (int) ((percentage / 100) * barLength);
        StringBuilder powerBar = new StringBuilder();

        for (int i = 0; i < barLength; i++) {
            if (i < filledBars) {
                powerBar.append("█");
            } else {
                powerBar.append("░");
            }
        }

        target.isOnline();

        viewer.sendMessage(color("&a&l------------------------------------------"));
        viewer.sendMessage(color("&6Pseudo: " + targetName.toUpperCase()));
        viewer.sendMessage(color("&ePower: " + powerColor + currentPower + "&7/&f" + maxPower));
        viewer.sendMessage(color("&a&l------------------------------------------"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                playerNames.add(p.getName());
            }
            playerNames.removeIf(s -> !s.toLowerCase().startsWith(args[0].toLowerCase()));
            return playerNames;
        }
        return new ArrayList<>();
    }
}
