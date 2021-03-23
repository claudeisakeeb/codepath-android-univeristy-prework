package com.example.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    String name, completeDate;
    int prio;

    public Item(String name) {
        this.name = name;
        this.completeDate = "";
        this.prio = 1;
    }
    public Item(String name, int prio) {
        this.name = name;
        this.prio = prio;
        this.completeDate = "";
    }

    public Item(String name, int prio, String completeDate) {
        this.name = name;
        this.prio = prio;
        this.completeDate = completeDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrio(int prio) {
        this.prio = prio;
    }

    public void completeItem() {
        this.prio = 0;
        this.completeDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public String getName() {
        return this.name;
    }

    public int getPriority() {
        return this.prio;
    }

    public String getCompleteDate() {
        return this.completeDate;
    }
}
