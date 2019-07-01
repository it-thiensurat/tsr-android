package th.co.bighead.utilities;

import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BHSpinnerAdapter<T> extends ArrayAdapter<T> {

	public BHSpinnerAdapter(Context context, T[] objects) {
		super(context, android.R.layout.simple_spinner_item, objects);
		// TODO Auto-generated constructor stub
		this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	public BHSpinnerAdapter(Context context, List<T> objects) {
		super(context, android.R.layout.simple_spinner_item, objects);
		// TODO Auto-generated constructor stub
		this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

    public BHSpinnerAdapter(Context context, int resourceID, List<T> objects) {
        super(context, resourceID, objects);
        // TODO Auto-generated constructor stub
        this.setDropDownViewResource(resourceID);
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		TextView tv = (TextView)super.getView(position, convertView, parent);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        setupView(tv, getItem(position));
		return tv;
	}

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView tv = (TextView)super.getDropDownView(position, convertView, parent);
        setupView(tv, getItem(position));
        return tv;
    }

    protected void setupView(TextView tv, T item) {
    }

}
