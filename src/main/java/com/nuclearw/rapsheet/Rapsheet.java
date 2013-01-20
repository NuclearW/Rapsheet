package com.nuclearw.rapsheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.nuclearw.rapsheet.api.RapsheetManager;
import com.nuclearw.rapsheet.commands.BaseCommandExecutor;

public class Rapsheet extends JavaPlugin {
	private static RapsheetManager manager;

	@Override
	public void onEnable() {
		initDatabase();

		manager = new SimpleRapsheetManager(this);

		getCommand("rapsheet").setExecutor(new BaseCommandExecutor(this));

		metrics();

		getLogger().info("Finished loading " + getDescription().getFullName());
	}

	@Override
	public void onDisable() {
		getLogger().info("Finished unloading " + getDescription().getFullName());
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(Record.class);
		return list;
	}

	public static RapsheetManager getManager() {
		return manager;
	}

	private void initDatabase() {
		try {
			getDatabase().find(Record.class).findRowCount();
		} catch (PersistenceException ex) {
			getLogger().info("Initializing database");
			this.installDDL();
		}
	}

	private void metrics() {
		try {
			Metrics metrics = new Metrics(this);
//			metrics.start();
		} catch (IOException e) { }
	}
}
