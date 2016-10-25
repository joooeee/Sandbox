package sandbox.cmdlib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import sandbox.cmdlib.info.Command;

public abstract class AbstractCommand {
    
    private static Map<UUID, Sender> cachedSenders = new HashMap<>();
    private static Sender consoleSender;
    
    private static Sender getSender(CommandSender sender) {
	if (sender instanceof Player) {
	    Player player = (Player) sender;
	    
	    Sender s;
	    
	    if (cachedSenders.containsKey(player.getUniqueId())) {
		s = cachedSenders.get(player.getUniqueId());
		s.refresh(player);
	    } else {
		cachedSenders.put(player.getUniqueId(), s = new Sender(player));
	    }
	    
	    return s;
	}
	if (sender instanceof ConsoleCommandSender)
	    return consoleSender == null ? consoleSender = new Sender(sender) : consoleSender;
	if (sender instanceof BlockCommandSender)
	    return new Sender(sender);
	
	return null;
    }
    
    private Command info;
    
    public AbstractCommand(Plugin plugin) {
	
	if (getClass().isAnnotationPresent(Command.class))
	    info = getClass().getAnnotation(Command.class);
	else {
	    try {
		
		Method executeMethod = getClass().getMethod("execute", Sender.class, Arguments.class);
		
		if (executeMethod.isAnnotationPresent(Command.class)) {
		    info = executeMethod.getAnnotation(Command.class);
		}
	    } catch (Exception exception) {
		System.out.println("Failed to register command with class name " + getClass().getName() + "! Stacktrace:");
		exception.printStackTrace();
		return;
	    }
	    
	}
	
	if (info == null) {
	    throw new RuntimeException("Couldn't find Command annotation in Command class " + getClass().getName());
	}
	
	try {
	    Field cMap = SimplePluginManager.class.getDeclaredField("commandMap");
	    cMap.setAccessible(true);
	    CommandMap map = (CommandMap) cMap.get(Bukkit.getPluginManager());
	    map.register(plugin.getDescription().getName(), new org.bukkit.command.Command(name(), description(), generateUsage(), Arrays.asList(aliases())) {
		@Override
		public boolean execute(CommandSender sender, String unusedLabel, String[] args) {
		    handle(sender, args);
		    return true;
		}
	    });
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public String name() {
	return info.name();
    }
    
    public String[] aliases() {
	return info.aliases();
    }
    
    public String permission() {
	return info.permission();
    }
    
    public int requiredArguments() {
	return info.requiredArguments();
    }
    
    public String usage() {
	return info.usage();
    }
    
    public String permissionMessage() {
	return info.permissionMessage();
    }
    
    public String description() {
	return info.description();
    }
    
    public String generateUsage() {
	return ChatColor.RED + "Usage: /" + name() + " " + usage();
    }
    
    public final void handle(CommandSender sender, String[] args) {
	if (!sender.hasPermission(permission())) {
	    sender.sendMessage(permissionMessage());
	    return;
	}
	
	if (args.length < requiredArguments()) {
	    sender.sendMessage(generateUsage());
	    return;
	}
	
	execute(getSender(sender), new Arguments(args));
    }
    
    public abstract void execute(Sender sender, Arguments arguments);
    
}
