package org.hssus.khel.hsskhel.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.hssus.khel.hsskhel.R;
import org.hssus.khel.hsskhel.models.SignUpModel;
import org.hssus.khel.hsskhel.services.SingupService;
import org.hssus.khel.hsskhel.util.Constant;

public class SignUp extends BaseActivity {

    private class KhelReceiver extends BroadcastReceiver {
        private KhelReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.actions.KHEL_SIGN_UP:
                    if (!intent.getBooleanExtra(Constant.extra.ERROR, false)) {
                        showToast("User created successfully.");
                        finish();
                    } else {
                        showToast(intent.getStringExtra(Constant.extra.ERROR_MESSAGE));
                    }
                    break;
            }
        }
    }

    public static final String TAG = SignUp.class.getName();
    private KhelReceiver khelReceiver;
    LocalBroadcastManager localBroadcastManager;
    EditText txtFirstName,txtLastName,txtShakhaName,txtAddress,txtCity,txtState,txtZip,txtCountry,txtEmail,txtPhone,txtPassword;
    Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initBroadcastReceiver();
        loadUIComponent();
    }

    private void loadUIComponent(){

        txtFirstName=(EditText)findViewById(R.id.txtFirstName);
        txtLastName=(EditText)findViewById(R.id.txtLastName);
        txtShakhaName=(EditText)findViewById(R.id.txtShakhaName);
        txtAddress=(EditText)findViewById(R.id.txtAddress);
        txtCity=(EditText)findViewById(R.id.txtCity);
        txtState=(EditText)findViewById(R.id.txtState);
        txtZip=(EditText)findViewById(R.id.txtZip);
        txtCountry=(EditText)findViewById(R.id.txtCountry);
        txtEmail=(EditText)findViewById(R.id.txtEmail);
        txtPhone=(EditText)findViewById(R.id.txtPhone);
        txtPassword=(EditText)findViewById(R.id.txtPassword);

        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpModel model = new SignUpModel();
                model.setFirstName(txtFirstName.getText().toString());
                model.setLastName(txtLastName.getText().toString());
                model.setShakhaName(txtShakhaName.getText().toString());
                model.setAddress(txtAddress.getText().toString());
                model.setCity(txtCity.getText().toString());
                model.setState(txtState.getText().toString());
                model.setZip(txtZip.getText().toString());
                model.setCountry(txtCountry.getText().toString());
                model.setEmailAddress(txtEmail.getText().toString());
                model.setPhoneNumber(txtPhone.getText().toString());
                model.setPassword(txtPassword.getText().toString());

                SingupService.startSingupService(SignUp.this,model.toJson().toString());
            }
        });
    }

    private void initBroadcastReceiver() {
        khelReceiver = new KhelReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter khelServiceFilter = new IntentFilter(Constant.actions.KHEL_SIGN_UP);

        localBroadcastManager.registerReceiver(khelReceiver, khelServiceFilter);
    }
}
