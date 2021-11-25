package su.nightexpress.anotherdailybonus.data.object;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUser;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BonusUser extends AbstractUser<AnotherDailyBonus> {

    private final Map<BonusType, BonusData> bonusData;

    public BonusUser(@NotNull AnotherDailyBonus plugin, @NotNull Player p) {
        this(plugin, p.getUniqueId(), p.getName(), System.currentTimeMillis(), new HashMap<>());
    }

    public BonusUser(@NotNull AnotherDailyBonus plugin, @NotNull UUID uuid, @NotNull String name, long lastOnline,
                     Map<BonusType, BonusData> bonusData) {
        super(plugin, uuid, name, lastOnline);
        this.bonusData = bonusData;
    }

    @NotNull
    public Map<BonusType, BonusData> getBonusData() {
        return bonusData;
    }

    @NotNull
    public BonusData getBonusData(@NotNull BonusType type) {
        return this.bonusData.computeIfAbsent(type, bonus -> new BonusData());
    }

    public void validateBonus() {
        for (BonusType bonusType : BonusType.values()) {
            if (bonusType.isEnabled()) {
                this.getBonusData(bonusType).validate();
            }
        }
    }
}
