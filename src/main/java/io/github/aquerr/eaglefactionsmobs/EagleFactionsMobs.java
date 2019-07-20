package io.github.aquerr.eaglefactionsmobs;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION, authors = {"Aquerr/Nerdi"},
dependencies = {@Dependency(id = "eaglefactions")})
public class EagleFactionsMobs
{

    private final CommandManager commandManager;
    private final EventManager eventManager;
    private final Path configPath;

    @Inject
    private EagleFactionsMobs(final CommandManager commandManager, final EventManager eventManager, final @ConfigDir(sharedRoot = false) Path configPath)
    {
        this.commandManager = commandManager;
        this.eventManager = eventManager;
        this.configPath = configPath;
    }


    @Listener
    public void onInitialization(final GameInitializationEvent event)
    {

    }

    private void registerCommands()
    {

    }

    private void registerListeners()
    {

    }
}
