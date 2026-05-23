package net.mcreator.brokendimensionbbs.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.Collections;

public class BrokendimensionportalPriStolknovieniiSushchnostiSBlokomProcedure {

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null || world.isClientSide())
			return;

		if (entity.isPassenger()) {
	        return;
        }

		if (!(world instanceof ServerLevel currentLevel))
			return;

		ResourceKey<Level> brokenDimension = ResourceKey.create(
				Registries.DIMENSION,
				ResourceLocation.fromNamespaceAndPath("broken_dimension", "broken_dimension")
		);

		ServerLevel targetLevel;

		if (currentLevel.dimension().equals(brokenDimension)) {
			targetLevel = currentLevel.getServer().getLevel(Level.OVERWORLD);
		} else {
			targetLevel = currentLevel.getServer().getLevel(brokenDimension);
		}

		if (targetLevel == null)
			return;

		BlockPos origin = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());

		BlockPos safePos = findPortalBeacon(targetLevel, origin, 64);

		if (safePos == null) {
			safePos = findSafePosition(targetLevel, origin);
		}

				entity.teleportTo(
				targetLevel,
				safePos.getX() + 0.5,
				safePos.getY(),
				safePos.getZ() + 0.5,
				java.util.Collections.emptySet(),
				entity.getYRot(),
				entity.getXRot()
        );

		targetLevel.playSound(
				null,
				safePos,
				BuiltInRegistries.SOUND_EVENT.get(
						ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "dimension_travel")
				),
				SoundSource.AMBIENT,
				1.0F,
				1.0F
		);
	}

	private static BlockPos findPortalBeacon(ServerLevel level, BlockPos origin, int radius) {
		ResourceLocation beaconId = ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "portal_beacon");

		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
					BlockPos pos = new BlockPos(origin.getX() + dx, y, origin.getZ() + dz);

					ResourceLocation blockId = level.registryAccess()
							.registryOrThrow(Registries.BLOCK)
							.getKey(level.getBlockState(pos).getBlock());

					if (beaconId.equals(blockId)) {
						BlockPos feet = pos.above();
						BlockPos head = feet.above();

						if (level.getBlockState(feet).isAir()
								&& level.getBlockState(head).isAir()) {
							return feet;
						}
					}
				}
			}
		}

		return null;
	}

	private static BlockPos findSafePosition(ServerLevel level, BlockPos origin) {
		int radius = 64;

		for (int r = 0; r <= radius; r++) {
			for (int dx = -r; dx <= r; dx++) {
				for (int dz = -r; dz <= r; dz++) {
					int px = origin.getX() + dx;
					int pz = origin.getZ() + dz;

					for (int py = level.getMaxBuildHeight() - 3; py > level.getMinBuildHeight(); py--) {
						BlockPos feet = new BlockPos(px, py, pz);
						BlockPos head = feet.above();
						BlockPos ground = feet.below();

						if (level.getBlockState(ground).isSolidRender(level, ground)
								&& level.getBlockState(feet).isAir()
								&& level.getBlockState(head).isAir()) {
							return feet;
						}
					}
				}
			}
		}

		return new BlockPos(origin.getX(), 150, origin.getZ());
	}
}
