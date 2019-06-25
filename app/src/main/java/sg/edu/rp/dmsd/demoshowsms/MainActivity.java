package sg.edu.rp.dmsd.demoshowsms;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity {

    Button btnbtnRetrieve;
    TextView tvSms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSms = findViewById(R.id.tv);
        btnbtnRetrieve = findViewById(R.id.btnRetrieve);

        btnbtnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int permissionCheck = PermissionChecker.checkSelfPermission
                        (MainActivity.this, Manifest.permission.READ_SMS);

                if(permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS},0);
                    //stops the action from proceeding further as permission not
                    //granted yet

                    return;
                }

                Uri uri = Uri.parse("content://sms");

                //the colum we want
                // date is when the message took place
                // address is the number of the other party
                // body is the message content
                // type 1 is received, type 2 sent
                String[] reqCols = new String[]{"date","address","body","type"};

                //Get Content Resolver object from which to
                //query the content provider
                ContentResolver cr = getContentResolver();
                //Fetch SMS Message from Built-in Content Provider
                Cursor cursor = cr.query(uri,reqCols,null,null,null);
                String smsBody = "";
                if(cursor.moveToFirst()){
                    do{
                        long dateInMillis = cursor.getLong(0);
                        String date = (String)DateFormat.format ("dd MMM yyyy h:mm:ss aa",dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);

                        if(type.equalsIgnoreCase("1")){
                            type = "Inbox";
                        }else{
                            type = "Sent";
                        }

                        smsBody += type + " " + address + "\n at" + date + "\n\"" + body +"\"\n\n";
                    }while (cursor.moveToNext());
                }
                tvSms.setText(smsBody);
            }
        });
    }
}
