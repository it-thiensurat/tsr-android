package th.co.thiensurat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.thiensurat.R;

public class ContractAdapter extends BHArrayAdapter {

    private Context context;
    private int resource;
    private List objects;
    private boolean isColor;

    public ContractAdapter(Context _context, int _resource, List _objects) {
        super(_context, _resource, _objects);

        this.context = _context;
        this.resource = _resource;
        this.objects = _objects;
    }

    public ContractAdapter(Context _context, int _resource, List _objects, boolean _isColor) {
        super(_context, _resource, _objects);

        this.context = _context;
        this.resource = _resource;
        this.objects = _objects;
        this.isColor = _isColor;
    }

    private class ViewHolder {
        public TextView txtCONTNO, txtCustomerFullName, txtStatus;
    }

    @Override
    protected void onViewItem(int position, View view, Object holder, Object info) {
        ViewHolder vh = (ViewHolder) holder;
        Class c = info.getClass();
        Field[] fields = c.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                f.setAccessible(true);

                Class<?> type = f.getType();

                if (f.getName().equalsIgnoreCase("CONTNO") && type.equals(String.class))
                    vh.txtCONTNO.setText(getStringValue(f, info));
                if (f.getName().equalsIgnoreCase("CustomerFullName") && type.equals(String.class))
                    vh.txtCustomerFullName.setText(getStringValue(f, info));
                if (view.findViewById(R.id.txtStatus) != null && f.getName().equalsIgnoreCase("Status") && type.equals(String.class))
                    vh.txtStatus.setText(getStatus(getStringValue(f, info)));
                if(isColor && f.getName().equalsIgnoreCase("HoldSalePaymentPeriod") && type.equals(int.class))
                    vh.txtCustomerFullName.setTextColor(this.context.getResources().getColor(getColor(getIntValue(f, info))));
            }
        }
    }

    public int getColor(int HoldSalePaymentPeriod) {
        switch (HoldSalePaymentPeriod) {
            case 0:
                return R.color.hold_payment_0;
            case 1:
                return R.color.hold_payment_1;
            case 2:
                return R.color.hold_payment_2;
            default:
                return R.color.hold_payment_3;
        }
    }

    public enum Status {
        REQUEST, APPROVED, COMPLETED, REJECTED
    }

    public String getStatus(String s) {
        if (s.equals(Status.REQUEST.toString())) {
            return "N";
        } else if (s.equals(Status.APPROVED.toString())) {
            return "Y";
        } else {
            return "";
        }
    }

    public String getStringValue(Field f, Object info) {
        try {
            if (f.get(info) != null) {
                return (String) f.get(info);
            }
        } catch (IllegalAccessException e) {
            return "";
        }
        return "";
    }

    public int getIntValue(Field f, Object info) {
        try {
            if (f.get(info) != null) {
                return (int) f.get(info);
            }
        } catch (IllegalAccessException e) {
            return 0;
        }
        return 0;
    }
}
