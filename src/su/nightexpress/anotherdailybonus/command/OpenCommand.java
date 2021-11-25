package su.nightexpress.anotherdailybonus.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.CollectionsUT;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.Perms;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.bonus.bonus.BonusConfig;

import java.util.Arrays;
import java.util.List;

public class OpenCommand extends AbstractCommand<AnotherDailyBonus> {

    public OpenCommand(@NotNull AnotherDailyBonus plugin) {
        super(plugin, new String[] { "open" }, Perms.CMD_OPEN);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.lang().Command_Open_Usage.getMsg();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.lang().Command_Open_Desc.getMsg();
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    @NotNull
    public List<@NotNull String> getTab(@NotNull Player p, int i, @NotNull String[] args) {
        if (i == 1) {
            return Arrays.stream(BonusType.getEnabled()).map(BonusType::name).toList();
        }
        return super.getTab(p, i, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        BonusType bonusType = args.length >= 2 ? CollectionsUT.getEnum(args[1], BonusType.class) : null;

        if (bonusType == null || !bonusType.isEnabled()) {
            plugin.getBonusManager().getBonusMainMenu().open(p, 1);
        }
        else {
            BonusConfig bonusConfig = plugin.getBonusManager().getBonusConfig(bonusType);
            if (bonusConfig == null)
                return;

            bonusConfig.openMenu(p);
        }
    }
}
