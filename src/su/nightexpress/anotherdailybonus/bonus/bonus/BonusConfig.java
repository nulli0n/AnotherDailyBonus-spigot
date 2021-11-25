package su.nightexpress.anotherdailybonus.bonus.bonus;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.AbstractLoadableItem;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.utils.StringUT;
import su.nexmedia.engine.utils.actions.ActionManipulator;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.api.IBonusConfig;
import su.nightexpress.anotherdailybonus.bonus.menu.BonusMenu;

import java.util.List;
import java.util.TreeMap;

public class BonusConfig extends AbstractLoadableItem<AnotherDailyBonus> implements IBonusConfig, ICleanable {

    private final BonusType type;

    private final boolean unlockAtNewCycle;
    private       long    unlockCustomTime;

    private final TreeMap<Integer, BonusReward> rewards;

    private BonusMenu bonusMenu;

    public BonusConfig(@NotNull AnotherDailyBonus plugin, @NotNull JYML cfg, @NotNull JYML cfgMenu,
                       @NotNull BonusType type) {
        super(plugin, cfg);
        this.type = type;

        if (!(this.unlockAtNewCycle = cfg.getBoolean("Settings.Cooldown.Unlock.At_New_Cycle"))) {
            this.unlockCustomTime = cfg.getLong("Settings.Cooldown.Unlock.Custom_Time") * 1000L;
        }

        this.rewards = new TreeMap<>();
        for (String id : cfg.getSection("Rewards")) {
            String path2 = "Rewards." + id + ".";

            List<String> description = StringUT.color(cfg.getStringList(path2 + "Description"));
            ActionManipulator rewardActions = new ActionManipulator(plugin, cfg, path2 + "Rewards.Custom_Actions");

            BonusReward reward = new BonusReward(description, rewardActions);
            this.rewards.put(this.rewards.size() + 1, reward);
        }

        this.bonusMenu = new BonusMenu(plugin, cfgMenu, this);
    }

    @Override
    public void onSave() {

    }

    @Override
    public void clear() {
        if (this.bonusMenu != null) {
            this.bonusMenu.clear();
            this.bonusMenu = null;
        }
    }

    @Override
    @NotNull
    public BonusType getType() {
        return type;
    }

    @Override
    public boolean isUnlockAtNewCycle() {
        return unlockAtNewCycle;
    }

    @Override
    public long getUnlockCustomTime() {
        return unlockCustomTime;
    }

    @Override
    @NotNull
    public TreeMap<Integer, BonusReward> getRewardsMap() {
        return rewards;
    }

    @Override
    public void openMenu(@NotNull Player player) {
        if (!this.getType().hasPermission(player)) {
            plugin.lang().Error_NoPerm.send(player);
            return;
        }
        this.bonusMenu.open(player, 1);
    }
}
