package io.github.aquerr.eaglefactionsmobs.ai;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.api.entity.ai.task.AITask;
import org.spongepowered.api.entity.ai.task.AITaskTypes;
import org.spongepowered.api.entity.ai.task.AbstractAITask;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.player.Player;

public class FollowPlayerTask extends AbstractAITask<Human>
{
    private static final double MAX_DISTANCE_SQUARED = 20 * 20;
    private static final double MIN_DISTANCE_SQUARED = 3 * 3;
    private static final int MOVE_MUTEX_BIT = 3;
    private static final double MOVEMENT_SPEED = 0.5;

    Human factionMob;
    Player playerToFollow;
    EntityAIBase entityAIBase;
    private final PathNavigate pathFinder;
    private int timeToRecalcPath;

    public FollowPlayerTask(final Human factionMob, final Player playerToFollow)
    {
        super(AITaskTypes.WANDER);
        this.factionMob = factionMob;
        this.playerToFollow = playerToFollow;
        this.entityAIBase = ((EntityAIBase) (Object)this);
        this.entityAIBase.setMutexBits(MOVE_MUTEX_BIT);
        this.pathFinder = ((EntityLiving)factionMob).getNavigator();
    }

    @Override
    public void start()
    {
        this.timeToRecalcPath = 0;
    }

    @Override
    public boolean shouldUpdate()
    {
        if (this.playerToFollow == null)
        {
            return false;
        }
        final Vector3d position = this.playerToFollow.getPosition();
        return this.factionMob.getLocation().getPosition().distanceSquared(position) >= MIN_DISTANCE_SQUARED;
    }

    @Override
    public void update()
    {
        EntityLiving entityMob = ((EntityLiving)factionMob);
        Entity entityPlayer = (Entity)this.playerToFollow;
        entityMob.getLookHelper().setLookPosition(entityPlayer.posX, entityPlayer.posY + (double)entityPlayer.getEyeHeight(), entityPlayer.posZ, (float)entityMob.getHorizontalFaceSpeed(), (float)entityMob.getVerticalFaceSpeed());
//        entityMob.getLookHelper().setLookPositionWithEntity((Entity) this.playerToFollow, 10.0F, );
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.pathFinder.tryMoveToEntityLiving(entityPlayer, MOVEMENT_SPEED) && entityMob.getDistanceSq(entityPlayer) >= 144.0D) {
                int i = MathHelper.floor(entityPlayer.posX) - 2;
                int j = MathHelper.floor(entityPlayer.posZ) - 2;
                int k = MathHelper.floor(entityPlayer.getEntityBoundingBox().minY);
                entityMob.setLocationAndAngles((double)((float)(i) + 0.5F), (double)k, (double)((float)(j) + 0.5F), entityMob.rotationYaw, entityMob.rotationPitch);
                this.pathFinder.clearPath();
                return;
            }
        }
    }

    @Override
    public boolean continueUpdating()
    {
        if (!this.pathFinder.noPath())
            return false;

        return this.factionMob.getLocation().getPosition().distanceSquared(this.playerToFollow.getPosition()) > MAX_DISTANCE_SQUARED;
    }

    @Override
    public void reset()
    {
        this.pathFinder.clearPath();
    }

    @Override
    public boolean canRunConcurrentWith(AITask<Human> other)
    {
        return (this.entityAIBase.getMutexBits() & ((EntityAIBase)other).getMutexBits()) == 0;
    }

    @Override
    public boolean canBeInterrupted()
    {
        return true;
    }

//    protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
//        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
//        IBlockState iblockstate = this.world.getBlockState(blockpos);
//        return iblockstate.getBlockFaceShape(this.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.tameable) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
//    }
}
