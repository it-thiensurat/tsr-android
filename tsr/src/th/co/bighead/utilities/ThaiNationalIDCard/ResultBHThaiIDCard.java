package th.co.bighead.utilities.ThaiNationalIDCard;

public abstract class ResultBHThaiIDCard implements IBHThaiIDCard {
    @Override
    public void onSuccess(BHThaiIDCard bhThaiIDCard, Personal personal) {
        bhThaiIDCard.Close();
    }

    @Override
    public void onNotSuccess(BHThaiIDCard bhThaiIDCard, BHThaiIDCard.ResultNotSuccess result) {
        bhThaiIDCard.Close();
    }
}