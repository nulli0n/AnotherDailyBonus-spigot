package su.nightexpress.anotherdailybonus.config;

import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.config.api.IConfigTemplate;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;

public class Config extends IConfigTemplate {

    public static boolean MENU_POPUP_ENABLED;
    public static boolean MENU_POPUP_WHEN_READY_ONLY;

    public Config(@NotNull AnotherDailyBonus plugin) {
        super(plugin);
    }

    @Override
    protected void load() {
        for (BonusType bonusType : BonusType.values()) {
            cfg.addMissing("Bonus_Types." + bonusType.name(), true);
            bonusType.setEnabled(cfg.getBoolean("Bonus_Types." + bonusType.name()));
        }

        cfg.saveChanges();

        MENU_POPUP_ENABLED = cfg.getBoolean("Menu_Popup.Enabled");
        MENU_POPUP_WHEN_READY_ONLY = cfg.getBoolean("Menu_Popup.Only_When_Ready");
    }
}
