package com.citam.schools;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AboutApp extends Activity {

    TextView tvCopyright, tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        tvVersion = findViewById(R.id.tvVersion);
        try {
            tvVersion.setText("Version "+getApplication().getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        }catch (Exception ignored){

        }
        tvCopyright = findViewById(R.id.tvCopyright);
        setDate(tvCopyright);
    }

    private void setDate(final TextView tv) {

        String url = "http://api.timezonedb.com/v2/get-time-zone?key=B4S7DIXFS1HM&by=zone&zone=Africa/Nairobi&format=json";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String data;
                        Log.d("TIME_RESPONSE", response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            data = obj.getString("formatted");

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                            String year = yearFormat.format(sdf.parse(data));
                            tv.setText("Copyright "+year+ " KQ Pride Centre");

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TIME_ERROR", error.getMessage(), error);

                    }
                }
        );
        CITAMSchools.getInstance().addToRequestQueue(postRequest, "TIME_REQUEST");
    }
}
