package th.co.thiensurat.adapter;

public class Column {
    private String text;
    private Style style;

    public Column(String text) {
        this.text = text;
    }

    public Column(String text, Style style) {
        this.text = text;
        this.style = style;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
