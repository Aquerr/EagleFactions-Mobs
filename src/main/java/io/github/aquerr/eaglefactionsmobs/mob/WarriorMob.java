package io.github.aquerr.eaglefactionsmobs.mob;

import com.flowpowered.math.vector.Vector3d;
import io.github.aquerr.eaglefactions.api.entities.Faction;
import io.github.aquerr.eaglefactionsmobs.ai.FollowPlayerTask;
import io.github.aquerr.eaglefactionsmobs.ai.TargetOwnerHurtByTaskTest;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.ai.GoalTypes;
import org.spongepowered.api.entity.ai.task.builtin.creature.AttackLivingAITask;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

public class WarriorMob
{
    private Human human;
    private Player player;

    public WarriorMob(final Faction faction, final Player player)
    {
        this.player = player;
        World world = player.getWorld();
        Vector3d position = player.getPosition();

        this.human = (Human)world.createEntity(EntityTypes.HUMAN, position);

        this.human.setChestplate(ItemStack.builder().itemType(ItemTypes.LEATHER_CHESTPLATE).build());
        this.human.setLeggings(ItemStack.builder().itemType(ItemTypes.LEATHER_LEGGINGS).build());
        this.human.setHelmet(ItemStack.builder().itemType(ItemTypes.LEATHER_HELMET).build());
        this.human.setBoots(ItemStack.builder().itemType(ItemTypes.LEATHER_BOOTS).build());
        this.human.offer(Keys.DISPLAY_NAME, Text.of(faction.getTag().getColor(), "[" + faction.getTag().toPlain() + "] Warrior"));
        this.human.offer(Keys.CUSTOM_NAME_VISIBLE, true);
//        this.human.offer(Keys.SKIN_UNIQUE_ID, UUID.fromString("53fb8065-d993-4d31-a16e-3512f2a70cf9"));
        this.human.offer(Keys.AI_ENABLED, true);

        initAITasks();
    }

    public void spawnMob()
    {
        this.player.getWorld().spawnEntity(this.human);
    }

    public Human getHuman()
    {
        return human;
    }

    private void initAITasks()
    {
        final AttackLivingAITask attackLivingAITask = AttackLivingAITask.builder()
                .longMemory()
                .speed(0.2)
                .build(this.human);

        final FollowPlayerTask followPlayerTask = new FollowPlayerTask(this.human, this.player);

        this.human.getGoal(GoalTypes.TARGET).get().clear();

        this.human.getGoal(GoalTypes.NORMAL).get().addTask(2, followPlayerTask);
        this.human.getGoal(GoalTypes.NORMAL).get().addTask(1, attackLivingAITask);

//        this.human.getGoal(GoalTypes)

        this.human.getGoal(GoalTypes.TARGET).get().addTask(1, TargetOwnerHurtByTaskTest.create(human, player));
    }

    public Player getOwner()
    {
        return this.player;
    }
}
