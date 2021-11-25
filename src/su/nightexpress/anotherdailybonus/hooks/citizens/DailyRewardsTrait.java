package su.nightexpress.anotherdailybonus.hooks.citizens;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusConfig;

@TraitName(TraitId.REWARDS_DAILY)
public class DailyRewardsTrait extends Trait {

    public DailyRewardsTrait() {
        super(TraitId.REWARDS_DAILY);
    }

    @EventHandler
    public void onClickLeft(NPCRightClickEvent e) {
        if (e.getNPC() == this.getNPC()) {
            Player p = e.getClicker();
            BonusConfig bonusConfig = AnotherDailyBonus.getInstance().getBonusManager().getBonusConfig(BonusType.DAILY);
            if (bonusConfig == null) return;

            bonusConfig.openMenu(p);
        }
    }
}
