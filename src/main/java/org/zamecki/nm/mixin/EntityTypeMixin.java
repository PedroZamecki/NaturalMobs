package org.zamecki.nm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zamecki.nm.utils.INaturalEntityData;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
    @Unique
    double naturalScale = 0.0d;
    @Unique
    public double getNaturalScale() {
        return naturalScale == 0.0d ? naturalScale : Math.random() * 0.3 + 0.85;
    }

    @Inject(method = "create*", at = @At("RETURN"), cancellable = true)
    protected void createWithNaturalScale(CallbackInfoReturnable<Entity> callbackInfo) {
        Entity entity = callbackInfo.getReturnValue();
        if (entity instanceof MobEntity mobEntity) {
            // Determine the random and natural scale of the mob.

            EntityAttributeInstance entityAttributeInstance = mobEntity.getAttributeInstance(EntityAttributes.SCALE);
            if (entityAttributeInstance == null) {
                throw new RuntimeException("Entity does not have scale attribute");
            }
            naturalScale = entityAttributeInstance.getBaseValue();
            if (naturalScale != 1.0d) {
                return;
            }
            naturalScale = getNaturalScale();
            entityAttributeInstance.setBaseValue(naturalScale);

            // Write the random sex
            if (mobEntity instanceof AnimalEntity animal) {
                INaturalEntityData naturalEntityData = (INaturalEntityData) animal;
                naturalEntityData.naturalMobs$getNaturalData().putBoolean("sex", animal.getRandom().nextBoolean());
            }

            callbackInfo.setReturnValue(mobEntity);
        }
    }
}
