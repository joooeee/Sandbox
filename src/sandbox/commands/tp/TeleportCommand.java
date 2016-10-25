package sandbox.commands.tp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class TeleportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cl, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console cannot use this command.");
		}
		return true;
	}

}
