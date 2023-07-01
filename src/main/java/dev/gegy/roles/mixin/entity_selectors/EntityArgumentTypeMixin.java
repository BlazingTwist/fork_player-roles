package dev.gegy.roles.mixin.entity_selectors;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.gegy.roles.PlayerRoles;
import dev.gegy.roles.api.PlayerRolesApi;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityArgumentType.class)
public class EntityArgumentTypeMixin {
    @WrapOperation(method = "listSuggestions", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/CommandSource;hasPermissionLevel(I)Z"))
    private boolean hasPermissionLevel(CommandSource source, int level, Operation<Boolean> original) {
        if (original.call(source, level)) {
            return true;
        }

        if (source instanceof ServerCommandSource serverSource) {
            var roles = PlayerRolesApi.lookup().bySource(serverSource);
            return roles.overrides().test(PlayerRoles.ENTITY_SELECTORS);
        }

        return false;
    }
}
