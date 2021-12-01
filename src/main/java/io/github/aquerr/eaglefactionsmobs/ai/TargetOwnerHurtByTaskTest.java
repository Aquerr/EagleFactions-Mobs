package io.github.aquerr.eaglefactionsmobs.ai;

import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.entity.ai.task.builtin.creature.target.FindNearestAttackableTargetAITask;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;

import java.util.function.Predicate;

public class TargetOwnerHurtByTaskTest
{
    public static FindNearestAttackableTargetAITask create(Human mob, final Player player)
    {
        return FindNearestAttackableTargetAITask.builder()
                .target(Living.class)
                .chance(100)
                .filter(findHurtByTargetPredicate((EntityPlayerMP)player))
                .build(mob);
    }

    private static Predicate<Living> findHurtByTargetPredicate(EntityPlayerMP player)
    {
        return living -> {
            if(living.equals(player.getRevengeTarget()) || living.equals(player.getAttackingEntity()))
            {
                System.out.println("Player has revenge target or attacking entity");
                return true;
            }
            else return false;
        };
    }
}
