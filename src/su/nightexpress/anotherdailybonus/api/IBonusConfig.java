package su.nightexpress.anotherdailybonus.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.ConfigHolder;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusReward;

import java.util.TreeMap;

public interface IBonusConfig extends ConfigHolder {

    @NotNull BonusType getType();

    boolean isUnlockAtNewCycle();

    long getUnlockCustomTime();

    @NotNull TreeMap<Integer, BonusReward> getRewardsMap();

    void openMenu(@NotNull Player player);
}
