package com.chrissetiana.intentsified;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.entry_text);
        button = findViewById(R.id.button_entry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textView.getText().toString().trim();
                Context context = MainActivity.this;
                Class destination = ChildActivity.class;
                Intent intent = new Intent(context, destination);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intent);
            }
        });
    }

    public void onClickOpenWebpageButton(View view) {
        String url = "https://developer.android.com/guide/components/intents-common";
        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        Log.d("MainActivity", uri.toString());
    }

    public void onClickOpenAddressButton(View view) {
        String address = "1600 Amphitheatre Parkway, CA";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .query(address);
        Uri uri = builder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        Log.d("MainActivity", uri.toString());
    }

    public void onClickShareTextButton(View view) {
        String text = "Sharing the coolest thing I've learned so far. You should check out Udacity and Google's Android Nanodegree!";
        String type = "text/plain";
        String title = "Learning How to Share";

        ShareCompat.IntentBuilder
                .from(this)
                .setType(type)
                .setChooserTitle(title)
                .setText(text)
                .startChooser();

        Log.d("MainActivity", "\nFrom: " + this + "\nType: " + type + "\nMessage: " + text);
    }

    public void createYourOwn(View view) {
        String title = "Graduation Ceremony";
        String location = "Udacity HeadQuarters";

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2018, 11, 01, 10, 00, 00);
        long begin = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        beginTime.set(2018, 11, 01, 12, 00, 00);
        long end = beginTime.getTimeInMillis();

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        Log.d("MainActivity", intent.toString());
    }
}
