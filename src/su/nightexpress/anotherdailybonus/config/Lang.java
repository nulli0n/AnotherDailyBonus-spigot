package su.nightexpress.anotherdailybonus.config;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.config.api.ILangMsg;
import su.nexmedia.engine.core.config.CoreLang;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;

public class Lang extends CoreLang {

    public Lang(@NotNull AnotherDailyBonus plugin) {
        super(plugin);
    }

    @Override
    public void setup() {
        super.setup();
        this.setupEnum(BonusType.class);
    }

    public ILangMsg Command_Open_Desc  = new ILangMsg(this, "Open bonus menu.");
    public ILangMsg Command_Open_Usage = new ILangMsg(this, "[type]");

    public ILangMsg Command_Reset_Desc = new ILangMsg(this, "Reset player bonus time.");
    public ILangMsg Command_Reset_Usage = new ILangMsg(this, "[player] [bonusType]");
    public ILangMsg Command_Reset_Done = new ILangMsg(this, "Reset &e%type% &7bonus data for &e%player%&7.");
}
