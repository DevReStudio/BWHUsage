package top.drstudio.bwhusage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.kongzue.dialog.v3.MessageDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ProgressBar progressBar = findViewById(R.id.circularProgressbar);
        final TextView textView = findViewById(R.id.textPercentage);
        textView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                /*(
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                 */
            }
        });
        this.setKey();
        setCurrentTime();
        update();
        test();
        try{
            Thread.sleep(2000);
        } catch (Exception e) {
            //
        }
        setClipboard();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_exit:
                finish();
                return true;
            case R.id.action_settings:
                showNormalDialog();
                Toast.makeText(this,"TODO", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_CopySubscriptionLink:
                //
                Toast.makeText(this,"Subscription Link Copied", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.feedback:
                feedback();
        }

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_exit) {
            return true;
        }

         */

        return super.onOptionsItemSelected(item);
    }
    /*
    public void updateInfo(View source) {
        //Log
        System.out.println("The Button has benn click!");
        update();
    }

     */

    public void update() {

        final TextView textView;
        textView = findViewById(R.id.textUsedDataContent);

        HttpRequest.build(MainActivity.this, Key.info_url).setJsonResponseListener(new JsonResponseListener() {
            @Override
            public void onResponse(JsonMap main, Exception error) {
                if (error == null) {

                    final String usedData = main.getString("data_counter");
                    System.out.println(main.toString() + "++++++++++++++++++++++++++++++++++");
                    Double textUesdData = Double.parseDouble(usedData) / 1024 / 1024 / 1024;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String textUesdDatatext = df.format(textUesdData) + "  GiB";
                    textView.setText(textUesdDatatext);

                    ProgressBar progressBar = findViewById(R.id.circularProgressbar);
                    TextView textPercentage = findViewById(R.id.textPercentage);

                    long long_usedData = Long.parseLong(usedData);
                    double data_Total = (long)500 * 1024 * 1024 * 1024;
                    //System.out.println(long_usedData);

                    double divided = long_usedData / data_Total;
                    int percent;

                    percent = (int)(divided * 100.0);
                    percent = 100 - percent;
                    //System.out.println(percent + "-----------"  + divided);


                    progressBar.setProgress(percent,true);
                    String  text = percent + "%";
                    textPercentage.setText(text);

                    nextResetDate(main.getString("data_next_reset"));

                    progressBar.setVisibility(View.VISIBLE);
                    textPercentage.setVisibility(View.VISIBLE);

                }
            }
        }).doGet();
    }

    public void setCurrentTime(){
        TextView textView = findViewById(R.id.textTimeCurrentContent);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = new Date();
        String currentTime = sdf.format(date);
        textView.setText(currentTime);
    }

    public void feedback() {
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:ljd69154@liangjundi.cn"));
        data.putExtra(Intent.EXTRA_SUBJECT, "Report problem App name BWH Usage");
        data.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(data);

    }

    public void nextResetDate(String strDateTimestamp){
        long dateTimestamp = Long.parseLong(strDateTimestamp);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
        String nextResetTime = sdf.format(new Date(dateTimestamp * 1000));
        System.out.println(nextResetTime);

        TextView textView = findViewById(R.id.textDateResetContent);
        textView.setText(nextResetTime);

    }

    public void setClipboard(){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", Key.subscription_link);
        clipboardManager.setPrimaryClip(mClipData);
    }

    private void showNormalDialog(){
        String title = "What's New";
        String message = "0x00. the icon to show how much data left\n\n" +
                         "0x01. Updated when the data will reset\n\n" +
                         "0x02. Updated a status light, which will show the status between you and server\n\n" +
                         "0x03. There's Subscription Link in the menu" +
                         "\n\n\n" +
                         "TODO: \n"  +
                         "Localization\n" +
                         "Pull To Refresh"
                        ;
        String okButton = "Close";

        MessageDialog.show(MainActivity.this, title, message, okButton);

    }

    private void test(){
        HttpRequest.build(MainActivity.this, Key.ping_url)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error == null){
                            //Success
                            boolean isBanned = main.getBoolean("banned");
                            RadioButton radioButton = findViewById(R.id.radioButton);
                            if (isBanned) {
                                ColorStateList colorStateList = new ColorStateList(
                                        new int[][]{
                                                new int[]{android.R.attr.state_checked},
                                        },
                                        new int[]{
                                                Color.RED,
                                        }
                                );
                                radioButton.setText("404 BS");
                                radioButton.setButtonTintList(colorStateList);
                            } else {
                                //Fail
                                ColorStateList colorStateList = new ColorStateList(
                                        new int[][]{
                                                new int[]{android.R.attr.state_checked},
                                        },
                                        new int[]{
                                                Color.parseColor("#00d91a"),
                                        }
                                );
                                radioButton.setText("200 OK");
                                radioButton.setButtonTintList(colorStateList);
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Request Occur Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        .doGet();
    }

    public void docOnClick(View view){
        String title = "Color Guide";
        String message = "\n" +
                "GREEN means working in China\n\n" +
                "RED means banned in China" +
                "\n\n\n"

                ;
        String okButton = "Close";

        MessageDialog.show(MainActivity.this, title, message, okButton);
    }

    public String readKey(){
        String str = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = this.getResources().openRawResource(R.raw.key);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


        try {
           str = reader.readLine();
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(str);
        return str;
    }

    public JSONObject covertToJSON(){

        String jsonString = readKey();

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void setKey(){
        JSONObject jsonObject = covertToJSON();

        try {
            Key.info_url = jsonObject.getString("info_url");
            Key.ping_url = jsonObject.getString("ping_url");
            Key.subscription_link = jsonObject.getString("subscription_link");
            Key.eamil = jsonObject.getString("email");
        } catch (Exception e){
            //
        }
    }
}
