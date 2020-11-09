package main;

public enum Type {
    Button_2Corners("width", "height"),
    Button_1Corner("width", "height"),
    Button_CutOff("w1", "d", "w2", "h"),
    Button_Circle("diameter"),
    Button_Rectangle("width", "height"),
    Item_Bulge("w1", "h1", "w2", "h2", "w3"),
    Item_Corner_Right("w1", "h1", "w2", "h2", "r1", "r2");

    private String[] measurements;

    Type(String... measurements) {
        this.measurements = measurements;
    };

    public String[] getMeasurements() {
        return measurements;
    }
}
