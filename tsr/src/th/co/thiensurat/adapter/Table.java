package th.co.thiensurat.adapter;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public List<Row> rows;
    private Style[] styles;

    public Table(){
        this.rows = new ArrayList<>();
        this.styles = new Style[]{};
    }

    public Table(List<Row> rows) {
        this.rows = rows;
    }

    public Table(List<Row> rows, Style[] styles) {
        this.rows = rows;
        setStyles(styles);
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public Style[] getStyles() {
        return styles;
    }

    public void setStyles(Style[] styles) {
        this.styles = styles;
        if (!(this.styles != null && this.styles.length > 0)) return;
        for (Row row : rows) {
            int i = 0;
            for (Column c : row.getFields()) {
                Style style = this.styles[i];
                if (c.getStyle() == null) {
                    c.setStyle(new Style(style.getColor(), style.getTypeface(), style.getGravity(), style.getLayout_weight()));
                } else {
                    if (style.getColor() > Integer.MIN_VALUE)
                        c.getStyle().setColor(style.getColor());
                    if (style.getTypeface() > Integer.MIN_VALUE)
                        c.getStyle().setTypeface(style.getTypeface());
                    if(this.styles.length == row.getFields().size()) {
                        if (style.getGravity() > Integer.MIN_VALUE && c.getStyle().getGravity() == Integer.MIN_VALUE)
                            c.getStyle().setGravity(style.getGravity());
                        if (style.getLayout_weight() > Float.MIN_VALUE && c.getStyle().getLayout_weight() == Float.MIN_VALUE)
                            c.getStyle().setLayout_weight(style.getLayout_weight());
                    }
                }
                i++;
            }
        }
    }
}
