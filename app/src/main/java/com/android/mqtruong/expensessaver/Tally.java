package com.android.mqtruong.expensessaver;


import android.widget.LinearLayout;

public class Tally {// extends LinearLayout{
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
