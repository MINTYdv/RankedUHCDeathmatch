package xyz.mintydev.uhcdeathmatch.duels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;

public class DuelManager {

	private final UHCDeathMatch main;
	
	private List<DuelRequest> requests = new ArrayList<>();
	
	public DuelManager(UHCDeathMatch main) {
		this.main = main;
		
		// launch runnable
		new DuelRequestExpireRunnable(main).runTaskTimer(main, 20*5, 20*5);
	}
	
	public void createRequest(Player player, Player target, UHCMode mode) {
		
		// create the request
		final DuelRequest request = new DuelRequest(player, target, mode, new Date());
		addRequest(request);
		
		// notify player
		
		final String content = Lang.get("commands.duel.messages.received").replaceAll("%player%", player.getName()).replaceAll("%mode%", ChatColor.stripColor(mode.getDisplayName()));
		TextComponent text = new TextComponent(content);
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.get("commands.duel.messages.hover")).create()));
		text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + player.getName()));
		target.spigot().sendMessage(text);
		
		// notify sender

		player.sendMessage(Lang.get("commands.duel.messages.request-sent").replaceAll("%player%", target.getName()).replaceAll("%mode%", ChatColor.stripColor(mode.getDisplayName())));
	}
	
	public void acceptRequest(DuelRequest request) {

		// notify asker
		request.getAskingPlayer().sendMessage(Lang.get("commands.duel.messages.accepted.asking-player").replaceAll("%target%", request.getOpponent().getName()));
		
		// notify player
		request.getOpponent().sendMessage(Lang.get("commands.duel.messages.accepted.player").replaceAll("%player%", request.getAskingPlayer().getName()));
		
		// remove request
		removeRequest(request);
		
	}
	
	public void addRequest(DuelRequest request) {
		this.requests.add(request);
	}
	
	public void removeRequest(DuelRequest request) {
		this.requests.remove(request);
	}
	
	public List<DuelRequest> getRequestsFrom(Player from, Player player){
		List<DuelRequest> res = new ArrayList<>();
		
		for(DuelRequest request : requests) {
			if(request.getAskingPlayer().equals(from) && request.getOpponent().equals(player)) {
				res.add(request);
			}
		}
		return res;
	}
	
	public List<DuelRequest> getSentRequests(Player sender){
		List<DuelRequest> res = new ArrayList<>();
		
		for(DuelRequest request : requests) {
			if(request.getAskingPlayer().equals(sender)) {
				res.add(request);
			}
		}
		return res;
	}
	
	public boolean hasSentRequestTo(Player sender, Player target) {
		List<DuelRequest> reqs = getSentRequests(sender);
		if(reqs == null || reqs.size() == 0) return false;
		
		for(DuelRequest req : reqs) {
			if(req.getOpponent().equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public List<DuelRequest> getRequests() {
		return requests;
	}
	
}
