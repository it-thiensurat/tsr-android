package th.co.thiensurat.fragments.sales.sales_quotation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.R;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;

public class ProductSelectedAdapter extends RecyclerView.Adapter<ProductSelectedAdapter.ItemRowHolder> {

    List<get_product_sale_q> selectedProductList;
    public ProductSelectedAdapter(List<get_product_sale_q> selectedProductList) {
        this.selectedProductList = selectedProductList;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.quotation_detail_adapter, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder itemRowHolder, int i) {
        get_product_sale_q productSaleQ = selectedProductList.get(i);
        double netItemPrice = 0;
        int qty = Integer.parseInt(productSaleQ.getProduct_qty());
        double price = Double.parseDouble(productSaleQ.getProduct_price());
        netItemPrice = qty * price;
        itemRowHolder.productName.setText((i + 1) + ". " + productSaleQ.getProduct_name());
        itemRowHolder.qty.setText(productSaleQ.getProduct_qty() + " เครื่อง");
        itemRowHolder.unitPrice.setText(BHUtilities.numericFormat(Double.parseDouble(productSaleQ.getProduct_price())) + " บาท");
        itemRowHolder.totalPrice.setText(BHUtilities.numericFormat(netItemPrice) + " บาท");
    }

    @Override
    public int getItemCount() {
        return selectedProductList.size();
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        private TextView qty, unitPrice, totalPrice, productName;

        public ItemRowHolder(View view) {
            super(view);
            qty = (TextView) view.findViewById(R.id.qty);
            unitPrice = (TextView) view.findViewById(R.id.unitPrice);
            totalPrice = (TextView) view.findViewById(R.id.totalPrice);
            productName = (TextView) view.findViewById(R.id.productName);
        }
    }
}
