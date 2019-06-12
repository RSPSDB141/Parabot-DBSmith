package org.parabot.dragonblader141.dbsmith.strategies;

import org.parabot.dragonblader141.dbsmith.DBSmithConstants;
import org.parabot.dragonblader141.dbsmith.DBSmithInstance;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.methods.utils.Settings;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.SceneObject;

public class DBSmithSmithing implements Strategy {

    private DBSmithInstance instance;
    public static final int USE_ITEM_WITH_ACTIONID = 62;
    
    public DBSmithSmithing(DBSmithInstance instance) {
        this.instance = instance;
    }

    @Override
    public boolean activate() {
        return instance.getState() == DBSmithInstance.State.SMITHING;
    }

    @Override
    public void execute() {
        DBSmithConstants.OreType oreType = instance.getOreType();

        int barId = DBSmithConstants.getBarItemId(oreType);

        if (Inventory.getCount(barId) == 0) {
            instance.setState(DBSmithInstance.State.DEPOSITING);
            return;
        }

        DBSmithConstants.ItemType toMake = instance.getItemType();
        if (instance.isAutoProgression()) {
            toMake = determineBestItemType(oreType, barId);
        }

        Item bar = Inventory.getItem(barId);
        bar.interact(Items.Option.USE);
        Time.sleep(500);

        SceneObject anvil = SceneObjects.getClosest(DBSmithConstants.Objects.ANVIL);
        if (anvil == null) {
            instance.stop("Could not find anvil");
            return;
        }

        Menu.sendAction(USE_ITEM_WITH_ACTIONID, anvil.getId(), anvil.getLocalRegionX(), anvil.getLocalRegionY());

        Time.sleep(500);
        boolean interfaceOpen = Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return Interfaces.getOpenInterfaceId() == DBSmithConstants.Interfaces.SMITHING_INTERFACE;
            }
        }, 1500);


        if (!interfaceOpen) {
            instance.stop("Could not open smithing menu");
            return;
        }

        Time.sleep(1500);

        System.out.println("Attempting to make " + oreType.name() + " " + toMake.name());
        Menu.sendAction(
                Settings.MENU_TRANSFORM_ONE_INTERACTION.getId(),
                DBSmithConstants.getItemId(oreType, toMake),
                toMake.getRow(),
                DBSmithConstants.Interfaces.SMITHING_ACTION3_OFFSET + toMake.getColumn(),
                4
        );

        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return Players.getMyPlayer().getAnimation() == -1;
            }
        }, 5000);

        Time.sleep(3500);
    }

    private DBSmithConstants.ItemType determineBestItemType(DBSmithConstants.OreType ot, int barId) {
        DBSmithConstants.ItemType bestItem = instance.getBestItemToMake(ot);
        while (bestItem != DBSmithConstants.ItemType.DAGGER) {
            if (Inventory.getCount(barId) >= bestItem.getBarsNeeded()) {
                break;
            }

            bestItem = instance.getBestItemToMake(ot, DBSmithConstants.getSmithingLevelRequired(ot, bestItem)); // get next best ore
        }

        return bestItem;
    }
}
