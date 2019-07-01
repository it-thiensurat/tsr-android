package th.co.bighead.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHListViewItem.RowType;
import th.co.thiensurat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

public abstract class BHListViewAdapter extends ArrayAdapter<BHListViewItem> implements Filterable, PinnedSectionListAdapter {

	private boolean refreshData;
	private List<BHListViewItem> data;

	public BHListViewAdapter(Context context) {
		super(context, 0);
		refreshData = true;
	}

	protected abstract int viewForItem(int section, int row);

	protected abstract int getItemCount(int section);

	protected abstract void onViewItem(View view, Object holder, int section, int row);

	protected int getSectionCount() {
		return 1;
	}

	protected int viewForSectionHeader(int section) {
		return 0;
	}

	protected int viewForSectionFooter(int section) {
		return 0;
	}
	
	protected void onViewSectionHeader(View view, Object holder, int section) {
	}
	
	protected void onViewSectionFooter(View view, Object holder, int section) {
	}
	
	protected boolean canHeaderSelected(int section) {
		return false;
	}
	
	protected boolean canFooterSelected(int section) {
		return false;
	}

	@Override
	public synchronized int getCount() {
		// TODO Auto-generated method stub
		if (refreshData) {
			data = new ArrayList<BHListViewItem>();
			for (int section = 0; section < getSectionCount(); section++) {
				if (viewForSectionHeader(section) > 0) {
					data.add(new BHListViewItem(RowType.Header, section));
				}

				for (int row = 0; row < getItemCount(section); row++) {
					data.add(new BHListViewItem(RowType.Item, section, row));
				}

				if (viewForSectionFooter(section) > 0) {
					data.add(new BHListViewItem(RowType.Footer, section));
				}
			}

			refreshData = false;
		}

		return data.size();
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		BHListViewItem item = data.get(position);
		return item.type == RowType.Item || (item.type == RowType.Header && canHeaderSelected(item.section)) || (item.type == RowType.Footer && canFooterSelected(item.section));
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		refreshData = true;
		super.notifyDataSetChanged();
	}
	
	@Override
	public BHListViewItem getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return data.get(position).hashCode();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		BHListViewItem item = (BHListViewItem) getItem(position);
		int resourceID = 0;
		switch (item.type) {
		case Header:
			resourceID = viewForSectionHeader(item.section);
			break;

		case Item:
			resourceID = viewForItem(item.section, item.row);
			break;

		case Footer:
			resourceID = viewForSectionFooter(item.section);
			break;

		default:
			break;
		}

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);

			Class<?> enclosingClass = this.getClass();
			Class<?>[] clss = enclosingClass.getDeclaredClasses();
			Class<?> cc = null;
			if (clss != null && clss.length > 0) {
				for (Class<?> cls : clss) {
					if (cls.getSimpleName().equals("ViewHolder")) {
						cc = cls;
						break;
					}
				}

				if (cc != null) {
					try {
						Constructor<?> ctor = cc.getDeclaredConstructor(enclosingClass);
						ctor.setAccessible(true);
						Object obj = ctor.newInstance(this);
						for (Field fld : cc.getDeclaredFields()) {
							int modifier = fld.getModifiers();
							if (Modifier.isPublic(modifier)) {
								Field fd;
								int id = 0;
								try {
									fd = R.id.class.getDeclaredField(fld.getName());
									id = fd.getInt(null);
								} catch (NoSuchFieldException e) {
                                    fd = android.R.id.class.getDeclaredField(fld.getName());
                                    id = fd.getInt(null);
								}

								if (id > 0) {
									View vw = view.findViewById(id);
									fld.set(obj, vw);
								}
							}
						}

						view.setTag(obj);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
                        throw new RuntimeException(e);
					}
				}
			}
		}

		Object holder = view.getTag();
		switch (item.type) {
		case Header:
			onViewSectionHeader(view, holder, item.section);
			break;

		case Item:
			onViewItem(view, holder, item.section, item.row);
			break;

		case Footer:
			onViewSectionFooter(view, holder, item.section);
			break;

		default:
			break;
		}

		return view;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return data.get(position).type.ordinal();
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return viewType == RowType.Header.ordinal();
	}
}
