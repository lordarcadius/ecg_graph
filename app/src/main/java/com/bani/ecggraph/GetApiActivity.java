package com.bani.ecggraph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GetApiActivity extends AppCompatActivity {
    public static String newApi;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_api);

        editText = findViewById(R.id.getapi);
    }

    public void getApi(View view){
        newApi = editText.getText().toString();
        Intent intent = new Intent(GetApiActivity.this, MainActivity.class);
        intent.putExtra("Value", newApi);
        startActivity(intent);
    }
}
