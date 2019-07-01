package th.co.thiensurat.fragments.share;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.PrinterController;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

public class PrinterSelectionFragment extends BHFragment {
	@InjectView
	private ListView lvDeviceList;

	private PrinterController controller;
	private BluetoothDevice device;

	private List<BluetoothDevice> pairDevices;
	private List<BluetoothDevice> searchDevices;

	public PrinterSelectionFragment(PrinterController controller, BluetoothDevice device) {
		this.controller = controller;
		this.device = device;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_printer_selection;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_search, R.string.button_ok };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

//		pairDevices = controller.getPairDevices();

		ArrayAdapter<Object> deviceAdapter = new ArrayAdapter<Object>(activity, android.R.layout.simple_list_item_1) {
			class ViewHolder {
				TextView tvHeader;
				CheckedTextView ctvTitle;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				Object item = getItem(position);
				boolean isHeader = item.getClass().equals(String.class);
				int resID = isHeader ? android.R.layout.simple_list_item_1 : android.R.layout.simple_list_item_single_choice;
				if (convertView == null) {
					convertView = LayoutInflater.from(getContext()).inflate(resID, parent, false);
					ViewHolder holder = new ViewHolder();
					if (isHeader) {
						holder.tvHeader = (TextView) convertView.findViewById(android.R.id.text1);
					} else {
						holder.ctvTitle = (CheckedTextView) convertView.findViewById(android.R.id.text1);
					}

					convertView.setTag(holder);
				}

				ViewHolder holder = (ViewHolder) convertView.getTag();
				if (holder != null) {
					if (isHeader) {
						String header = (String) item;
						holder.tvHeader.setText(header);
						convertView.setOnClickListener(null);
						convertView.setOnLongClickListener(null);
						convertView.setClickable(false);
						convertView.setLongClickable(false);
						convertView.setEnabled(false);
					} else {
						BluetoothDevice device = (BluetoothDevice) item;
						holder.ctvTitle.setText(device.getName());
						if (PrinterSelectionFragment.this.device != null) {
							holder.ctvTitle.setChecked(device.getAddress().equals(PrinterSelectionFragment.this.device.getAddress()));
						}
					}
				}

				return convertView;
			}
		};

		if (pairDevices != null && pairDevices.size() > 0) {
			deviceAdapter.add("อุปกรณ์ที่จับคู่");
			deviceAdapter.addAll(pairDevices);
		}

		lvDeviceList.setAdapter(deviceAdapter);

	}

}
