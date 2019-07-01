package th.co.bighead.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import th.co.thiensurat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

public abstract class BHArrayAdapter<T> extends ArrayAdapter<T> implements Filterable {
	private int mResource;
	protected List<T> originalList;
	protected List<T> filterList;

	public BHArrayAdapter(Context context, int resource, T[] objects) {
		super(context, resource, objects);
		mResource = resource;
		
		List<T> list = Arrays.asList(objects);
		if (list == null) {
			list = new ArrayList<T>();
		}
		
		originalList = list;
		filterList = originalList;
	}

	public BHArrayAdapter(Context context, int resource, List<T> objects) {
		super(context, resource, objects);
		mResource = resource;
		if (objects == null) {
			objects = new ArrayList<T>();
		}
		
		originalList = objects;
		filterList = originalList;
	}

	protected abstract void onViewItem(int position, View view, Object holder, T info);

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filterList.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return filterList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return filterList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(mResource, parent, false);

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
		T info = getItem(position);
		onViewItem(position, view, holder, info);

		return view;
	}

}
