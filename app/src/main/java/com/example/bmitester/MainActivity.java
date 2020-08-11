package com.example.bmitester;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    InputMethodManager imm;
    Button button_submit;
    Button button_refresh;
    EditText editText_height;
    EditText editText_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        button_submit = findViewById(R.id.Button_submit);
        button_refresh = findViewById(R.id.Button_refresh);
        editText_height = findViewById(R.id.EditText_height);
        editText_weight = findViewById(R.id.EditText_weight);

        hideKeyboard();

        editText_weight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo
                            .IME_ACTION_DONE:
                        checkInput();
                        break;
                    default:
                        return false;
                }

                return false;
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("submit button clicked");
                checkInput();
                hideKeyboard();
            }
        });

        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    private void checkInput(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage("your input value is not valid");
        alertDialog.setTitle("error !");

        String height = editText_height.getText().toString();
        String weight = editText_weight.getText().toString();

        if (!height.isEmpty() && !weight.isEmpty()){
            // Edit text is not null
            
            // float 형 변환 & 단위 변환
            float hght = Float.parseFloat(height) / 100;        // 단위 : m
            float wght = Float.parseFloat(weight);              // 단위 : kg

            if (hght < 1 || wght > 200){
                // input value is not valid
                alertDialog.setMessage("your input is not valid");
                AlertDialog alert = alertDialog.create();
                alert.show();
            } else {
                // input value is valid
                float BMI = wght / ( hght * hght );
                Log.d("BMI value", Float.toString(BMI));

                calculateBMI(BMI);
            }

        } else {
            // Edit text is null !
            Toast.makeText(getApplicationContext(),"You didn't fill the blanks.", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateBMI(float bmi){

        TextView textView_bmi = findViewById(R.id.TextView_bmi);
        textView_bmi.setText(Float.toString(bmi));

        LinearLayout linearLayout_result = findViewById(R.id.LinearLayout_result);
        linearLayout_result.setVisibility(View.VISIBLE);

        String level;

        if (bmi < 18.5) {
            level = "저체중";
            // 저체중
        } else if (bmi < 23) {
            level = "정상";
            // 정상
        } else if (bmi < 25) {
            level = "과체중";
            // 과체중
        } else {
            level = "비만";
            // 비만
        }

        TextView textView_description = findViewById(R.id.TextView_description);
        textView_description.setText("당신은 "+ level + "에 속합니다.");
    }

    private void hideKeyboard(){
        imm.hideSoftInputFromWindow(editText_height.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editText_weight.getWindowToken(), 0);
    }
}