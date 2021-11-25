package su.nightexpress.anotherdailybonus.data;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserDataHandler;
import su.nexmedia.engine.data.DataTypes;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;
import su.nightexpress.anotherdailybonus.api.BonusType;
import su.nightexpress.anotherdailybonus.data.object.BonusData;
import su.nightexpress.anotherdailybonus.data.object.BonusUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class UserDataHandler extends AbstractUserDataHandler<AnotherDailyBonus, BonusUser> {

    private static UserDataHandler               instance;
    private final Function<ResultSet, BonusUser> FUNC_USER;

    private static final String COL_BONUS_DATA = "bonusData";

    protected UserDataHandler(@NotNull AnotherDailyBonus plugin) throws SQLException {
        super(plugin);

        this.FUNC_USER = (rs) -> {
            try {
                UUID uuid = UUID.fromString(rs.getString(COL_USER_UUID));
                String name = rs.getString(COL_USER_NAME);
                long lastOnline = rs.getLong(COL_USER_LAST_ONLINE);

                Map<BonusType, BonusData> storedBonus = gson.fromJson(rs.getString(COL_BONUS_DATA), new TypeToken<Map<BonusType, BonusData>>() {}.getType());

                return new BonusUser(plugin, uuid, name, lastOnline, storedBonus);
            }
            catch (SQLException e) {
                return null;
            }
        };
    }

    @NotNull
    public static UserDataHandler getInstance(@NotNull AnotherDailyBonus plugin) throws SQLException {
        if (instance == null) {
            instance = new UserDataHandler(plugin);
        }
        return instance;
    }

    @Override
    @NotNull
    protected LinkedHashMap<String, String> getColumnsToCreate() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(COL_BONUS_DATA, DataTypes.STRING.build(this.dataType));
        return map;
    }

    @Override
    @NotNull
    protected LinkedHashMap<String, String> getColumnsToSave(@NotNull BonusUser user) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(COL_BONUS_DATA, this.gson.toJson(user.getBonusData()));
        return map;
    }

    @Override
    @NotNull
    protected Function<ResultSet, BonusUser> getFunctionToUser() {
        return this.FUNC_USER;
    }
}
