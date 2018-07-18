package pl.alkosoft.odliczaczor.data;

public final class RGBColor {

    private float red;
    private float green;
    private float blue;
    private float alpha;

    public RGBColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }
}
