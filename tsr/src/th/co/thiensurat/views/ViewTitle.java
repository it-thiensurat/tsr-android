package th.co.thiensurat.views;

import th.co.thiensurat.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewTitle extends TableRow {
	private TextView txtTitle;

	public ViewTitle(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ViewTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		if (isInEditMode()) {
			return;
		}

		LayoutInflater.from(context).inflate(R.layout.view_title, this, true);
		txtTitle = (TextView) findViewById(R.id.txtTitle);

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewTitleStyle, 0, 0);
		try {
			txtTitle.setText(ta.getString(R.styleable.ViewTitleStyle_text));
			txtTitle.setTextColor(ta.getColor(R.styleable.ViewTitleStyle_textColor, Color.BLACK));
			txtTitle.setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					ta.getDimension(R.styleable.ViewTitleStyle_textSize,
							TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16.0f, getResources().getDisplayMetrics())));
		} finally {
			ta.recycle();
		}
	}

	public void setText(int titleResourceID) {
		setText(getResources().getString(titleResourceID));
	}

	public void setText(String text) {
		txtTitle.setText(text);
	}

}
