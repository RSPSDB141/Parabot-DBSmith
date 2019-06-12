package org.parabot.dragonblader141.dbsmith.strategies;

import org.parabot.dragonblader141.dbsmith.DBSmithConstants;
import org.parabot.dragonblader141.dbsmith.DBSmithInstance;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Bank;
import org.rev317.min.api.methods.Interfaces;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.wrappers.Item;

public class DBSmithSetup implements Strategy {

    private final DBSmithInstance instance;

    public DBSmithSetup(DBSmithInstance instance) {
        this.instance = instance;
    }

    @Override
    public boolean activate() {
        return instance.getState() == DBSmithInstance.State.SETTING_UP;
    }

    @Override
    public void execute() {
        Bank.open();
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return Bank.isOpen();
            }
        }, 10000);

        if (Inventory.getItem(DBSmithConstants.Item.HAMMER) == null) {
            if (Bank.getItem(DBSmithConstants.Item.HAMMER) == null) {
                instance.stop("Could not find hammer in bank or inventory.");
                return;
            }

            Bank.withdraw(DBSmithConstants.Item.HAMMER, 1, 500);
        }

        DBSmithConstants.OreType toWithdraw;
        if (instance.isAutoProgression()) {
            toWithdraw = determineBestOreType();
        } else {
            toWithdraw = instance.getOreType();
        }

        if (toWithdraw != DBSmithConstants.OreType.BRONZE) {
            int itemId = DBSmithConstants.getOreItemId(toWithdraw);
            int stackSize = Bank.getCount(itemId);

            if (stackSize == 0) {
                instance.stop("Could not find ores in bank or inventory.");
                return;
            }

            Bank.withdraw(itemId, 0, 500);
        } else {
            int qtyCopper = Math.min(13, Bank.getCount(DBSmithConstants.Item.COPPER_ORE));
            int qtyTin = Math.min(13, Bank.getCount(DBSmithConstants.Item.TIN_ORE));

            if (qtyCopper <= 0 || qtyTin <= 0) {
                instance.stop("Could not find ores in bank or inventory.");
                return;
            }

            int qty = Math.min(qtyCopper, qtyTin);

            Bank.withdraw(DBSmithConstants.Item.COPPER_ORE, qty, 200);
            Time.sleep(500);
            Bank.withdraw(DBSmithConstants.Item.TIN_ORE, qty, 200);
            Time.sleep(500);

        }

        Interfaces.openInterface(-1);

        instance.setOreType(toWithdraw);
        instance.setState(DBSmithInstance.State.SMELTING);
    }

    private DBSmithConstants.OreType determineBestOreType() {
        DBSmithConstants.OreType bestOre = instance.getBestOre();
        while (bestOre != DBSmithConstants.OreType.BRONZE) {
            Item i;
            if ((i = Bank.getItem(DBSmithConstants.getOreItemId(bestOre))) != null) {
                int stackSize = i.getStackSize();
                if (stackSize > 0) {
                    break;
                }
            }

            bestOre = instance.getBestOre(bestOre.getBaseLevel()); // get next best ore
        }

        return bestOre;
    }
}
