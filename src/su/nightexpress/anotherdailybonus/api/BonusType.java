package su.nightexpress.anotherdailybonus.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.Constants;
import su.nightexpress.anotherdailybonus.Perms;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

public enum BonusType {

    DAILY, WEEKLY, MONTHLY,;

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean hasPermission(@NotNull Player player) {
        return player.hasPermission(Perms.BONUS_TYPE + this.name().toLowerCase())
                || player.hasPermission(Perms.BONUS_TYPE + Constants.MASK_ANY);
    }

    @NotNull
    public static BonusType[] getEnabled() {
        return Stream.of(BonusType.values()).filter(BonusType::isEnabled).toList().toArray(new BonusType[0]);
    }

    public long getNewCycleDate() {
        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime dateCycle;

        switch (this) {
            case DAILY -> dateCycle = dateNow.plusDays(1);
            case WEEKLY -> dateCycle = dateNow.plusDays(7);
            case MONTHLY -> dateCycle = dateNow.withDayOfMonth(1).plusMonths(1);
            default -> {
                return 0L;
            }
        }

        Instant instant = dateCycle.withHour(0).withMinute(0).withSecond(0).atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }
}
