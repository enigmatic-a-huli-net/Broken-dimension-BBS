    package net.mcreator.brokendimensionbbs.block;

import net.mcreator.brokendimensionbbs.procedures.BrokendimensionportalPriStolknovieniiSushchnostiSBlokomProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrokendimensionportalBlock extends Block {
   public BrokendimensionportalBlock() {
      super(Properties.of().sound(SoundType.AMETHYST).strength(-1.0F, 3600000.0F).lightLevel((s) -> 15).noCollission().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
   }

   public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
      return 15;
   }

   public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return Shapes.empty();
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return box((double)0.0F, (double)6.0F, (double)0.0F, (double)16.0F, (double)10.0F, (double)16.0F);
   }

   public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
      return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
   }

   public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {
      super.entityInside(blockstate, world, pos, entity);
      BrokendimensionportalPriStolknovieniiSushchnostiSBlokomProcedure.execute(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), entity);
   }

   public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
      super.animateTick(state, world, pos, random);
      if (this.isSoundAnchor(world, pos)) {
         if (random.nextInt(260) == 0) {
            world.playLocalSound((double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "dimension_portal")), SoundSource.BLOCKS, 0.65F, 0.85F + random.nextFloat() * 0.3F, false);
         }
      }
   }

   private boolean isSoundAnchor(Level world, BlockPos pos) {
      return !world.getBlockState(pos.below()).is(this) && !world.getBlockState(pos.north()).is(this) && !world.getBlockState(pos.west()).is(this);
   }
}
 