package th.co.bighead.utilities;

public class BHListViewItem {
	public enum RowType {
		Item, Header, Footer
	}
	
	public RowType type;
	public int section;
	public int row;

	public BHListViewItem(RowType type, int section) {
		this(type, section, -1);
	}

	public BHListViewItem(RowType type, int section, int row) {
		this.type = type;
		this.section = section;
		this.row = row;
	}

}
