package com.citam.schools;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

/**
 * Created by POlonde on 10/18/2017.
 */

public class Fragment_Contact extends Fragment {

    View v;
    EditText txtFullName, txtEmail, txtSubject, txtMessage;

    Button btnSubmit;
    boolean fieldsValid = true;

    String apiHost = CITAMSchools.getInstance().getApiHost();
    FloatingActionButton fab_call;
    CustomProgressDialog progressDialog;
    TextView tvAgree;
    CheckBox chAgree;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_contact, container, false);
        progressDialog = new CustomProgressDialog(getContext(), R.drawable.ic_star_black_24dp);
        txtEmail = v.findViewById(R.id.txtEmailAddress);
        txtFullName = v.findViewById(R.id.txtFullNames);
        txtSubject = v.findViewById(R.id.txtSubject);
        txtMessage = v.findViewById(R.id.txtMessage);
        txtMessage.setHorizontallyScrolling(false);
        fab_call = v.findViewById(R.id.fab_call);

        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+254711022803";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });


        btnSubmit = v.findViewById(R.id.btnSubmitEnquiry);
        chAgree = v.findViewById(R.id.chAgree);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
                if (fieldsValid) {
                    postData();
                }
            }
        });

        tvAgree = v.findViewById(R.id.tvAgree);

        tvAgree.append(Html.fromHtml("<a href=https://www.kenya-airways.com/privacy-policy/en/> privacy policy</a>"));
        tvAgree.setClickable(true);
        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());

        return v;
    }


    private void validateFields() {
        if (txtFullName.getText().toString().trim().length() == 0) {
            txtFullName.setError("Enter your full names");
            txtFullName.requestFocus();
            fieldsValid = false;
        } else if (!isEmailValid(txtEmail.getText().toString().trim())) {
            txtEmail.setError("Enter a valid email address");
            txtEmail.requestFocus();
            fieldsValid = false;
        } else if (txtSubject.getText().toString().trim().length() == 0) {
            txtSubject.setError("Enter the subject");
            txtSubject.requestFocus();
            fieldsValid = false;
        }
        else if (!chAgree.isChecked()) {
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
                        Log.d("CONTACT_RESPONSE", response);
                        if (response.equalsIgnoreCase("true")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Thank you");
                            builder.setMessage("Thank you for your Message." + "\n" +
                                    " We will get back to you as soon as possible." + "\n" +
                                    " Regards." + "\n" + "\n" +
                                    " KQ Pride Team");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    clearFields();


                                }
                            });
                            builder.show();

                        } else {
                            Toast.makeText(getContext(), "An error occurred somewhere, please try again after some time", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CONTACT_ERROR", error.getMessage(), error);
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getContext(), "Connection timed out.Please try again", Toast.LENGTH_LONG).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getContext(), "Authorization failed.Please try again", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getContext(), "Server error.Please contact customer service desk", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                        } else if (error instanceof ParseError) {
                            Toast.makeText(getContext(), "An error occurred on our side.Please contact customer service desk", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "An error occurred.Please contact customer service desk", Toast.LENGTH_LONG).show();

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

                params.put("Heading", txtSubject.getText().toString());
                params.put("Message", txtMessage.getText().toString());
                params.put("FormType", "con");

                return params;
            }
        };
        CITAMSchools.getInstance().addToRequestQueue(postRequest, "JSON REQUEST");

    }

    private void clearFields() {
        txtMessage.setText(null);
        txtEmail.setText(null);
        txtFullName.setText(null);
        txtSubject.setText(null);

    }


}
