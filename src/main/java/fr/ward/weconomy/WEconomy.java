package fr.ward.weconomy;

import fr.ward.weconomy.command.WEconomyCommand;
import fr.ward.weconomy.config.ConfigType;
import fr.ward.weconomy.listeners.PlayerJoinLeaveListener;
import fr.ward.weconomy.manager.*;
import fr.ward.weconomy.placeholder.SomeExpansion;
import fr.ward.weconomy.utils.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class WEconomy extends JavaPlugin {

    private static WEconomy INSTANCE;

    private EconomyManager economyManager;
    private CacheManager cacheManager;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private DiscordManager discordManager;

    public WEconomy() {
        INSTANCE = this;
    }

    /**
     * Get the actual WEconomy instance
     *
     * @return {@link WEconomy} the actual instance
     */
    public static WEconomy getInstance() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        this.cacheManager = new CacheManager();
        this.economyManager = new EconomyManager();
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager();
        this.discordManager = new DiscordManager();

        economyManager.load();
        configManager.load();
        databaseManager.load();
        discordManager.load();

        setupPlaceHolder();

        MessageManager.build(ConfigType.MESSAGE.getGeneratedYML().getConfig());

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinLeaveListener(), this);

        Objects.requireNonNull(this.getCommand("economy")).setExecutor(new WEconomyCommand());

        loadBStats();

        super.onEnable();
    }

    private void setupPlaceHolder() {
        if (getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null) {
            new SomeExpansion().register();
        }
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public DiscordManager getDiscordManager() {
        return discordManager;
    }

    private void loadBStats() {
        new Metrics(this, 17624);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
