package org.parabot.dragonblader141.dbsmith;

import org.parabot.dragonblader141.dbsmith.strategies.DBSmithDepositing;
import org.parabot.dragonblader141.dbsmith.strategies.DBSmithSetup;
import org.parabot.dragonblader141.dbsmith.strategies.DBSmithSmelting;
import org.parabot.dragonblader141.dbsmith.strategies.DBSmithSmithing;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.Interfaces;
import org.rev317.min.api.methods.Menu;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * GUI / Info display credits to EmmaStone/SCoutinho.
 * Learned how to draw text and display a GUI based on his
 * Woodcutter script.
 *
 * @author dragonblader141
 */
@ScriptManifest(
        author = "dragonblader141",
        name = "Dragon Smith",
        category = Category.SMITHING,
        version = 0.1,
        description = "Smithing Script for DreamScape",
        servers = {"DreamScape"})
public class DBSmith extends Script implements Paintable, MessageListener {

    private DBSmithInstance state;
    private DBSmithMetrics metrics;
    private DBSmithUI ui;

    public DBSmith() {
        this.state = new DBSmithInstance();
        this.metrics = new DBSmithMetrics();
        this.ui = new DBSmithUI(state);
        ui.setVisible(true);
    }

    @Override
    public boolean onExecute() {
        while (ui.isVisible()) {
            Time.sleep(200);
        }

        if (!state.getOptionsSet()) {
            return false;
        }

        // Open teleports menu -- purposely left hardcoded. lmk if it's a problem
        Menu.sendAction(1043, 2361, 16, 3214);

        Time.sleep(200);
        Menu.clickButton(DBSmithConstants.Buttons.SKILLING_TELEPORT);
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return Interfaces.getBackDialogId() == DBSmithConstants.Interfaces.SKILLING_BACK_DIALOG;
            }
        }, 3000);

        Menu.clickButton(DBSmithConstants.Buttons.SELECT_MINING_SMITHING);
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return DBSmithConstants.Locations.MINING_START.distanceTo() == 0;
            }
        }, 10000);

        this.state.setState(DBSmithInstance.State.SETTING_UP);

        List<Strategy> strategies = new ArrayList<>();
        strategies.addAll(Arrays.asList(
                new DBSmithSetup(this.state),
                new DBSmithSmelting(this.state),
                new DBSmithSmithing(this.state),
                new DBSmithDepositing(this.state)
        ));

        provide(strategies);
        return true;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;

        g2d.setColor(Color.CYAN);

        g2d.drawString("DBSmith 1.0", 10, 35);
        g2d.drawString("Bars Smelted: " + metrics.getBarsSmelted(), 10, 60);
        g2d.drawString("Items Smithed: " + metrics.getItemsSmithed(), 10, 75);
    }

    @Override
    public void messageReceived(MessageEvent messageEvent) {
        if (messageEvent.getMessage().contains("You make")) {
            this.metrics.incrementItems();
        } else if (messageEvent.getMessage().contains("You retrieve")) {
            this.metrics.incrementBars();
        }
    }
}
