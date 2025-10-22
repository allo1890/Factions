package fr.maxairfrance.factions.commands;

import fr.maxairfrance.factions.gestionfactions.FPlayer;
import fr.maxairfrance.factions.gestionfactions.FPlayers;
import fr.maxairfrance.factions.gestionfactions.Faction;
import fr.maxairfrance.factions.gestionfactions.Factions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FShowCommand extends FCommand {

    public FShowCommand() {
        super("show", "Afficher les informations d'une faction", true, false);
        addAlias("who", "f", "info");
    }

    @Override
    public boolean executeCommand(CommandSender sender, Player player, FPlayer fPlayer, String[] args) {
        Faction faction;

        if (args.length == 0) {
            if (!fPlayer.hasFaction()) {
                player.sendMessage(color("&cVous n'appartenez à aucune faction !"));
                player.sendMessage(color("&eUtilisez &f/f create <nom> &epour créer une faction."));
                return true;
            }
            faction = fPlayer.getFaction();
        } else {
            String factionName = args[0];
            faction = Factions.getInstance().getByName(factionName);

            if (faction == null) {
                player.sendMessage(color("&cLa faction '" + factionName + "' n'existe pas."));
                return true;
            }
        }

        displayFaction(player, faction, fPlayer);
        return true;
    }

    private void displayFaction(Player player, Faction faction, FPlayer fPlayer) {
        FPlayer leader = FPlayers.getInstance().getByUuid(faction.getLeader());

        int onlineCount = faction.getOnlineMembers().size();
        int totalCount = faction.getMemberCount();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String createdDate = sdf.format(new Date(faction.getCreatedDate()));

        StringBuilder membersList = new StringBuilder();
        List<FPlayer> members = new ArrayList<>();
        for (java.util.UUID uuid : faction.getMembers()) {
            FPlayer fp = FPlayers.getInstance().getByUuid(uuid);
            if (fp != null) {
                members.add(fp);
            }
        }

        members.sort((a, b) -> {
            int roleA = faction.getRole(a.getUuid()).getPower();
            int roleB = faction.getRole(b.getUuid()).getPower();
            return Integer.compare(roleB, roleA);
        });

        for (int i = 0; i < members.size(); i++) {
            FPlayer fp = members.get(i);
            String roleColor = getRoleColor(faction.getRole(fp.getUuid()));
            String onlineStatus = fp.isOnline() ? "&a●" : "&7●";

            membersList.append(color(onlineStatus + " " + roleColor + fp.getName()));

            if (i < members.size() - 1) {
                membersList.append(color("&7, "));
            }
        }

        player.sendMessage(color("&a&m-------------------------------"));
        player.sendMessage(color("&6&l" + faction.getName().toUpperCase()));
        player.sendMessage(color("&7&o" + faction.getDescription()));
        player.sendMessage(color("&e◆ Chef: &f" + (leader != null ? leader.getName() : "Inconnu")));
        player.sendMessage(color("&e◆ Membres: &f" + onlineCount + "&7/&f" + totalCount));
        player.sendMessage(color("&e◆ Puissance: &f" + faction.getPower() + "&7/&f" + faction.getMaxPower()));
        player.sendMessage(color("&e◆ Créée le: &f" + createdDate));
        player.sendMessage(color("&e&lMEMBRES:"));
        player.sendMessage(color("&f" + membersList));
        player.sendMessage(color("&a&m-------------------------------"));
    }

    private String getRoleColor(fr.maxairfrance.factions.gestionfactions.Role role) {
        switch (role) {
            case CHEF:
                return "&c";
            case OFFICIER:
                return "&6";
            case MEMBRE:
                return "&e";
            default:
                return "&7";
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> factionNames = new ArrayList<>();
            for (Faction f : Factions.getInstance().getAll()) {
                factionNames.add(f.getName());
            }
            factionNames.removeIf(s -> !s.toLowerCase().startsWith(args[0].toLowerCase()));
            return factionNames;
        }
        return new ArrayList<>();
    }
}
