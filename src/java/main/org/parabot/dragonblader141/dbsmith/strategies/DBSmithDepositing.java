package org.parabot.dragonblader141.dbsmith.strategies;

import org.parabot.dragonblader141.dbsmith.DBSmithConstants;
import org.parabot.dragonblader141.dbsmith.DBSmithInstance;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Bank;

public class DBSmithDepositing implements Strategy {

    private DBSmithInstance instance;

    public DBSmithDepositing(DBSmithInstance instance) {
        this.instance = instance;
    }

    @Override
    public boolean activate() {
        return instance.getState() == DBSmithInstance.State.DEPOSITING;
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
        Bank.depositAllExcept(DBSmithConstants.Item.HAMMER);

        Time.sleep(500);
        instance.setState(DBSmithInstance.State.SETTING_UP);
    }
}
