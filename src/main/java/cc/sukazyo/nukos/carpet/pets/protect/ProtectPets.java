package cc.sukazyo.nukos.carpet.pets.protect;

import cc.sukazyo.nukos.carpet.CarpetAdditionNukos;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.AbstractTeam;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static cc.sukazyo.nukos.carpet.ModCarpetNukos.LOGGER;

public class ProtectPets implements PetProtectionChecker {
	
	public static class Builder implements PetProtectionBuilder {
		public static final Builder INSTANCE = new Builder();
		@Override
		public Optional<PetProtectionChecker> fromConfig (String config) throws IllegalConfigException {
			if (config.startsWith("pets")) {
				return Optional.of( switch (config) {
					case "pets" -> new ProtectPets();
					case "pets/owned" -> new ProtectPets(true, false);
					case "pets/team" -> new ProtectPets(false, true);
					default -> throw new IllegalConfigException("%s is not a valid config for pets superset.".formatted(config));
				});
			} else {
				return Optional.empty();
			}
		}
	}
	
	private final boolean onlyOwner;
	private final boolean onlyTeam;
	
	public ProtectPets () {
		this(false, false);
	}
	
	public ProtectPets (boolean onlyOwner, boolean onlyTeam) {
		this.onlyOwner = onlyOwner;
		this.onlyTeam = onlyTeam;
	}
	
	@Override
	public boolean shouldProtect (PlayerEntity damageSource, LivingEntity target) {
		
		if (target instanceof Tameable pet) {
			
			UUID ownerUUID = pet.getOwnerUuid();
			
			if (ownerUUID != null) {
				if (onlyOwner || onlyTeam) {
					if (ownerUUID.equals(pet.getOwnerUuid())) return true;
					if (onlyTeam) {
						try {
							AbstractTeam team = target.getScoreboardTeam();
							Collection<String> teammates = team.getPlayerList();
							GameProfile ownerProfile = CarpetAdditionNukos.SERVER.getUserCache().getByUuid(ownerUUID).get();
							String ownerName = ownerProfile.getName();
							return teammates.contains(ownerName);
						} catch (Exception e) {
							LOGGER.warn("Error get pet owner info:", e);
						}
					}
					return false;
				}
				return true;
			}
			
		}
		
		return false;
		
	}
	
}
