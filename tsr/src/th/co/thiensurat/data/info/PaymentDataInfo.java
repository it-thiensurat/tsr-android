package th.co.thiensurat.data.info;

import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class PaymentDataInfo extends BHParcelable {
	public ContractInfo contract;
	public DebtorCustomerInfo customer;
	public ProductInfo product;
	public AddressInfo defaultAddress;
	public AddressInfo installAddress;
	public AddressInfo paymentAddress;
	public TripInfo trip;
	public List<SalePaymentPeriodInfo> periods;
}
