package su.nightexpress.anotherdailybonus.hooks.citizens;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusConfig;

@TraitName(TraitId.REWARDS_WEEKLY)
public class WeeklyRewardsTrait extends Trait {

    public WeeklyRewardsTrait() {
        super(TraitId.REWARDS_WEEKLY);
    }

    @EventHandler
    public void onClickLeft(NPCRightClickEvent e) {
        if (e.getNPC() == this.getNPC()) {
            Player p = e.getClicker();
            BonusConfig bonusConfig = AnotherDailyBonus.getInstance().getBonusManager().getBonusConfig(BonusType.WEEKLY);
            if (bonusConfig == null) return;

            bonusConfig.openMenu(p);
        }
    }
}
