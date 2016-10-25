package sandbox.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Sandbox extends JavaPlugin {
	
	public void onEnable() {
		log("Initializing Sandbox.");
		update();
	}
	
	public void onDisable() {
		log("Disabled.");
	}
	
	public static void log(String msg) {
		System.out.println(msg);
	}
	
	public void update() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL("https://raw.githubusercontent.com/joooeee/Sandbox/master/version/version1.txt");
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String inputLine = in.readLine();
					if (inputLine.equals("updatetrue")) {
						Bukkit.getConsoleSender().sendMessage("A new version of Sandbox has been released at BukkitDev.");
						Bukkit.getConsoleSender().sendMessage("It is recommended you update to it.");
						Bukkit.getConsoleSender().sendMessage("Version Number: 0.1");
					} else {
						Bukkit.getConsoleSender().sendMessage("No update found.");
					}
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}
