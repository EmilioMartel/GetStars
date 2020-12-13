package com.marquelo.getstars.working;

public class Address {
    private String address;
    private boolean isSelected;

    public Address() {
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
