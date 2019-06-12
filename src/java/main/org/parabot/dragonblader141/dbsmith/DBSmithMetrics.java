package org.parabot.dragonblader141.dbsmith;

public class DBSmithMetrics {

    private final long startTime;
    private int barsSmelted;
    private int itemsSmithed;

    public DBSmithMetrics() {
        this.startTime = System.currentTimeMillis();
        this.barsSmelted = 0;
        this.itemsSmithed = 0;

    }

    public void incrementBars() {
        this.barsSmelted++;
    }

    public void incrementItems() {
        this.itemsSmithed++;
    }

    public int getBarsSmelted() {
        return barsSmelted;
    }

    public int getItemsSmithed() {
        return itemsSmithed;
    }
}
