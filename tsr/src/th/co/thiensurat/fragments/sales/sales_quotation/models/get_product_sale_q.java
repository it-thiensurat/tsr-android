package th.co.thiensurat.fragments.sales.sales_quotation.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class get_product_sale_q {
    private String product_id;
    private String product_name;
    private String product_qty;
    private String product_price;

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    private List<ProductSpecModel> product_spec;

    public List<ProductSpecModel> getProduct_spec() {
        return product_spec;
    }

    public void setProduct_spec(List<ProductSpecModel> product_spec) {
        this.product_spec = product_spec;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }
}
