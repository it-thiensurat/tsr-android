package th.co.thiensurat.fragments.payment.first;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

public class FirstPaymentImportDataFragment extends BHFragment {

	ExpandableListAdapter listAdapter;
	private ArrayList<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	@InjectView
	private ExpandableListView lvCust;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_first;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		// return 0;
		return R.layout.fragment_first_payment_import_data;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {

		// // get the listview
		// expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		// listAdapter = new ExpandableListAdapter(this, listDataHeader,
		// listDataChild);
		listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

		// setting list adapter
		lvCust.setAdapter(listAdapter);

		// Listview Group click listener
		lvCust.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		lvCust.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				Toast.makeText(getActivity(), listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
			}
		});

		// Listview Group collasped listener
		lvCust.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				Toast.makeText(getActivity(), listDataHeader.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
			}
		});

		// Listview on child click listener
		lvCust.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(),
						listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});

	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("12 �չҤ� 2557");
		listDataHeader.add("13 �չҤ� 2557");
		listDataHeader.add("14 �չҤ� 2557");
		// listDataHeader.add("15 �չҤ� 2557");

		// Adding child data
		List<String> top250 = new ArrayList<String>();
		top250.add("͹ѹ ͹ѹ����");
		top250.add("��ʹ� �������");
		top250.add("�ӹҭ �ͧ���");
		top250.add("�ҭ��� ��������");

		List<String> nowShowing = new ArrayList<String>();
		nowShowing.add("��� 㨴�");

		List<String> comingSoon = new ArrayList<String>();
		comingSoon.add("�չ� �ҹ�");

		listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
		listDataChild.put(listDataHeader.get(1), nowShowing);
		listDataChild.put(listDataHeader.get(2), comingSoon);

	}

	private class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Context _context;
		private List<String> _listDataHeader; // header titles
		// child data in format of header title, child title
		private HashMap<String, List<String>> _listDataChild;

		public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
			this._context = context;
			this._listDataHeader = listDataHeader;
			this._listDataChild = listChildData;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			// return 0;
			return this._listDataHeader.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			// return 0;
			return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			// return null;
			return this._listDataHeader.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			// return null;
			return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			// return 0;
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			// return 0;
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// return null;
			String headerTitle = (String) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.list_first_payment_import_list_group, null);
			}

			TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
			lblListHeader.setTypeface(null, Typeface.BOLD);
			lblListHeader.setText(headerTitle);

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// return null;

			final String childText = (String) getChild(groupPosition, childPosition);

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.list_first_payment_import_list_item, null);
			}

			TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

			txtListChild.setText(childText);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			// return false;
			return true;
		}

	}
}
