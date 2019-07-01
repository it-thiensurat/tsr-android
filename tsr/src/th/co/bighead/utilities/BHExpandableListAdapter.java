package th.co.bighead.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

import th.co.thiensurat.R;
import th.co.thiensurat.adapter.ExpandableHeader;
import th.co.thiensurat.adapter.Table;

public abstract class BHExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ExpandableHeader> listDataHeader;
    private HashMap<ExpandableHeader, Table> listDataChild;
    private int resourceHeader;
    private int resourceChild;

    public BHExpandableListAdapter(Context _context, int _resourceHeader, int _resourceChild, List<ExpandableHeader> _listDataHeader, HashMap<ExpandableHeader, Table> _listDataChild) {
        this.context = _context;
        this.listDataHeader = _listDataHeader;
        this.listDataChild = _listDataChild;
        this.resourceHeader = _resourceHeader;
        this.resourceChild = _resourceChild;
    }

    protected abstract void onGroupViewItem(int groupPosition, boolean isExpanded, View view, Object holder, ExpandableHeader info);

    protected abstract void onChildViewItem(int groupPosition, int childPosition, boolean isLastChild, View view, Object holder, Table info);

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public ExpandableHeader getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Table getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(this.resourceHeader, parent, false);

            Class<?> enclosingClass = this.getClass();
            Class<?>[] clss = enclosingClass.getDeclaredClasses();
            Class<?> cc = null;
            if (clss != null && clss.length > 0) {
                for (Class<?> cls : clss) {
                    if (cls.getSimpleName().equals("GroupViewHolder")) {
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
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        Object holder = view.getTag();
        ExpandableHeader info = getGroup(groupPosition);
        onGroupViewItem(groupPosition, isExpanded, view, holder, info);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(this.resourceChild, parent, false);

            Class<?> enclosingClass = this.getClass();
            Class<?>[] clss = enclosingClass.getDeclaredClasses();
            Class<?> cc = null;
            if (clss != null && clss.length > 0) {
                for (Class<?> cls : clss) {
                    if (cls.getSimpleName().equals("ChildViewHolder")) {
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
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        Object holder = view.getTag();
        Table info = getChild(groupPosition, childPosition);
        onChildViewItem(groupPosition, childPosition, isLastChild, view, holder, info);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
