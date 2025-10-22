package fr.maxairfrance.factions.commands;

import fr.maxairfrance.factions.Main;
import fr.maxairfrance.factions.gestionfactions.FPlayer;
import fr.maxairfrance.factions.gestionfactions.FPlayers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FCommand implements CommandExecutor, TabCompleter {

    protected static final Map<String, FCommand> subCommands = new HashMap<>();

    protected final String name;
    protected final List<String> aliases;
    protected final String permission;
    protected final String description;
    protected final boolean requiresFaction;
    protected final boolean requiresPlayer;

    public FCommand(String name, String description, boolean requiresPlayer, boolean requiresFaction) {
        this.name = name.toLowerCase();
        this.aliases = new ArrayList<>();
        this.permission = "factions." + name;
        this.description = description;
        this.requiresPlayer = requiresPlayer;
        this.requiresFaction = requiresFaction;

        subCommands.put(this.name, this);
    }

    public FCommand addAlias(String... aliases) {
        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
            subCommands.put(alias.toLowerCase(), this);
        }
        return this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return execute(sender, new String[0]);
        }

        String subCommand = args[0].toLowerCase();
        FCommand cmd = subCommands.get(subCommand);

        if (cmd != null) {
            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            return cmd.execute(sender, newArgs);
        }

        return execute(sender, args);
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (requiresPlayer && !(sender instanceof Player)) {
            sender.sendMessage(color("&cCette commande ne peut être exécutée que par un joueur."));
            return true;
        }

        Player player = requiresPlayer ? (Player) sender : null;
        FPlayer fPlayer = player != null ? FPlayers.getInstance().getByPlayer(player) : null;

        if (requiresFaction && (fPlayer == null || !fPlayer.hasFaction())) {
            sender.sendMessage(color("&cVous devez appartenir à une faction pour utiliser cette commande."));
            return true;
        }

        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(color("&cVous n'avez pas la permission d'utiliser cette commande."));
            return true;
        }

        return executeCommand(sender, player, fPlayer, args);
    }

    public abstract boolean executeCommand(CommandSender sender, Player player, FPlayer fPlayer, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>(subCommands.keySet());
            completions.removeIf(s -> !s.startsWith(args[0].toLowerCase()));
            return completions;
        }

        if (args.length > 1) {
            String subCommand = args[0].toLowerCase();
            FCommand cmd = subCommands.get(subCommand);
            if (cmd != null) {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                return cmd.onTabComplete(sender, command, alias, newArgs);
            }
        }

        return new ArrayList<>();
    }

    protected String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    protected Main getPlugin() {
        return Main.getInstance();
    }
}