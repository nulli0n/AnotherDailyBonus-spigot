package su.nightexpress.anotherdailybonus.bonus.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.*;
import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.utils.ItemUT;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.IBonusReward;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusConfig;
import su.nightexpress.anotherdailybonus.data.object.BonusUser;
import su.nightexpress.anotherdailybonus.data.object.BonusData;

import java.util.HashMap;
import java.util.Map;

public class BonusMenu extends AbstractMenu<AnotherDailyBonus> {

    private final BonusConfig bonusConfig;

    private final int[]                      rewardSlots;
    private final Map<RewardType, ItemStack> rewardFormat;

    public BonusMenu(@NotNull AnotherDailyBonus plugin, @NotNull JYML cfg, @NotNull BonusConfig bonusConfig) {
        super(plugin, cfg, "");
        this.bonusConfig = bonusConfig;

        this.rewardSlots = cfg.getIntArray("Rewards.Slots");
        this.rewardFormat = new HashMap<>();
        for (RewardType rewardType : RewardType.values()) {
            this.rewardFormat.put(rewardType, cfg.getItem("Rewards.Format." + rewardType.name()));
        }

        IMenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                switch (type2) {
                    case CLOSE -> player.closeInventory();
                    case RETURN -> plugin.getBonusManager().getBonusMainMenu().open(player, 1);
                    default -> { }
                }
            }
        };

        for (String id : cfg.getSection("Content")) {
            IMenuItem menuItem = cfg.getMenuItem("Content." + id, MenuItemType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(click);
            }
            this.addItem(menuItem);
        }
    }

    private enum RewardType {
        AVAILABLE, TAKEN, LOCKED, UPCOMING,
    }

    @Override
    public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {
        BonusConfig bonusConfig = plugin.getBonusManager().getBonusConfig(this.bonusConfig.getType());
        if (bonusConfig == null || bonusConfig.getRewardsMap().isEmpty()) {
            throw new IllegalStateException("Attempt to open disabled bonus GUI!");
        }

        BonusUser user = plugin.getUserManager().getOrLoadUser(player);
        BonusData bonusData = user.getBonusData(this.bonusConfig.getType());
        user.validateBonus();

        int rewardCount = bonusData.getRewardCount();
        int rewardLast = bonusConfig.getRewardsMap().lastKey();

        bonusConfig.getRewardsMap().forEach((day, reward) -> {

            boolean isTaken = rewardCount >= day;
            boolean isNext = (rewardCount + 1) == day && !bonusData.isReady();
            boolean isAvailable = (rewardCount + 1) == day && bonusData.isReady();
            boolean isLocked = rewardCount < day && !isNext && !isAvailable;

            ItemStack item;

            if (isAvailable) item = new ItemStack(this.rewardFormat.get(RewardType.AVAILABLE));
            else if (isNext) item = new ItemStack(this.rewardFormat.get(RewardType.UPCOMING));
            else if (isLocked) item = new ItemStack(this.rewardFormat.get(RewardType.LOCKED));
            else if (isTaken) item = new ItemStack(this.rewardFormat.get(RewardType.TAKEN));
            else return;

            ItemUT.replace(item, str -> reward.replacePlaceholders().apply(str
                .replace(IBonusReward.PLACEHOLDER_DAY, String.valueOf(day))));

            IMenuItem icon = new MenuItem(item, this.rewardSlots[day - 1]);
            if (isAvailable) {
                IMenuClick click = (p2, type, e) -> {
                    reward.give(p2);

                    bonusData.setRewardCount(day == rewardLast ? 0 : rewardCount + 1);
                    bonusData.setTimeUntilNextReward(this.bonusConfig.isUnlockAtNewCycle() ? this.bonusConfig.getType().getNewCycleDate()
                        : System.currentTimeMillis() + this.bonusConfig.getUnlockCustomTime());
                    bonusData.setTimeUntilResetReward(System.currentTimeMillis()
                        + (bonusData.getTimeUntilNextReward() - System.currentTimeMillis()) * 2);

                    if (plugin.cfg().dataSaveInstant) {
                        plugin.getUserManager().save(user, true);
                    }

                    this.open(player, 1);
                };

                icon.setClick(click);
            }
            this.addItem(player, icon);
        });
    }

    @Override
    public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull IMenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);

        BonusUser user = plugin.getUserManager().getOrLoadUser(player);
        BonusData bonusData = user.getBonusData(this.bonusConfig.getType());
        user.validateBonus();

        ItemUT.replace(item, bonusData.replacePlaceholders());
    }

    @Override
    public boolean cancelClick(@NotNull SlotType slotType, int slot) {
        return true;
    }
}
