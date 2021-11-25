package su.nightexpress.anotherdailybonus;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.data.UserDataHolder;
import su.nexmedia.engine.commands.api.IGeneralCommand;
import su.nightexpress.anotherdailybonus.command.OpenCommand;
import su.nightexpress.anotherdailybonus.command.ResetCommand;
import su.nightexpress.anotherdailybonus.config.Config;
import su.nightexpress.anotherdailybonus.config.Lang;
import su.nightexpress.anotherdailybonus.data.UserDataHandler;
import su.nightexpress.anotherdailybonus.data.UserManager;
import su.nightexpress.anotherdailybonus.data.object.BonusUser;
import su.nightexpress.anotherdailybonus.bonus.BonusManager;

import java.sql.SQLException;

public class AnotherDailyBonus extends NexPlugin<AnotherDailyBonus> implements UserDataHolder<AnotherDailyBonus, BonusUser> {

    private static AnotherDailyBonus instance;

    private Config config;
    private Lang lang;

    private UserDataHandler dataHandler;
    private UserManager userManager;

    private BonusManager bonusManager;

    public AnotherDailyBonus() {
        instance = this;
    }

    @NotNull
    public static AnotherDailyBonus getInstance() {
        return instance;
    }

    @Override
    public void enable() {
        this.bonusManager = new BonusManager(this);
        this.bonusManager.setup();
    }

    @Override
    public void disable() {
        if (this.bonusManager != null) {
            this.bonusManager.shutdown();
            this.bonusManager = null;
        }
    }

    @Override
    public boolean setupDataHandlers() {
        try {
            this.dataHandler = UserDataHandler.getInstance(this);
            this.dataHandler.setup();
        }
        catch (SQLException e) {
            this.error("Could not setup data handler!");
            return false;
        }

        this.userManager = new UserManager(this);
        this.userManager.setup();

        return true;
    }

    @Override
    public void setConfig() {
        this.config = new Config(this);
        this.config.setup();

        this.lang = new Lang(this);
        this.lang.setup();
    }

    @Override
    public void registerHooks() {

    }

    @Override
    public void registerCmds(@NotNull IGeneralCommand<AnotherDailyBonus> mainCommand) {

    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<AnotherDailyBonus> mainCommand) {
        super.registerCommands(mainCommand);
        mainCommand.addDefaultCommand(new OpenCommand(this));
        mainCommand.addChildren(new ResetCommand(this));
    }

    @Override
    public boolean useNewCommandManager() {
        return true;
    }

    @Override
    @NotNull
    public Config cfg() {
        return this.config;
    }

    @Override
    @NotNull
    public Lang lang() {
        return this.lang;
    }

    @Override
    @NotNull
    public UserDataHandler getData() {
        return this.dataHandler;
    }

    @NotNull
    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @NotNull
    public BonusManager getBonusManager() {
        return bonusManager;
    }
}
