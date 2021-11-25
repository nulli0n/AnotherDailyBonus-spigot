package su.nightexpress.anotherdailybonus.hooks.citizens;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;

@TraitName(TraitId.DAILY_BONUS)
public class DailyBonusTrait extends Trait {

    public DailyBonusTrait() {
        super(TraitId.DAILY_BONUS);
    }

    @EventHandler
    public void onClickLeft(NPCRightClickEvent e) {
        if (e.getNPC() == this.getNPC()) {
            Player player = e.getClicker();
            AnotherDailyBonus.getInstance().getBonusManager().getBonusMainMenu().open(player, 1);
        }
    }
}
