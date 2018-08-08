package com.chrissetiana.intentsified;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.entry_text);
        button = findViewById(R.id.entry_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Toast.makeText(context, "Button clicked!\nTODO: Start a new Activity and pass some data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
