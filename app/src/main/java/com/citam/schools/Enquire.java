package com.citam.schools;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Enquire extends AppCompatActivity {
    EditText txtCourse, txtEmail, txtFullName, txtMessage;
    String selectedCourse;
    Button btnSubmit;
    boolean fieldsValid = true;

    String apiHost = CITAMSchools.getInstance().getApiHost();
    CustomProgressDialog progressDialog;
    TextView tvAgree;
    CheckBox chAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquire);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Course Enquiry");
        }
        progressDialog = new CustomProgressDialog(this, R.drawable.ic_star_black_24dp);
        selectedCourse = getIntent().getExtras().getString("selectedCourse");

        txtCourse = findViewById(R.id.txtCourse);
        txtEmail = findViewById(R.id.txtEmail);
        txtFullName = findViewById(R.id.txtFullNames);
        txtMessage = findViewById(R.id.txtMessage);
        txtMessage.setHorizontallyScrolling(false);
        btnSubmit = findViewById(R.id.btnSubmitEnquiry);
        chAgree = findViewById(R.id.chAgree);

        txtCourse.setText(selectedCourse);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
                if (fieldsValid) {
                    postData();
                }
            }
        });

        tvAgree = findViewById(R.id.tvAgree);

        tvAgree.append(Html.fromHtml("<a href=https://www.kenya-airways.com/privacy-policy/en/> privacy policy</a>"));
        tvAgree.setClickable(true);
        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void validateFields() {
        if (txtFullName.getText().toString().trim().length() == 0) {
            txtFullName.setError("Enter your full names");
            txtFullName.requestFocus();
            fieldsValid = false;
        } else if (!isEmailValid(txtEmail.getText().toString())) {
            txtEmail.setError("Enter a valid email address");
            txtEmail.requestFocus();
            fieldsValid = false;
        } else if (txtCourse.getText().toString().trim().length() == 0) {
            txtCourse.setError("Course Cannot be blank");
            txtCourse.requestFocus();
            fieldsValid = false;
        } else if (txtMessage.getText().toString().trim().length() == 0) {
            txtMessage.setError("Message Cannot be blank");
            txtMessage.requestFocus();
            fieldsValid = false;
        } else if (!chAgree.isChecked()) {
            chAgree.setError("You must agree to privacy policy");
            chAgree.requestFocus();
            fieldsValid = false;
        }
        else {
            fieldsValid = true;
        }
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private void postData() {


        progressDialog.show();
        progressDialog.setCancelable(false);


        String url = apiHost + "comm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("ENQUIRY_RESPONSE", response);
                        if (response.equalsIgnoreCase("true")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Enquire.this);
                            builder.setTitle("Thank you for getting in touch with us");
                            builder.setMessage("We will respond to you within 48 hours except during the weekends" +
                                    " i.e Saturdays and Sundays and on all public holidays.If not,Please allow up to" +
                                    " three to five working days for a delayed response." + "\n" +
                                    " Your patience is very much appreciated whilst we handle your case to a satisfactory" +
                                    " conclusion." + "\n" +
                                    " Yours sincerely," + "\n" + "\n" +
                                    " KQ Pride Centre");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Enquire.this.finish();


                                }
                            });
                            builder.show();

                        } else {
                            Toast.makeText(Enquire.this, "An error occured somewhere, please try again after some time", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(Enquire.this, "Please check your internet connection", Toast.LENGTH_LONG).show();

                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(Enquire.this, "Connection timed out.Please try again", Toast.LENGTH_LONG).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Enquire.this, "Authorization failed.Please try again", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(Enquire.this, "Server error.Please contact customer service desk", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Enquire.this, "Please check your internet connection", Toast.LENGTH_LONG).show();

                        } else if (error instanceof ParseError) {
                            Toast.makeText(Enquire.this, "An error occurred on our side.Please contact customer service desk", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Enquire.this, "An error occurred.Please contact customer service desk", Toast.LENGTH_LONG).show();

                        }
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("FullName", txtFullName.getText().toString());
                params.put("EmailAddress", txtEmail.getText().toString());
                params.put("EnquiryType", "Course");
                params.put("Heading", txtCourse.getText().toString());
                params.put("Message", txtMessage.getText().toString());
                params.put("FormType", "enq");

                return params;
            }
        };
        CITAMSchools.getInstance().addToRequestQueue(postRequest, "JSON REQUEST");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}