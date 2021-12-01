//package io.github.aquerr.eaglefactionsmobs.ai;
//
//import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.ai.EntityAIBase;
//import org.spongepowered.api.entity.ai.task.AITask;
//import org.spongepowered.api.entity.ai.task.AITaskTypes;
//import org.spongepowered.api.entity.ai.task.AbstractAITask;
//import org.spongepowered.api.entity.living.Human;
//import org.spongepowered.api.entity.living.player.Player;
//
//public class TargetOwnerHurtByTask extends AbstractAITask<Human>
//{
//    EntityAIBase entityAIBase;
//    EntityLiving factionMob;
//    EntityLivingBase attacker;
//    EntityLiving owner;
//    private int timestamp;
//
//    public TargetOwnerHurtByTask(final Human factionMob, final Player player)
//    {
//        super(AITaskTypes.WATCH_CLOSEST);
//        this.factionMob = (EntityLiving) factionMob;
//        this.owner = (EntityLiving)player;
//        this.entityAIBase = ((EntityAIBase) (Object)this);
//        this.entityAIBase.setMutexBits(1);
//    }
//
//    @Override
//    public void start()
//    {
//        this.factionMob.setAttackTarget(this.attacker);
//
//        if (owner != null)
//        {
//            this.timestamp = owner.getRevengeTimer();
//        }
//
////        this.targetSearchStatus = 0;
////        this.targetSearchDelay = 0;
////        this.targetUnseenTicks = 0;
//    }
//
//    @Override
//    public boolean shouldUpdate()
//    {
//        if (owner == null)
//        {
//            return false;
//        }
//        else
//        {
//            this.attacker = owner.getRevengeTarget();
//            int i = owner.getRevengeTimer();
//            return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entitylivingbase);
//        }
//    }
//
//    @Override
//    public void update()
//    {
//
//    }
//
//    @Override
//    public boolean continueUpdating()
//    {
//        return false;
//    }
//
//    @Override
//    public void reset()
//    {
//
//    }
//
//    @Override
//    public boolean canRunConcurrentWith(AITask<Human> other)
//    {
//        return (this.entityAIBase.getMutexBits() & ((EntityAIBase)other).getMutexBits()) == 0;
//    }
//
//    @Override
//    public boolean canBeInterrupted()
//    {
//        return true;
//    }
//
////    public EntityAIOwnerHurtByTarget(EntityTameable theDefendingTameableIn)
////    {
////        super(theDefendingTameableIn, false);
////        this.tameable = theDefendingTameableIn;
////        this.setMutexBits(1);
////    }
////
////    /**
////     * Returns whether the EntityAIBase should begin execution.
////     */
////    public boolean shouldExecute()
////    {
////        if (!this.tameable.isTamed())
////        {
////            return false;
////        }
////        else
////        {
////            EntityLivingBase entitylivingbase = this.tameable.getOwner();
////
////            if (entitylivingbase == null)
////            {
////                return false;
////            }
////            else
////            {
////                this.attacker = entitylivingbase.getRevengeTarget();
////                int i = entitylivingbase.getRevengeTimer();
////                return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entitylivingbase);
////            }
////        }
////    }
////
////    /**
////     * Execute a one shot task or start executing a continuous task
////     */
////    public void startExecuting()
////    {
////        this.taskOwner.setAttackTarget(this.attacker);
////        EntityLivingBase entitylivingbase = this.tameable.getOwner();
////
////        if (entitylivingbase != null)
////        {
////            this.timestamp = entitylivingbase.getRevengeTimer();
////        }
////
////        super.startExecuting();
////    }
//}
