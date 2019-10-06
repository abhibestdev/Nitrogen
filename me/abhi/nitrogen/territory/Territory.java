package me.abhi.nitrogen.territory;

import me.abhi.nitrogen.util.Border;

public class Territory {

    private String name;
    private boolean safe;
    private Border border;

    public Territory(String name, boolean safe, Border border) {
        this.name = name;
        this.safe = safe;
        this.border = border;
    }

    public String getName() {
        return name;
    }

    public boolean isSafe() {
        return safe;
    }

    public Border getBorder() {
        return border;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public void setBorder(Border border) {
        this.border = border;
    }
}
