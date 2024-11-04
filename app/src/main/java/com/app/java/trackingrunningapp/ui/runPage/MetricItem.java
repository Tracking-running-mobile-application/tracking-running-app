package com.app.java.trackingrunningapp.ui.runPage;

public class MetricItem {
    private String label;
    private String value;

    public MetricItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
