package io.github.aquerr.eaglefactionsmobs.command;

import io.github.aquerr.eaglefactionsmobs.EagleFactionsMobs;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public abstract class AbstractCommand implements CommandExecutor
{
    private final EagleFactionsMobs plugin;

    public AbstractCommand(final EagleFactionsMobs plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public abstract CommandResult execute(CommandSource source, CommandContext args) throws CommandException;

    protected EagleFactionsMobs getPlugin()
    {
        return this.getPlugin();
    }
}
