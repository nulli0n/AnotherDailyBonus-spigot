package su.nightexpress.anotherdailybonus.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.utils.actions.ActionManipulator;

import java.util.List;
import java.util.function.UnaryOperator;

public interface IBonusReward extends IPlaceholder {

    String PLACEHOLDER_DAY = "%bonus_reward_counter%";
    String PLACEHOLDER_DESCRIPTION = "%bonus_reward_description%";

    @Override
    @NotNull
    default UnaryOperator<String> replacePlaceholders() {
        return str -> str
            .replace(PLACEHOLDER_DESCRIPTION, String.join("\n", this.getDescription()))
            ;
    }

    @NotNull List<String> getDescription();

    void setDescription(@NotNull List<String> description);

    @NotNull ActionManipulator getRewardActions();

    void setRewardActions(@NotNull ActionManipulator rewardActions);

    default void give(@NotNull Player player) {
        this.getRewardActions().process(player);
    }
}
