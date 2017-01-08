package com.android.mqtruong.expensessaver;

import java.util.ArrayList;

/**
 * Created by Mai on 1/7/2017.
 */

public class MainApplication {
    ArrayList<Tally> tallyArrayList;

    public MainApplication() {
        tallyArrayList = new ArrayList<Tally>();
    }
    public void addTally(String name, int value) {
        tallyArrayList.add(new Tally(name, value));
    }
}
