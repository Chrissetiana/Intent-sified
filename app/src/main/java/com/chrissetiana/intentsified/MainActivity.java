package com.chrissetiana.intentsified;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ImageView imageView;
    private EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_entry);
        Button button = findViewById(R.id.button_entry);
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

    public void onClickOpenWebpage(View view) {
        String url = "https://chrissetiana.github.io";
        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        Log.d(LOG_TAG, uri.toString());
    }

    public void onClickOpenAddress(View view) {
        String address = "Abuja, Nigeria";
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

        Log.d(LOG_TAG, uri.toString());
    }

    public void onClickShareText(View view) {
        String text = "Sharing the coolest thing I've learned so far.";
        String type = "text/plain";
        String title = "Learning How to Share";

        ShareCompat.IntentBuilder
                .from(this)
                .setType(type)
                .setChooserTitle(title)
                .setText(text)
                .startChooser();

        Log.d(LOG_TAG, "\nFrom: " + this + "\nType: " + type + "\nMessage: " + text);
    }

    public void onClickSaveToCalendar(View view) {
        String title = "Birthday Celebration";
        String location = "My House";

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2019, 11, 13, 00, 00, 00);
        long begin = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        beginTime.set(2019, 11, 13, 11, 59, 59);
        long end = endTime.getTimeInMillis();

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        Log.d(LOG_TAG, intent.toString());
    }

    public void onClickSendEmail(View view) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Happy Birthday");
        intent.putExtra(Intent.EXTRA_TEXT, "You are invited to my birthday!");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onClickTakePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        }

        Log.d(LOG_TAG, "Camera launched");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            imageView.setImageBitmap(bitmap);
        }
    }
}