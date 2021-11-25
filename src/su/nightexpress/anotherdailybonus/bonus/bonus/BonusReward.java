package su.nightexpress.anotherdailybonus.bonus.bonus;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.StringUT;
import su.nexmedia.engine.utils.actions.ActionManipulator;
import su.nightexpress.anotherdailybonus.api.IBonusReward;

import java.util.List;

public class BonusReward implements IBonusReward {

    private List<String> description;
    private ActionManipulator rewardActions;

    public BonusReward(@NotNull List<String> description, @NotNull ActionManipulator rewardActions) {
        this.setDescription(description);
        this.setRewardActions(rewardActions);
    }

    @Override
    @NotNull
    public List<String> getDescription() {
        return description;
    }

    @Override
    public void setDescription(@NotNull List<String> description) {
        this.description = StringUT.color(description);
    }

    @NotNull
    @Override
    public ActionManipulator getRewardActions() {
        return rewardActions;
    }

    @Override
    public void setRewardActions(@NotNull ActionManipulator rewardActions) {
        this.rewardActions = rewardActions;
    }
}
