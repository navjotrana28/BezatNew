package com.bezat.bezat.models;

public class DashBoardItem {
    String name;
  int drawable;

    public DashBoardItem( int drawable,String name) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
