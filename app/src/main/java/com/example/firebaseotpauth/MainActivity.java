package com.example.firebaseotpauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {

    CountryCodePicker codePicker;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codePicker=findViewById(R.id.countryCodePicker);
        editText=findViewById(R.id.edt1);

        codePicker.registerCarrierNumberEditText(editText);


    }

    public void generateotp(View view) {
        Intent intent=new Intent(MainActivity.this,Login.class);
        intent.putExtra("mobile",codePicker.getFullNumberWithPlus().replace(" ",""));
        startActivity(intent);

    }
}