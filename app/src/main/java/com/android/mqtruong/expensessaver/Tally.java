package com.android.mqtruong.expensessaver;


import android.widget.LinearLayout;

import java.util.ArrayList;

public class Tally {
    public static final int DEFAULT_VALUE = 0;
    public static final double DEFAULT_AMOUNT = 0.0;
    public static final double DEFAULT_STEP = 1.0;
    int value;
    String name;
    double amount;
    double steps;
    public Tally(String name, int value, double amount, double steps) {
        this.name = name;
        this.value = value;
        this.amount = amount;
        this.steps = steps;
    }
}
