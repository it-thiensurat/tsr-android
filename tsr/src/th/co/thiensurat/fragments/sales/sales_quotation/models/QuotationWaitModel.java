package th.co.thiensurat.fragments.sales.sales_quotation.models;

import java.util.List;

public class QuotationWaitModel {

    private String quotationId;
    private String quotationDate;
    private String quotationDiscount;
    private String quotationProjectName;
    private int quotationStatus;
    private String quotationComment;
    private String quotationStatusText;
    private List<CustomerAPModel> customerAPModelList;
    private List<get_product_sale_q> productList;

    public String getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(String quotationId) {
        this.quotationId = quotationId;
    }

    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getQuotationDiscount() {
        return quotationDiscount;
    }

    public void setQuotationDiscount(String quotationDiscount) {
        this.quotationDiscount = quotationDiscount;
    }

    public String getQuotationProjectName() {
        return quotationProjectName;
    }

    public void setQuotationProjectName(String quotationProjectName) {
        this.quotationProjectName = quotationProjectName;
    }

    public int getQuotationStatus() {
        return quotationStatus;
    }

    public void setQuotationStatus(int quotationStatus) {
        this.quotationStatus = quotationStatus;
    }

    public String getQuotationComment() {
        return quotationComment;
    }

    public void setQuotationComment(String quotationComment) {
        this.quotationComment = quotationComment;
    }

    public String getQuotationStatusText() {
        return quotationStatusText;
    }

    public void setQuotationStatusText(String quotationStatusText) {
        this.quotationStatusText = quotationStatusText;
    }

    public List<CustomerAPModel> getCustomerAPModelList() {
        return customerAPModelList;
    }

    public void setCustomerAPModelList(List<CustomerAPModel> customerAPModelList) {
        this.customerAPModelList = customerAPModelList;
    }

    public List<get_product_sale_q> getProductList() {
        return productList;
    }

    public void setProductList(List<get_product_sale_q> productList) {
        this.productList = productList;
    }
}
