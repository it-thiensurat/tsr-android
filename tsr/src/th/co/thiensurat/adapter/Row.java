package th.co.thiensurat.adapter;

import java.util.List;

public class Row {
    private List<Column> fields;
    private int layoutParams;
    private boolean line;
    private Style style;

    public Row(List<Column> fields, int layoutParams, boolean line, Style style) {
        this.fields = fields;
        this.layoutParams = layoutParams;
        this.line = line;
        setStyle(style);
    }

    public Row(List<Column> fields) {
        this.fields = fields;
    }

    public Row(List<Column> fields, boolean line) {
        this.fields = fields;
        this.line = line;
    }

    public Row(List<Column> fields, Style style) {
        this.fields = fields;
        setStyle(style);
    }

    public Row(List<Column> fields, boolean line, Style style) {
        this.fields = fields;
        this.line = line;
        setStyle(style);
    }

    public List<Column> getFields() {
        return fields;
    }

    public void setFields(List<Column> fields) {
        this.fields = fields;
    }

    public int getLayoutParams() {
        return layoutParams;
    }

    public void setLayoutParams(int layoutParams) {
        this.layoutParams = layoutParams;
    }

    public boolean isLine() {
        return line;
    }

    public void setLine(boolean line) {
        this.line = line;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
        if (this.style == null) return;
        for (Column c : fields) {
            if (c.getStyle() == null) {
                c.setStyle(new Style(style.getColor(), style.getTypeface(), style.getGravity(), style.getLayout_weight()));
            } else {
                if (this.style.getColor() > Integer.MIN_VALUE && c.getStyle().getColor() == Integer.MIN_VALUE)
                    c.getStyle().setColor(this.style.getColor());
                if (this.style.getTypeface() > Integer.MIN_VALUE && c.getStyle().getTypeface() == Integer.MIN_VALUE)
                    c.getStyle().setTypeface(this.style.getTypeface());
                if (this.style.getGravity() > Integer.MIN_VALUE && c.getStyle().getGravity() == Integer.MIN_VALUE)
                    c.getStyle().setGravity(this.style.getGravity());
                if (this.style.getLayout_weight() > Float.MIN_VALUE && c.getStyle().getLayout_weight() == Float.MIN_VALUE)
                    c.getStyle().setLayout_weight(this.style.getLayout_weight());
            }
        }
    }
}
