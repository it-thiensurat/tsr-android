package th.co.bighead.utilities.ThaiNationalIDCard;

public interface IBHThaiIDCard {
    void onSuccess(BHThaiIDCard bhThaiIDCard, Personal personal);
    void onNotSuccess(BHThaiIDCard bhThaiIDCard, BHThaiIDCard.ResultNotSuccess result);
}