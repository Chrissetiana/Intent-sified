// Fix image intent

package com.chrissetiana.intentsified;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ImageView imageView;
    private EditText textView;
    private String imagePath = "";
    private String imageFilePath;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_entry);
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

        imageView = findViewById(R.id.image_view);
    }

    public void onClickOpenWebpage(View view) {
        String url = "https://chrissetiana.github.io";
        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        checkActivity(intent);

        Log.d(LOG_TAG, "Redirecting to " + uri.toString());
    }

    public void onClickOpenMap(View view) {
        String address = "Abuja, Nigeria";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .appendQueryParameter("q", address);
        Uri uri = builder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        checkActivity(intent);

        Log.d(LOG_TAG, "Locating " + uri.toString());
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

        Log.d(LOG_TAG, "From: " + this + " ///// Type: " + type + " ///// Message: " + text);
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

        checkActivity(intent);

        Log.d(LOG_TAG, intent.toString());
    }

    public void onClickSendEmail(View view) {
        String address = "chrissetiana@gmail.com";
        String subject = "Happy Birthday";
        String message = "You are invited to my birthday!";

        Intent intent = new Intent();
        intent.setData(Uri.parse("mailto:" + address));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        checkActivity(intent);

        Log.d(LOG_TAG, "Sending email to " + address);
    }

    public void onClickTakePicture(View view) {
//        File file = getFile();
//        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }

            Log.d(LOG_TAG, "Camera launched");
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();

        return image;
    }

//    @NonNull
//    private File getFile() {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String fileName = timeStamp + ".jpg";
//
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        imagePath = storageDir.getAbsolutePath() + "/" + fileName;
//
//        return new File(imagePath);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // For small thumbnail
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            imageView.setImageBitmap(bitmap);

            // For large photo
            File file = new File(imagePath);

            if (file.exists()) {
                Bitmap bitmap2 = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView.setImageBitmap(bitmap2);
            }
        }
    }

    public void onClickSetReminder(View view) {
        String subject = "Wake up!";
        int hour = 5;
        int minute = 30;
        int snooze = 5;

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, subject);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        intent.putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, snooze);

        checkActivity(intent);

        Log.d(LOG_TAG, "Setting up alarm for " + subject + " at " + hour + ":" + minute);
    }

    public void onClickCallNumber(View view) {
        String number = "+2348146984900";

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));

        checkActivity(intent);

        Log.d(LOG_TAG, "Dialing " + number);
    }

    public void onClickComposeMessage(View view) {
        String recipient = "+2348146984900";
        String message = "Hello, how are you?";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + recipient));
        intent.putExtra("sms_body", message);

        checkActivity(intent);

        // Another way
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(recipient, null, message, null, null);

        Log.d(LOG_TAG, "Sending an sms to " + recipient);
    }

    private void checkActivity(Intent intent) {
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}