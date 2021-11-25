package su.nightexpress.anotherdailybonus.bonus.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.utils.ItemUT;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusConfig;
import su.nightexpress.anotherdailybonus.data.object.BonusData;
import su.nightexpress.anotherdailybonus.data.object.BonusUser;

public class BonusMainMenu extends AbstractMenu<AnotherDailyBonus> {

    public BonusMainMenu(@NotNull AnotherDailyBonus plugin, @NotNull JYML cfg) {
        super(plugin, cfg, "");

        IMenuClick click = (p, type, e) -> {
            if (type instanceof BonusType bonusType) {
                if (!bonusType.isEnabled()) return;

                BonusConfig bonusConfig = plugin.getBonusManager().getBonusConfig(bonusType);
                if (bonusConfig == null) return;

                bonusConfig.openMenu(p);
            }
        };

        for (String id : cfg.getSection("Content")) {
            IMenuItem menuItem = cfg.getMenuItem("Content." + id, BonusType.class);

            Enum<?> type = menuItem.getType();
            if (type instanceof BonusType bonusType && !bonusType.isEnabled()) {
                continue;
            }

            if (type != null) {
                menuItem.setClick(click);
            }
            this.addItem(menuItem);
        }
    }

    @Override
    public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull IMenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);

        Enum<?> type = menuItem.getType();
        if (!(type instanceof BonusType bonusType)) return;

        BonusUser user = plugin.getUserManager().getOrLoadUser(player);
        user.validateBonus();
        BonusData bonusData = user.getBonusData(bonusType);

        ItemUT.replace(item, bonusData.replacePlaceholders());
    }

    @Override
    public boolean cancelClick(@NotNull SlotType slotType, int slot) {
        return true;
    }
}
