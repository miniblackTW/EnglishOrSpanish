package me.miniblacktw.englishorspanish;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

public class Movements implements Listener {

    private final EnglishOrSpanish plugin;

    public Movements(EnglishOrSpanish plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.isGameInProgress() && plugin.getPlayersInGame().contains(event.getPlayer().getUniqueId())) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                plugin.endGame(event.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (plugin.isGameInProgress() && plugin.getPlayersInGame().contains(event.getPlayer().getUniqueId())) {
            plugin.endGame(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (plugin.isGameInProgress() && plugin.getPlayersInGame().contains(event.getPlayer().getUniqueId())) {
            plugin.endGame(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (plugin.isGameInProgress() && plugin.getPlayersInGame().contains(event.getPlayer().getUniqueId())) {
            plugin.endGame(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.isGameInProgress() && plugin.getPlayersInGame().contains(event.getPlayer().getUniqueId())) {
            plugin.endGame(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.isGameInProgress()) {
            plugin.getPlayersInGame().remove(event.getPlayer().getUniqueId());
        }
    }
}