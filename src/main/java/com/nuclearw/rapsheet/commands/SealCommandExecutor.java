package com.nuclearw.rapsheet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nuclearw.rapsheet.Rapsheet;
import com.nuclearw.rapsheet.Record;

public class SealCommandExecutor extends RapsheetCommand implements CommandExecutor {
	public SealCommandExecutor(Rapsheet plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 3) {
			printArgsError(args);
			printHelp(sender, label);
			return true;
		}

		String target = findTarget(args[1]);

		int chargeId = -1;

		try {
			chargeId = Integer.valueOf(args[2]);
		} catch (NumberFormatException ex) {
			printHelp(sender, label);
			return true;
		}

		Record found = Rapsheet.getManager().getCharge(target, chargeId);

		if(found == null) {
			sender.sendMessage(COULD_NOT_FIND_CHARGE.replace("<PLAYER>", target));
			return true;
		}

		if(found.isSealed()) {
			sender.sendMessage(ChatColor.RED + "You cannot seal a charge that is already sealed!");
			return true;
		}

		boolean success = Rapsheet.getManager().sealPlayerCharge(target, chargeId);

		if(!success) {
			plugin.getLogger().severe("Error trying to seal player " + target + "'s chargeId: " + chargeId);
		}

		sender.sendMessage(ChatColor.GRAY + "Sealed " + ChatColor.GOLD + "charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " - " + ChatColor.AQUA + found.getChargeShort());

		// Notify player if they are online
		Player player = plugin.getServer().getPlayer(target);

		if(player == null) {
			return true;
		}

		player.sendMessage(ChatColor.GOLD + "Charge " + ChatColor.RESET + "#" + found.getChargeId() + ChatColor.GOLD + " has been " + ChatColor.GRAY + "sealed" + ChatColor.GOLD + " by " + ChatColor.AQUA + sender.getName());

		return true;
	}
}
