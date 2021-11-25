package su.nightexpress.anotherdailybonus.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserManager;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.data.object.BonusUser;

public class UserManager extends AbstractUserManager<AnotherDailyBonus, BonusUser> {

    public UserManager(@NotNull AnotherDailyBonus plugin) {
        super(plugin, plugin);
    }

    @Override
    @NotNull
    protected BonusUser createData(@NotNull Player player) {
        return new BonusUser(plugin, player);
    }
}
