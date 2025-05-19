package cc.sukazyo.nukos.carpet.pets.protect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ProtectEntityOf implements PetProtectionChecker {
	
	public static class Builder implements PetProtectionBuilder {
		public static final Builder INSTANCE = new Builder();
		@Override
		public Optional<PetProtectionChecker> fromConfig (String config) throws IllegalConfigException {
			
			String[] cfg = config.split("/");
			Identifier id = new Identifier(cfg[0]);
			
			if (Registries.ENTITY_TYPE.getOrEmpty(id).isEmpty()) {
				return Optional.empty();
			}
			
			if (cfg.length == 1) return Optional.of(new ProtectEntityOf(id));
			return Optional.of(switch (cfg[1]) {
				case "baby" -> new ProtectEntityOf(id, true);
				default -> throw new IllegalConfigException("%s is not a valid subtype for an entity.".formatted(cfg[1]));
			});
			
		}
	}
	
	private final Identifier id;
	private final boolean babyOnly;
	
	public ProtectEntityOf(final Identifier id) {
		this(id, false);
	}
	
	public ProtectEntityOf (final Identifier id, final boolean babyOnly) {
		this.id = id;
		this.babyOnly = babyOnly;
	}
	
	@Override
	public boolean shouldProtect (PlayerEntity damageSource, LivingEntity target) {
		
		if (Registries.ENTITY_TYPE.getId(target.getType()).equals(this.id)) {
			
			if (babyOnly) {
				return target.isBaby();
			}
			
			return true;
			
		}
		
		return false;
	}
	
}
