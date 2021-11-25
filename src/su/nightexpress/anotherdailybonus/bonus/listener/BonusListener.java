package su.nightexpress.anotherdailybonus.bonus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.event.EngineUserLoadEvent;
import su.nexmedia.engine.api.manager.AbstractListener;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusConfig;
import su.nightexpress.anotherdailybonus.bonus.BonusManager;
import su.nightexpress.anotherdailybonus.config.Config;
import su.nightexpress.anotherdailybonus.data.object.BonusData;
import su.nightexpress.anotherdailybonus.data.object.BonusUser;

import java.util.ArrayList;
import java.util.List;

public class BonusListener extends AbstractListener<AnotherDailyBonus> {

    private BonusManager bonusManager;

    public BonusListener(@NotNull BonusManager bonusManager) {
        super(bonusManager.plugin());
        this.bonusManager = bonusManager;
    }

    @EventHandler
    public void onUserLoad(EngineUserLoadEvent<AnotherDailyBonus, BonusUser> e) {
        if (!(e.getPlugin() instanceof AnotherDailyBonus)) return;
        if (!Config.MENU_POPUP_ENABLED) return;

        BonusUser user = e.getUser();
        Player player = user.getPlayer();
        if (player == null) return;

        user.validateBonus();

        List<BonusType> ready = new ArrayList<>();
        for (BonusType bonusType : BonusType.getEnabled()) {
            BonusData bonusData = user.getBonusData(bonusType);
            if (bonusData.isReady()) ready.add(bonusType);
        }

        if (ready.isEmpty() && Config.MENU_POPUP_WHEN_READY_ONLY) return;
        if (ready.size() != 1) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                this.plugin.getBonusManager().getBonusMainMenu().open(player, 1);
            }, 5L);
            return;
        }

        BonusType bonusType = ready.get(0);
        BonusConfig bonusConfig = this.bonusManager.getBonusConfig(bonusType);
        if (bonusConfig == null) return;

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            bonusConfig.openMenu(player);
        }, 5L);
    }
}
