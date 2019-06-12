package org.parabot.dragonblader141.dbsmith;

import org.rev317.min.api.wrappers.Tile;

public class DBSmithConstants {

    public static class Item {
        public static final int HAMMER = 2348;

        public static final int BRONZE_BAR = 2350;
        public static final int IRON_BAR = 2352;
        public static final int STEEL_BAR = 2354;
        public static final int MITHRIL_BAR = 2360;
        public static final int ADAMANT_BAR = 2362;
        public static final int RUNE_BAR = 2364;

        public static final int COPPER_ORE = 437;
        public static final int TIN_ORE = 439;
        public static final int IRON_ORE = 441;
        public static final int MITHRIL_ORE = 448;
        public static final int ADAMANT_ORE = 450;
        public static final int RUNITE_ORE = 452;
    }

    public static class Interfaces {
        public static final int SKILLING_BACK_DIALOG = 2492;
        public static final int SMELT_BAR_BACK_DIALOG = 2400;
        public static final int SMITHING_INTERFACE = 994;
        public static final int SMITHING_ACTION3_OFFSET = 1119; // corresponds to the column (1119 + column #)
    }

    public static class Buttons {
        public static final int SKILLING_TELEPORT = 20053;
        public static final int SELECT_MINING_SMITHING = 2495;

        public static final int SMELT_X_BRONZE = 2414;
        public static final int SMELT_X_IRON = 3988;
        public static final int SMELT_X_STEEL = 3996;
        public static final int SMELT_X_MITHRIL = 4158;
        public static final int SMELT_X_ADAMANT = 7442;
        public static final int SMELT_X_RUNE = 7447;
    }

    public static class Locations {
        public static final Tile MINING_START = new Tile(9905, 2916);
        public static final Tile FURNACE_START = new Tile(9901, 2913);
        public static final Tile ANVIL_START = new Tile(9912, 2906);
    }

    public static class Objects {
        public static final int FURNACE = 3044;
        public static final int ANVIL = 2783;
    }

    public enum OreType {
        BRONZE(0), IRON(15), STEEL(30), MITHRIL(50), ADAMANT(70), RUNE(85);

        private int baseLevel;

        OreType(int baseLevel) {
            this.baseLevel = baseLevel;
        }

        public int getBaseLevel() {
            return baseLevel;
        }
    }

    public enum ItemType {

        DAGGER(0, 1, 0, 0),
        MACE(2, 1, 1, 1),
        ARROWTIPS(5, 1, 1, 4),
        SCIMITAR(5, 3, 0, 2),
        PLATELEGS(18, 3, 1, 2);
        private int levelModifier, barsNeeded, row, column;

        ItemType(int levelModifier, int barsNeeded, int row, int column) {
            this.levelModifier = levelModifier;
            this.barsNeeded = barsNeeded;
            this.row = row;
            this.column = column;
        }

        public int getLevelModifier() {
            return levelModifier;
        }

        public int getBarsNeeded() {
            return barsNeeded;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }
    }


    public static final int getSmithingLevelRequired(OreType ot, ItemType it) {
        // This coerces the range to be between 1 and 99, inclusive.
        return Math.max(1, Math.min(ot.getBaseLevel() + it.getLevelModifier(), 99));
    }

    public static final int getItemId(OreType ot, ItemType it) {
        switch (ot) {
            case BRONZE:
                switch (it) {
                    case DAGGER:
                        return 1205;
                    case MACE:
                        return 1422;
                    case ARROWTIPS:
                        return 39;
                    case SCIMITAR:
                        return 1321;
                    case PLATELEGS:
                        return 1075;
                }
            case IRON:
                switch (it) {
                    case DAGGER:
                        return 1203;
                    case MACE:
                        return 1420;
                    case ARROWTIPS:
                        return 40;
                    case SCIMITAR:
                        return 1323;
                    case PLATELEGS:
                        return 1067;
                }
            case STEEL:
                switch (it) {
                    case DAGGER:
                        return 1207;
                    case MACE:
                        return 1424;
                    case ARROWTIPS:
                        return 41;
                    case SCIMITAR:
                        return 1325;
                    case PLATELEGS:
                        return 1069;
                }
            case MITHRIL:
                switch (it) {
                    case DAGGER:
                        return 1209;
                    case MACE:
                        return 1428;
                    case ARROWTIPS:
                        return 42;
                    case SCIMITAR:
                        return 1329;
                    case PLATELEGS:
                        return 1071;
                }
            case ADAMANT:
                switch (it) {
                    case DAGGER:
                        return 1211;
                    case MACE:
                        return 1430;
                    case ARROWTIPS:
                        return 43;
                    case SCIMITAR:
                        return 1331;
                    case PLATELEGS:
                        return 1073;
                }
            case RUNE:
                switch (it) {
                    case DAGGER:
                        return 1213;
                    case MACE:
                        return 1432;
                    case ARROWTIPS:
                        return 44;
                    case SCIMITAR:
                        return 1333;
                    case PLATELEGS:
                        return 1079;
                }
        }
        return -1;
    }

    public static int getOreItemId(OreType oreType) {
        switch (oreType) {
            case RUNE:
                return Item.RUNITE_ORE;
            case ADAMANT:
                return Item.ADAMANT_ORE;
            case MITHRIL:
                return Item.MITHRIL_ORE;
            case STEEL:
            case IRON:
                return Item.IRON_ORE;
            case BRONZE:
                return Item.COPPER_ORE;
            default:
                return -1;
        }
    }

    public static int getBarItemId(OreType ot) {
        switch (ot) {
            case RUNE:
                return Item.RUNE_BAR;
            case ADAMANT:
                return Item.ADAMANT_BAR;
            case MITHRIL:
                return Item.MITHRIL_BAR;
            case STEEL:
                return Item.STEEL_BAR;
            case IRON:
                return Item.IRON_BAR;
            case BRONZE:
                return Item.BRONZE_BAR;
        }
        return 0;
    }

    public static int getSmeltingButton(OreType ot) {
        switch (ot) {
            case RUNE:
                return Buttons.SMELT_X_RUNE;
            case ADAMANT:
                return Buttons.SMELT_X_ADAMANT;
            case MITHRIL:
                return Buttons.SMELT_X_MITHRIL;
            case STEEL:
                return Buttons.SMELT_X_STEEL;
            case IRON:
                return Buttons.SMELT_X_IRON;
            case BRONZE:
                return Buttons.SMELT_X_BRONZE;
        }
        return 0;
    }


}
