package su.nightexpress.anotherdailybonus.bonus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.hooks.external.citizens.CitizensHK;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusConfig;
import su.nightexpress.anotherdailybonus.bonus.listener.BonusListener;
import su.nightexpress.anotherdailybonus.bonus.menu.BonusMainMenu;
import su.nightexpress.anotherdailybonus.hooks.citizens.DailyBonusTrait;
import su.nightexpress.anotherdailybonus.hooks.citizens.DailyRewardsTrait;
import su.nightexpress.anotherdailybonus.hooks.citizens.MonthlyRewardsTrait;
import su.nightexpress.anotherdailybonus.hooks.citizens.WeeklyRewardsTrait;

import java.util.HashMap;
import java.util.Map;

public class BonusManager extends AbstractManager<AnotherDailyBonus> {

    private Map<BonusType, BonusConfig> bonusConfig;
    private BonusMainMenu               bonusMainMenu;

    public BonusManager(@NotNull AnotherDailyBonus plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.bonusConfig = new HashMap<>();
        this.bonusMainMenu = new BonusMainMenu(plugin, JYML.loadOrExtract(plugin, "main.menu.yml"));
        CitizensHK citizensHook = plugin.getCitizens();

        for (BonusType bonusType : BonusType.getEnabled()) {
            String name = bonusType.name().toLowerCase();

            JYML cfg = JYML.loadOrExtract(plugin, "/rewards/" + name + "/rewards.yml");
            JYML cfgMenu = JYML.loadOrExtract(plugin, "/rewards/" + name + "/menu.yml");

            BonusConfig bonusConfig = new BonusConfig(plugin, cfg, cfgMenu, bonusType);
            this.bonusConfig.put(bonusType, bonusConfig);

            if (citizensHook != null) {
                if (bonusType == BonusType.DAILY) {
                    citizensHook.registerTrait(plugin, DailyRewardsTrait.class);
                }
                else if (bonusType == BonusType.WEEKLY) {
                    citizensHook.registerTrait(plugin, WeeklyRewardsTrait.class);
                }
                else if (bonusType == BonusType.MONTHLY) {
                    citizensHook.registerTrait(plugin, MonthlyRewardsTrait.class);
                }
            }
        }
        this.plugin.info("Loaded " + this.bonusConfig.size() + " bonus types!");

        if (citizensHook != null) {
            citizensHook.registerTrait(plugin, DailyBonusTrait.class);
        }
        this.addListener(new BonusListener(this));
    }

    @Override
    public void onShutdown() {
        if (this.bonusMainMenu != null) {
            this.bonusMainMenu.clear();
            this.bonusMainMenu = null;
        }
        if (this.bonusConfig != null) {
            this.bonusConfig.values().forEach(BonusConfig::clear);
            this.bonusConfig.clear();
            this.bonusConfig = null;
        }
    }

    @NotNull
    public BonusMainMenu getBonusMainMenu() {
        return bonusMainMenu;
    }

    @Nullable
    public BonusConfig getBonusConfig(@NotNull BonusType bonusType) {
        return this.bonusConfig.get(bonusType);
    }
}
