package io.github.aquerr.eaglefactionsmobs.command;

import com.flowpowered.math.vector.Vector3d;
import io.github.aquerr.eaglefactions.api.entities.Faction;
import io.github.aquerr.eaglefactionsmobs.EagleFactionsMobs;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.ai.GoalTypes;
import org.spongepowered.api.entity.ai.task.AITask;
import org.spongepowered.api.entity.ai.task.AITaskTypes;
import org.spongepowered.api.entity.ai.task.AbstractAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.AttackLivingAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.AvoidEntityAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.WanderAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.target.FindNearestAttackableTargetAITask;
import org.spongepowered.api.entity.living.Agent;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.monster.*;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

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

        final World world = player.getWorld();

        final Human entity = (Human) world.createEntity(EntityTypes.HUMAN, player.getPosition());
        entity.setChestplate(ItemStack.builder().itemType(ItemTypes.LEATHER_CHESTPLATE).build());
        entity.setLeggings(ItemStack.builder().itemType(ItemTypes.LEATHER_LEGGINGS).build());
        entity.setHelmet(ItemStack.builder().itemType(ItemTypes.LEATHER_HELMET).build());
        entity.setBoots(ItemStack.builder().itemType(ItemTypes.LEATHER_BOOTS).build());
        entity.offer(Keys.DISPLAY_NAME, Text.of(TextColors.BLUE, "Warrior"));
        entity.offer(Keys.CUSTOM_NAME_VISIBLE, true);
        entity.offer(Keys.SKIN_UNIQUE_ID, UUID.fromString("53fb8065-d993-4d31-a16e-3512f2a70cf9"));
        entity.offer(Keys.AI_ENABLED, true);

        final WanderAITask wanderAITask = WanderAITask.builder()
                .executionChance(10)
                .speed(0.2)
                .build(entity);

        final AttackLivingAITask attackLivingAITask = AttackLivingAITask.builder()
                .longMemory()
                .speed(0.2)
                .build(entity);

        final FollowPlayerTask followPlayerTask = new FollowPlayerTask(player);

        final AvoidEntityAITask avoidEntityAITask = AvoidEntityAITask
                .builder()
                .closeRangeSpeed(1)
                .farRangeSpeed(2)
                .searchDistance(10)
                .targetSelector((x) -> x instanceof Player)
                .build(entity);
//        entity.getGoal(GoalTypes.NORMAL).get().addTask(0, avoidEntityAITask);
//        entity.getGoal(GoalTypes.NORMAL).get().addTask(2, followPlayerTask);
        entity.getGoal(GoalTypes.NORMAL).get().addTask(2, wanderAITask);
        entity.getGoal(GoalTypes.NORMAL).get().addTask(1, attackLivingAITask);
        entity.getGoal(GoalTypes.TARGET).get().clear();

        final FindNearestAttackableTargetAITask findNearestAttackableTargetAITask = FindNearestAttackableTargetAITask.builder()
                .chance(1)
                .filter(x -> {
                    if (x instanceof Skeleton)
                        return true;
                    else if (x instanceof Zombie)
                        return true;
                    else if(x instanceof Slime)
                        return true;
                    else return x instanceof Spider;
                })
                .target(Monster.class)
                .build(entity);

        entity.getGoal(GoalTypes.TARGET).get().addTask(0, findNearestAttackableTargetAITask);
//        entity.getGoal(GoalTypes.NORMAL).get).addTask(0, new MoveSillyToPlayerAITask(player));
        world.spawnEntity(entity);

        super.getPlugin().getPowerManager().setPower(player.getUniqueId(), playerPower - 2.0f);
        return CommandResult.success();
    }

    private static class FollowPlayerTask extends AbstractAITask<Agent> {
        final Player player;

        protected FollowPlayerTask(Player player) {
            super(AITaskTypes.WANDER);
            this.player = player;
        }

        @Override
        public void start() {
        }

        @Override
        public boolean shouldUpdate() {
            return getOwner().isPresent();
        }

        @Override
        public void update() {
            //            Agent agent = this.getOwner().orElse(null);
//            if (agent != null) {
//                agent.setRotation(new Vector3d(player.getRotation().getX() * 0.5f, player.getRotation().getY(), player.getRotation().getZ()));
//                Vector3d position = player.getLocation().getPosition().add(new Vector3d(1f, 0f, 1f));
//                agent.setTransform(new Transform<>(new Location<>(agent.getWorld(), position.getX(), agent.getLocation().getY(), position.getZ())));
//            }

            Agent agent = this.getOwner().orElse(null);
            if (agent != null) {
                agent.setRotation(new Vector3d(player.getRotation().getX(), player.getRotation().getY(), player.getRotation().getZ()));
                Vector3d position = player.getLocation().getPosition().add(new Vector3d(1f, 0f, 1f));
                agent.setTransform(new Transform<>(new Location<>(agent.getWorld(), position.getX(), player.getLocation().getY(), position.getZ())));
            }
        }

        @Override
        public boolean continueUpdating() {
            return true;
        }

        @Override
        public void reset() {
        }

        @Override
        public boolean canRunConcurrentWith(AITask<Agent> other) {
            return false;
        }

        @Override
        public boolean canBeInterrupted() {
            return false;
        }
    }
}
