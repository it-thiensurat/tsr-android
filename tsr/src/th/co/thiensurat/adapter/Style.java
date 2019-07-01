package th.co.thiensurat.adapter;

public class Style {

    private int color;
    private int typeface;
    private int gravity;
    private float layout_weight;

    public static Style Color(int color) {
        return new Style(color, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }

    public static Style Typeface(int typeface) {
        return new Style(Integer.MIN_VALUE, typeface, Integer.MIN_VALUE, Float.MIN_VALUE);
    }

    public static Style Gravity(int gravity) {
        return new Style(Integer.MIN_VALUE, Integer.MIN_VALUE, gravity, Float.MIN_VALUE);
    }

    public static Style LayoutWeight(float layout_weight) {
        return new Style(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, layout_weight);
    }

    public Style(int gravity, float layout_weight) {
        this.color = Integer.MIN_VALUE;
        this.typeface = Integer.MIN_VALUE;
        this.gravity = gravity;
        this.layout_weight = layout_weight;
    }

    public Style(int color, int gravity, float layout_weight) {
        this.color = color;
        this.typeface = Integer.MIN_VALUE;
        this.gravity = gravity;
        this.layout_weight = layout_weight;
    }

    public Style(int gravity, float layout_weight, int typeface) {
        this.color = Integer.MIN_VALUE;
        this.typeface = typeface;
        this.gravity = gravity;
        this.layout_weight = layout_weight;
    }

    public Style(int color, int typeface, int gravity, float layout_weight) {
        this.color = color;
        this.typeface = typeface;
        this.gravity = gravity;
        this.layout_weight = layout_weight;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTypeface() {
        return typeface;
    }

    public void setTypeface(int typeface) {
        this.typeface = typeface;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public float getLayout_weight() {
        return layout_weight;
    }

    public void setLayout_weight(float layout_weight) {
        this.layout_weight = layout_weight;
    }
}
