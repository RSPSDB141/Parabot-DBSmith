package org.parabot.dragonblader141.dbsmith.strategies;

import org.parabot.dragonblader141.dbsmith.DBSmithConstants;
import org.parabot.dragonblader141.dbsmith.DBSmithInstance;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.SceneObject;

public class DBSmithSmelting implements Strategy {

    private final DBSmithInstance instance;

    public DBSmithSmelting(DBSmithInstance instance) {
        this.instance = instance;
    }

    @Override
    public boolean activate() {
        return instance.getState() == DBSmithInstance.State.SMELTING;
    }

    @Override
    public void execute() {
        Walking.walkTo(DBSmithConstants.Locations.FURNACE_START);

        SceneObject furnace = SceneObjects.getClosest(DBSmithConstants.Objects.FURNACE);
        if (furnace == null) {
            instance.stop("Could not find furnace object");
            return;
        }

        furnace.interact(SceneObjects.Option.SMELT);
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return Interfaces.getBackDialogId() == DBSmithConstants.Interfaces.SMELT_BAR_BACK_DIALOG;
            }
        }, 5000);

        final DBSmithConstants.OreType oreType = instance.getOreType();
        Menu.clickButton(DBSmithConstants.getSmeltingButton(oreType));
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return Inventory.getItem(DBSmithConstants.getOreItemId(oreType)) == null;
            }
        }, 30000);

        Walking.walkTo(DBSmithConstants.Locations.ANVIL_START);
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return DBSmithConstants.Locations.ANVIL_START.distanceTo() == 0;
            }
        }, 5000);

        instance.setState(DBSmithInstance.State.SMITHING);
    }

}
