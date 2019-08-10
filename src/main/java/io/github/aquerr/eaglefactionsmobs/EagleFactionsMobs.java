package io.github.aquerr.eaglefactionsmobs;

import com.google.inject.Inject;
//import io.github.aquerr.eaglefactions.api.EagleFactions;
//import io.github.aquerr.eaglefactions.api.logic.FactionLogic;
//import io.github.aquerr.eaglefactions.api.managers.IPowerManager;
import io.github.aquerr.eaglefactions.api.EagleFactions;
import io.github.aquerr.eaglefactions.api.logic.FactionLogic;
import io.github.aquerr.eaglefactions.api.managers.IPowerManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION, authors = {"Aquerr/Nerdi"},
dependencies = {@Dependency(id = "eaglefactions")})
public class EagleFactionsMobs
{
    private final CommandManager commandManager;
    private final EventManager eventManager;
    private final Path configPath;

    private EagleFactions eagleFactions;
    private FactionLogic factionLogic;
    private IPowerManager powerManager;

    @Inject
    private EagleFactionsMobs(final CommandManager commandManager, final EventManager eventManager, final @ConfigDir(sharedRoot = false) Path configPath) {
        this.commandManager = commandManager;
        this.eventManager = eventManager;
        this.configPath = configPath;
    }

    @Listener
    public void onInitialization(final GameInitializationEvent event)
    {
        Sponge.getServer().getConsole().sendMessage(PluginInfo.PLUGIN_PREFIX.concat(Text.of(TextColors.BLUE, "Registering commands...")));
        registerCommands();

        Sponge.getServer().getConsole().sendMessage(PluginInfo.PLUGIN_PREFIX.concat(Text.of(TextColors.BLUE, "Registering listeners...")));
        registerListeners();
    }

    @Listener
    public void onPostInitialization(final GamePostInitializationEvent event)
    {
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX.concat(Text.of(TextColors.YELLOW, "Establishing connection with Eagle Factions..."))));

        //Load EagleFactions
        this.factionLogic = Sponge.getServiceManager().provide(FactionLogic.class).orElse(null);
        this.powerManager = Sponge.getServiceManager().provide(IPowerManager.class).orElse(null);

        if (this.factionLogic == null || this.powerManager == null)
        {
            Sponge.getServer().getConsole().sendMessage(PluginInfo.PLUGIN_ERROR.concat(Text.of("No Eagle Factions found!")));
            Sponge.getServer().getConsole().sendMessage(PluginInfo.PLUGIN_ERROR.concat(Text.of("Disabling the plugin...")));

            disablePlugin();
            return;
        }

        Sponge.getServer().getConsole().sendMessage(PluginInfo.PLUGIN_PREFIX.concat(Text.of(TextColors.GREEN, "Successfully established connection with Eagle Factions")));
    }

    private void registerCommands()
    {

    }

    private void registerListeners()
    {

    }

    private void disablePlugin()
    {

    }

//    public FactionLogic getFactionLogic()
//    {
//        return this.factionLogic;
//    }
//
//    public IPowerManager getPowerManager()
//    {
//        return this.powerManager;
//    }
}
