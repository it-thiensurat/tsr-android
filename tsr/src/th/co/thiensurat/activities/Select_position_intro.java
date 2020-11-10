package th.co.thiensurat.activities;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.LinearLayout;

        import th.co.bighead.utilities.BHActivity;
        import th.co.bighead.utilities.BHApplication;
        import th.co.bighead.utilities.BHPreference;
        import th.co.thiensurat.BuildConfig;
        import th.co.thiensurat.R;
        import com.crashlytics.android.Crashlytics;
        import io.fabric.sdk.android.Fabric;
        import th.co.thiensurat.service.data.AuthenticateInputInfo;

        import static th.co.bighead.utilities.BHPreference.pp3;

public class Select_position_intro extends BHActivity {

    LinearLayout sale_button,credit_button;
    LoginActivity LoginActivity;
    private AuthenticateInputInfo input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.select_position_user);
        sale_button = (LinearLayout) findViewById(R.id.sale_button);
        credit_button = (LinearLayout) findViewById(R.id.credit_button);

        input = new AuthenticateInputInfo();


        sale_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BHPreference.setSourceSystem("Sale");
                BHApplication.getInstance().getPrefManager().setPreferrence("pp3", "2");
                Intent intent = new Intent(Select_position_intro.this, LoginActivity.class);
                Bundle bun = new Bundle();
                bun.putString("position", "Sale");
                bun.putString("UserName", input.UserName);
                bun.putString("Password", input.Password);

                intent.putExtras(bun);
                startActivity(intent);


                //LoginActivity=new LoginActivity();
                //LoginActivity.login(input.UserName,input.Password);
            }
        });

        credit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BHPreference.setSourceSystem("Credit");
                BHApplication.getInstance().getPrefManager().setPreferrence("pp3", "2");

                Intent intent = new Intent(Select_position_intro.this, LoginActivity.class);
                Bundle bun = new Bundle();
                bun.putString("position", "Credit");
                bun.putString("UserName", input.UserName);
                bun.putString("Password", input.Password);

                intent.putExtras(bun);
                startActivity(intent);

               // LoginActivity=new LoginActivity();
               // LoginActivity.login(input.UserName,input.Password);

            }
        });


    }

}
