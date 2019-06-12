package org.parabot.dragonblader141.dbsmith;

import org.parabot.core.Context;
import org.parabot.environment.scripts.Script;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Skill;

public class DBSmithInstance {

    private boolean autoProgression;
    private boolean optionsSet;
    private DBSmithConstants.OreType oreType;
    private DBSmithConstants.ItemType itemType;
    private State state;

    public void stop(String reason) {
        System.out.println("[DBSmith] Stopped due to " + reason);
        Context.getInstance().getRunningScript().setState(Script.STATE_STOPPED);
    }

    public void setAutoProgression(boolean selected) {
        this.autoProgression = selected;
    }

    public boolean isAutoProgression() {
        return autoProgression;
    }

    public void setOptionsSet(boolean options) {
        this.optionsSet = options;
    }

    public boolean getOptionsSet() {
        return optionsSet;
    }

    public void setOreType(DBSmithConstants.OreType selectedItem) {
        this.oreType = selectedItem;
    }

    // TODO change based on autoprogression
    public DBSmithConstants.OreType getOreType() {
        return oreType;
    }

    public State getState() {
        return this.state;
    }

    public int getSmithingLevel() {
        return Skill.SMITHING.getLevel();
    }

    public int getBarCount(DBSmithConstants.OreType ot) {
        int barId = DBSmithConstants.getBarItemId(ot);
        return Inventory.getCount(barId);
    }

    public DBSmithConstants.OreType getBestOre() {
        return getBestOre(999);
    }

    public DBSmithConstants.OreType getBestOre(int cap) {
        DBSmithConstants.OreType best = DBSmithConstants.OreType.BRONZE;
        int skillLevel = getSmithingLevel();

        for (DBSmithConstants.OreType ore : DBSmithConstants.OreType.values()) {
            if (ore.getBaseLevel() >= cap) {
                continue;
            }
            if (ore.getBaseLevel() <= skillLevel && ore.getBaseLevel() > best.getBaseLevel()) {
                best = ore;
            }
        }

        return best;
    }


    public DBSmithConstants.ItemType getBestItemToMake(DBSmithConstants.OreType oreType) {
        return getBestItemToMake(oreType, 999);
    }

    public DBSmithConstants.ItemType getBestItemToMake(DBSmithConstants.OreType ot, int cap) {
        DBSmithConstants.ItemType bestItem = null;
        int bestLevel = 0;
        int skillLevel = getSmithingLevel();

        int barCount = getBarCount(ot);

        for (DBSmithConstants.ItemType it : DBSmithConstants.ItemType.values()) {
            int requiredLevel = DBSmithConstants.getSmithingLevelRequired(ot, it);
            if (requiredLevel >= cap) {
                continue;
            }
            if (it.getBarsNeeded() > barCount) {
                continue;
            }

            if (requiredLevel >= bestLevel && skillLevel >= requiredLevel) {
                bestLevel = requiredLevel;
                bestItem = it;
            }
        }


        return bestItem;
    }


    public void setState(State newState) {
        this.state = newState;
    }

    public DBSmithConstants.ItemType getItemType() {
        return itemType;
    }

    public void setItemType(DBSmithConstants.ItemType it) {
        this.itemType = it;
    }

    public enum State {
        SETTING_UP, SMELTING, SMITHING, DEPOSITING;
    }
}
