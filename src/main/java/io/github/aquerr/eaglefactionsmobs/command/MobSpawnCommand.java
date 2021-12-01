package io.github.aquerr.eaglefactionsmobs.command;

import io.github.aquerr.eaglefactions.api.entities.Faction;
import io.github.aquerr.eaglefactionsmobs.EagleFactionsMobs;
import io.github.aquerr.eaglefactionsmobs.mob.WarriorMob;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class MobSpawnCommand extends AbstractCommand
{
    public MobSpawnCommand(final EagleFactionsMobs eagleFactionsMobs)
    {
        super(eagleFactionsMobs);
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
    {
        if (!(source instanceof Player))
            throw new CommandException(Text.of("Only in-game players can use this command!"));

        final Player player = (Player)source;
        final Optional<Faction> optionalFaction = super.getPlugin().getFactionLogic().getFactionByPlayerUUID(player.getUniqueId());

        if (!optionalFaction.isPresent())
            throw new CommandException(Text.of("You must be in faction in order to use this command!"));

        final Faction faction = optionalFaction.get();
        final float playerPower = super.getPlugin().getPowerManager().getPlayerPower(player.getUniqueId());
        if (playerPower < 2.0f)
            throw new CommandException(Text.of("You do not have enough power to spawn a faction mob!"));

        WarriorMob warriorMob = new WarriorMob(faction, player);
        warriorMob.spawnMob();

        super.getPlugin().getPowerManager().setPlayerPower(player.getUniqueId(), playerPower - 2.0f);
        return CommandResult.success();
    }
}
