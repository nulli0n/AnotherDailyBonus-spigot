package su.nightexpress.anotherdailybonus.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.CollectionsUT;
import su.nexmedia.engine.utils.PlayerUT;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.Perms;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.data.object.BonusUser;

import java.util.List;

public class ResetCommand extends AbstractCommand<AnotherDailyBonus> {

    public ResetCommand(@NotNull AnotherDailyBonus plugin) {
        super(plugin, new String[]{"reset"}, Perms.CMD_RESET);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.lang().Command_Reset_Usage.getMsg();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.lang().Command_Reset_Desc.getMsg();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<@NotNull String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return PlayerUT.getPlayerNames();
        }
        if (arg == 2) {
            return CollectionsUT.getEnumsList(BonusType.class);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) {
            this.printUsage(sender);
            return;
        }

        BonusUser user = plugin.getUserManager().getOrLoadUser(args[1], false);
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        BonusType bonusType = CollectionsUT.getEnum(args[2], BonusType.class);
        if (bonusType == null) {
            this.errorType(sender, BonusType.class);
            return;
        }

        user.getBonusData(bonusType).reset();
        plugin.lang().Command_Reset_Done
            .replace("%type%", plugin.lang().getEnum(bonusType))
            .replace("%player%", user.getName())
            .send(sender);
    }
}
