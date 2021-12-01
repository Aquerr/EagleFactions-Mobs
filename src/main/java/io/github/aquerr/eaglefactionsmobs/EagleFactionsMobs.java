package io.github.aquerr.eaglefactionsmobs;

import com.google.inject.Inject;
import io.github.aquerr.eaglefactions.api.EagleFactions;
import io.github.aquerr.eaglefactions.api.logic.FactionLogic;
import io.github.aquerr.eaglefactions.api.managers.PowerManager;
import io.github.aquerr.eaglefactionsmobs.command.MobSpawnCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;
import java.util.*;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION, authors = {"Aquerr/Nerdi"},
dependencies = {@Dependency(id = "eaglefactions")})
public class EagleFactionsMobs
{
    private final CommandManager commandManager;
    private final EventManager eventManager;
    private final Path configPath;

    private final Map<List<String>, CommandSpec> subcommands = new HashMap<>();

    private EagleFactions eagleFactions;
    private FactionLogic factionLogic;
    private PowerManager powerManager;

    @Inject
    private EagleFactionsMobs(final CommandManager commandManager, final EventManager eventManager, final @ConfigDir(sharedRoot = false) Path configPath) {
        this.commandManager = commandManager;
        this.eventManager = eventManager;
        this.configPath = configPath;
    }

    public FactionLogic getFactionLogic() {
        return this.factionLogic;
    }

    public PowerManager getPowerManager() {
        return this.powerManager;
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

        Optional<?> optionalEagleFactionsInstance = Sponge.getPluginManager().getPlugin("eaglefactions").get().getInstance();
        if(optionalEagleFactionsInstance.isPresent())
        {
            this.eagleFactions = (EagleFactions) optionalEagleFactionsInstance.get();
        }

        //Load EagleFactions
        this.factionLogic = Sponge.getServiceManager().provide(FactionLogic.class).orElse(null);
        this.powerManager = Sponge.getServiceManager().provide(PowerManager.class).orElse(null);

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
        this.subcommands.put(Arrays.asList("mob"), CommandSpec.builder()
                .description(Text.of("Spawns a faction mob"))
                .permission(PluginPermissions.MOB_SPAWN_COMMAND)
                .executor(new MobSpawnCommand(this))
                .build());

        //TODO: To register our commands under "/f", Eagle Factions need to provide us such functionality. An command register event or method.
//        final CommandMapping eagleFactionsCommandMapping = this.commandManager.get("f").orElse(null);

//        if (eagleFactionsCommandMapping == null)
//        {
//            Sponge.getServer().getConsole().sendMessage(PluginInfo.PLUGIN_ERROR.concat(Text.of("Could not get Eagle Factions root command thus no commands were registered.")));
//            return;
//        }

        //Reregister eaglefactions command
        final CommandSpec eagleFactionsMobsCommand = CommandSpec.builder()
                .children(this.subcommands)
                .build();

        this.commandManager.register(this, eagleFactionsMobsCommand, "fm", "factionsmobs", "factionmobs");
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
