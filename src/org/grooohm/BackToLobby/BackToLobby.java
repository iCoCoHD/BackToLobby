package org.grooohm.BackToLobby;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class BackToLobby extends Plugin {
	private String lobby;
	private String alreadyThere;
	private String commandName;

	public void onEnable() {
		loadConfig("plugins/" + this.getDescription().getName(), "/config.ini");
		ProxyServer.getInstance().getPluginManager().registerCommand(new Command(commandName) {
			@Override
			public void execute(CommandSender sender, String[] args) {
				ProxiedPlayer p = (ProxiedPlayer)sender;
				if(!p.getServer().getInfo().getName().equals(lobby)) {
					if(ProxyServer.getInstance().getServers().containsKey(lobby))
					{
						p.connect(ProxyServer.getInstance().getServerInfo(lobby));
					} else {
						p.sendMessage("The server "+lobby+" does not exist!");
					}
				} else {
					p.sendMessage(alreadyThere);
				}
			}
		});
	}

	private void loadConfig(String folder, String filename) {
		Properties config = new Properties();
		try {
			File dir = new File(folder);
			if (!dir.exists())
				dir.mkdirs();

			File file = new File(folder + filename);
			if (!file.exists()) {
				file.createNewFile();
				config.load(new FileReader(file));
				config.setProperty("commandName", "lobby");
				config.setProperty("lobbyServer", "servername");
				config.setProperty("alreadyThere", "You are already on the lobby server");
				config.store(new FileWriter(file), this.getDescription().getName() + " Configuration");
			} else {
				config.load(new FileReader(file));
			}
			this.commandName = config.getProperty("commandName", "lobby");
			this.lobby = config.getProperty("lobbyServer", "servername");
			this.alreadyThere = config.getProperty("alreadyThere", "You are already on the lobby server");
		} catch (Exception e) {
			ProxyServer.getInstance().getLogger().severe("Unable to load " + this.getDescription().getName() + " configuration file, please make sure it exists!  Using default values");
		}
		
	}
}
