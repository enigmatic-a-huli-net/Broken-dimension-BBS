package net.mcreator.brokendimensionbbs.procedures;
//openTicks++
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;

public class BrokenPortalController {
	private static final int OPEN_DELAY = 5;
	private static final int CLOSE_DELAY = 5;

	private static final ResourceLocation PORTAL_BLOCK_ID =
			ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "brokendimensionportal");

	private static final Map<String, PortalState> STATES = new HashMap<>();

	public static void tick(ServerLevel level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		Direction facing = getFacing(state).getOpposite();

		if (!isAreaLoaded(level, pos, facing)) {
			return;
		}

		String key = getKey(level, pos);
		PortalState portalState = STATES.computeIfAbsent(key, k -> new PortalState());

		boolean valid = isValidFrame(level, pos, facing);
		boolean portalExists = portalExists(level, pos, facing);

		if (portalExists) {
			portalState.open = true;
		}

		if (!portalState.open) {
			if (valid) {
				if (!portalState.triggerPlayed) {
					playSound(level, pos, "dimension_trigger1");
					portalState.triggerPlayed = true;
				}

				portalState.openTicks++;
				spawnChargingParticles(level, pos, facing);

				if (portalState.openTicks >= OPEN_DELAY) {
	                createPortal(level, pos, facing);
	                playSound(level, pos, "dimension_trigger2");
	                spawnSculkParticles(level, pos, facing);
					portalState.open = true;
					portalState.openTicks = 0;
					portalState.closeTicks = 0;
					portalState.triggerPlayed = false;
				}
			} else {
				portalState.openTicks = 0;
				portalState.triggerPlayed = false;
			}

			return;
		}

		if (valid) {
			portalState.closeTicks = 0;
			return;
		}

		portalState.closeTicks++;

		if (portalState.closeTicks >= CLOSE_DELAY) {
			removePortal(level, pos, facing);
			portalState.open = false;
			portalState.openTicks = 0;
			portalState.closeTicks = 0;
			portalState.triggerPlayed = false;
		}
	}
	
	private static void spawnChargingParticles(ServerLevel level, BlockPos pos, Direction facing) {
	BlockPos center = local(pos, facing, 0, 0, 3);

	level.sendParticles(
			net.minecraft.core.particles.ParticleTypes.SCULK_SOUL,
			center.getX() + 0.5,
			center.getY() + 0.25,
			center.getZ() + 0.5,
			8,
			0.8,
			0.05,
			0.8,
			0.02
	);
}

    private static void playSound(ServerLevel level, BlockPos pos, String soundName) {
    	level.playSound(
    			null,
    			pos,
    			BuiltInRegistries.SOUND_EVENT.get(
    					ResourceLocation.fromNamespaceAndPath(
    							"broken_dimension_bbs",
    							soundName
    					)
    			),
    			SoundSource.BLOCKS,
    			1.0F,
    			1.0F
    	);
    }
	
    	private static void spawnSculkParticles(ServerLevel level, BlockPos pos, Direction facing) {
    	for (BlockPos portalPos : getPortalInterior(pos, facing)) {
    		level.sendParticles(
    				net.minecraft.core.particles.ParticleTypes.SCULK_SOUL,
    				portalPos.getX() + 0.5,
    				portalPos.getY() + 0.25,
    				portalPos.getZ() + 0.5,
    				60,
    				0.35,
    				0.08,
    				0.35,
    				0.02
    		);
    	}
    }

	public static void closePortal(ServerLevel level, BlockPos pos, Direction facing) {
		removePortal(level, pos, facing);
		STATES.remove(getKey(level, pos));
	}

	public static void closePortal(ServerLevel level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		Direction facing = getFacing(state).getOpposite();
		closePortal(level, pos, facing);
	}

	private static boolean isValidFrame(ServerLevel level, BlockPos pos, Direction facing) {
		Block portalBlock = getBlock(level, PORTAL_BLOCK_ID);

		if (portalBlock == null) {
			return false;
		}

		for (int[] offset : FRAME_OFFSETS) {
			BlockPos checkPos = local(pos, facing, offset[0], offset[1], offset[2]);

			if (level.getBlockState(checkPos).getBlock() != Blocks.WAXED_COPPER_BLOCK) {
				return false;
			}
		}

		if (!checkOpticalSources(level, pos, facing)) {
			return false;
		}

		for (BlockPos insidePos : getPortalInterior(pos, facing)) {
			BlockState insideState = level.getBlockState(insidePos);

			if (!insideState.isAir() && insideState.getBlock() != portalBlock) {
				return false;
			}
		}

		return true;
	}

	private static void createPortal(ServerLevel level, BlockPos pos, Direction facing) {
		Block portalBlock = getBlock(level, PORTAL_BLOCK_ID);

		if (portalBlock == null) {
			return;
		}

		for (BlockPos portalPos : getPortalInterior(pos, facing)) {
			if (level.getBlockState(portalPos).isAir()) {
				level.setBlockAndUpdate(portalPos, portalBlock.defaultBlockState());
			}
		}
	}

	private static void removePortal(ServerLevel level, BlockPos pos, Direction facing) {
		Block portalBlock = getBlock(level, PORTAL_BLOCK_ID);

		if (portalBlock == null) {
			return;
		}

		for (BlockPos portalPos : getPortalInterior(pos, facing)) {
			if (level.getBlockState(portalPos).getBlock() == portalBlock) {
				level.setBlockAndUpdate(portalPos, Blocks.AIR.defaultBlockState());
			}
		}
	}

	private static boolean portalExists(ServerLevel level, BlockPos pos, Direction facing) {
		Block portalBlock = getBlock(level, PORTAL_BLOCK_ID);

		if (portalBlock == null) {
			return false;
		}

		for (BlockPos portalPos : getPortalInterior(pos, facing)) {
			if (level.getBlockState(portalPos).getBlock() == portalBlock) {
				return true;
			}
		}

		return false;
	}

	private static boolean isAreaLoaded(ServerLevel level, BlockPos pos, Direction facing) {
		for (BlockPos checkPos : getAllCheckPositions(pos, facing)) {
			if (!level.hasChunkAt(checkPos)) {
				return false;
			}
		}

		return true;
	}

	private static BlockPos[] getAllCheckPositions(BlockPos pos, Direction facing) {
		java.util.ArrayList<BlockPos> result = new java.util.ArrayList<>();

		for (int[] offset : FRAME_OFFSETS) {
			addUnique(result, local(pos, facing, offset[0], offset[1], offset[2]));
		}

		for (BlockPos portalPos : getPortalInterior(pos, facing)) {
			addUnique(result, portalPos);
		}

		addUnique(result, local(pos, facing, -3, 0, 3));
		addUnique(result, local(pos, facing, 3, 0, 3));

		return result.toArray(new BlockPos[0]);
	}

	private static BlockPos[] getPortalInterior(BlockPos pos, Direction facing) {
		java.util.ArrayList<BlockPos> result = new java.util.ArrayList<>();

		addArea(result, pos, facing, -1, 0, 1, 1, 0, 5);
		addArea(result, pos, facing, -2, 0, 2, 2, 0, 4);

		return result.toArray(new BlockPos[0]);
	}

	private static void addArea(java.util.ArrayList<BlockPos> result, BlockPos center, Direction facing,
			int x1, int y1, int z1, int x2, int y2, int z2) {
		for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
			for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
				for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
					addUnique(result, local(center, facing, x, y, z));
				}
			}
		}
	}

	private static void addUnique(java.util.ArrayList<BlockPos> result, BlockPos pos) {
		if (!result.contains(pos)) {
			result.add(pos);
		}
	}

	private static BlockPos local(BlockPos center, Direction forward, int localX, int localY, int localZ) {
		Direction right = forward.getClockWise();

		return center
				.relative(right, localX)
				.relative(Direction.UP, localY)
				.relative(forward, localZ);
	}

	private static Direction getFacing(BlockState state) {
		if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
			return state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		}

		return Direction.NORTH;
	}

	private static Block getBlock(ServerLevel level, ResourceLocation id) {
		return level.registryAccess()
				.registryOrThrow(Registries.BLOCK)
				.get(id);
	}

	private static String getKey(ServerLevel level, BlockPos pos) {
		return level.dimension().location() + ":" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
	}

	private static boolean checkOpticalSources(ServerLevel level, BlockPos catalystPos, Direction facing) {
		BlockPos leftSourcePos = local(catalystPos, facing, -3, 0, 3);
		BlockPos rightSourcePos = local(catalystPos, facing, 3, 0, 3);

		Direction right = facing.getClockWise();
		Direction left = right.getOpposite();

		if (!isBlock(level, leftSourcePos, "create_optical", "thermal_optical_source")) {
			return false;
		}

		if (!isBlock(level, rightSourcePos, "create_optical", "thermal_optical_source")) {
			return false;
		}

		BlockState leftState = level.getBlockState(leftSourcePos);
		BlockState rightState = level.getBlockState(rightSourcePos);

		if (getFacing(leftState) != right) {
			return false;
		}

		if (getFacing(rightState) != left) {
			return false;
		}

		if (getKineticSpeed(level, leftSourcePos) < 222) {
			return false;
		}

		if (getKineticSpeed(level, rightSourcePos) < 222) {
			return false;
		}

		if (getLavaAmount(level, leftSourcePos) < 500) {
			return false;
		}

		if (getLavaAmount(level, rightSourcePos) < 500) {
			return false;
		}

		return true;
	}

	private static boolean isBlock(ServerLevel level, BlockPos pos, String namespace, String path) {
		ResourceLocation targetId = ResourceLocation.fromNamespaceAndPath(namespace, path);

		ResourceLocation blockId = level.registryAccess()
				.registryOrThrow(Registries.BLOCK)
				.getKey(level.getBlockState(pos).getBlock());

		return targetId.equals(blockId);
	}

	private static float getKineticSpeed(ServerLevel level, BlockPos pos) {
		net.minecraft.world.level.block.entity.BlockEntity blockEntity = level.getBlockEntity(pos);

		if (blockEntity == null) {
			return 0;
		}

		try {
			java.lang.reflect.Method method = blockEntity.getClass().getMethod("getSpeed");
			Object result = method.invoke(blockEntity);

			if (result instanceof Number number) {
				return Math.abs(number.floatValue());
			}
		} catch (Exception ignored) {
		}

		return 0;
	}

	private static int getLavaAmount(ServerLevel level, BlockPos pos) {
		var fluidHandler = level.getCapability(
				net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK,
				pos,
				null
		);

		if (fluidHandler == null) {
			return 0;
		}

		int amount = 0;

		for (int i = 0; i < fluidHandler.getTanks(); i++) {
			var stack = fluidHandler.getFluidInTank(i);

			if (stack.getFluid().is(net.minecraft.tags.FluidTags.LAVA)) {
				amount += stack.getAmount();
			}
		}

		return amount;
	}

	private static class PortalState {
		boolean open = false;
		boolean triggerPlayed = false;
		int openTicks = 0;
		int closeTicks = 0;
	}

	private static final int[][] FRAME_OFFSETS = new int[][] {
			{-1, 0, 0},
			{1, 0, 0},

			{-2, 0, 1},
			{2, 0, 1},

			{-3, 0, 2},
			{3, 0, 2},

			{-3, 0, 4},
			{3, 0, 4},

			{-2, 0, 5},
			{2, 0, 5},

			{-1, 0, 6},
			{0, 0, 6},
			{1, 0, 6}
	};
}
