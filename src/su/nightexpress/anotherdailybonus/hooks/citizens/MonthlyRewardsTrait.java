package su.nightexpress.anotherdailybonus.hooks.citizens;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusConfig;

@TraitName(TraitId.REWARDS_MONTHLY)
public class MonthlyRewardsTrait extends Trait {

    public MonthlyRewardsTrait() {
        super(TraitId.REWARDS_MONTHLY);
    }

    @EventHandler
    public void onClickLeft(NPCRightClickEvent e) {
        if (e.getNPC() == this.getNPC()) {
            Player p = e.getClicker();
            BonusConfig bonusConfig = AnotherDailyBonus.getInstance().getBonusManager().getBonusConfig(BonusType.MONTHLY);
            if (bonusConfig == null) return;

            bonusConfig.openMenu(p);
        }
    }
}
