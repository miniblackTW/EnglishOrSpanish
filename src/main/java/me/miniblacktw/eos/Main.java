package me.miniblacktw.eos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin implements CommandExecutor {

    private boolean inProgress = false;
    private BukkitTask msgTask;
    private final Set<UUID> playersInGame = new HashSet<>();
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final long COOLDOWN_TIME = 30 * 60 * 1000;

    private final String[] messages = {
            "§7Baby, you got something in your nose",
            "§7Sniffing that K, did you feel the hole?",
            "§7Hope you find peace for yourself",
            "§7New boyfriend ain't going fill the void",
            "§7Do you even really like this track?",
            "§7Take away the drugs, would you feel the noise?",
            "§7More and more you try to run away..."
    };

    @Override
    public void onEnable() {
        this.getCommand("englishorspanish").setExecutor(this);
        this.getCommand("eos").setExecutor(this);
        getServer().getPluginManager().registerEvents(new Movements(this), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        if (inProgress) {
            player.sendMessage(ChatColor.RED + "There is already a game in progress!");
            return true;
        }

        if (cooldowns.containsKey(playerId)) {
            long timeSinceLastUse = System.currentTimeMillis() - cooldowns.get(playerId);
            if (timeSinceLastUse < COOLDOWN_TIME) {
                long timeRemaining = (COOLDOWN_TIME - timeSinceLastUse) / 1000;
                player.sendMessage(ChatColor.RED + "Try again in " + timeRemaining / 60 + " minutes");
                return true;
            }
        }

        cooldowns.put(playerId, System.currentTimeMillis());
        startGame();
        return true;
    }

    private void startGame() {
        playersInGame.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            playersInGame.add(p.getUniqueId());
            p.sendTitle(ChatColor.GOLD + "English or Spanish?", ChatColor.RED + "Whoever moves first is gay!!!");
        }

        Bukkit.getScheduler().runTaskLater(this, () -> {
            inProgress = true;
        }, 20L);

        msgTask = new BukkitRunnable() {
            private int index = 0;

            @Override
            public void run() {
                if (!inProgress) {
                    cancel();
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(messages[index]);
                }
                index = (index + 1) % messages.length;
            }
        }.runTaskTimer(this, 80L, 80L);
    }

    public void endGame(UUID losingPlayerId) {
        inProgress = false;

        Player loser = Bukkit.getPlayer(losingPlayerId);
        String loserName = (loser != null) ? loser.getName() : "A player";

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(losingPlayerId)) {
                player.sendTitle(ChatColor.RED + "You Moved!", "");
            } else {
                player.sendTitle(ChatColor.GREEN + "Victory!", "");
            }
        }

        Bukkit.broadcastMessage(ChatColor.AQUA + loserName + ChatColor.RED + " is a gay!");
        if (msgTask != null) {
            msgTask.cancel();
        }
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public Set<UUID> getPlayersInGame() {
        return playersInGame;
    }
}