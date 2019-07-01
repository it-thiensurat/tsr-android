package th.co.bighead.utilities;

public abstract class BHPagerFragment extends BHFragment {
	protected BHFragment parent;
	
	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends BHFragment> T newInstance(Class<T> cls, BHParcelable data, BHFragment parent) {
		BHPagerFragment instance = (BHPagerFragment) BHFragment.newInstance(cls, data);
		instance.parent = parent;

		return (T) instance;
	}
}
