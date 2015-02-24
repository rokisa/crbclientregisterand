package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import ke.co.example.weaversoft.crbclientregister.api.ClientDetailsAPI;
import ke.co.example.weaversoft.crbclientregister.model.NextOfKin;
import ke.co.example.weaversoft.crbclientregister.util.ClientDetailsUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by weaversoft on 2/24/2015.
 */
public class AddNextOfKinScreen extends Activity {
    EditText emailAddressNextOfKin;
    private String valid_email;
    Long clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_of_kin_add);

        Intent originatorActivity = getIntent();
        clientId = originatorActivity.getExtras().getLong("ClientId");
        initilizeUI();
    }

    public void createNextOfKin(View view){

        EditText nationalIdET = (EditText)
                findViewById(R.id.etNOKNationalId);
        EditText firstNameET = (EditText)
                findViewById(R.id.etNOKFirstName);
        EditText lastNameET = (EditText)
                findViewById(R.id.etNOKLastName);
        emailAddressNextOfKin = (EditText)
                findViewById(R.id.etNOKEmailAddress);
        EditText phoneNumberET = (EditText)
                findViewById(R.id.etNOKPhoneNumber);
        EditText relationshipET = (EditText)
                findViewById(R.id.etNOKRelationship);

        boolean validationStatus = validateNOKInput(nationalIdET.getText().toString(),
                firstNameET.getText().toString(), lastNameET.getText().toString(),
                emailAddressNextOfKin.getText().toString(), phoneNumberET.getText().toString(),
                relationshipET.getText().toString());
        if(validationStatus){
            NextOfKin nextOfKin = new NextOfKin();
            nextOfKin.setCustomerId(clientId);
            nextOfKin.setEmailAddress(emailAddressNextOfKin.getText().toString());
            nextOfKin.setFirstName(firstNameET.getText().toString());
            nextOfKin.setLastName(lastNameET.getText().toString());
            nextOfKin.setRelationship(relationshipET.getText().toString());
            nextOfKin.setPhoneNumber(phoneNumberET.getText().toString());
            nextOfKin.setNationalId(nationalIdET.getText().toString());
            persistNextOfKin(nextOfKin);
        }
    }

    private boolean validateNOKInput(String nationalId, String firstName, String lastName,
                                     String emailAddress, String phoneNumber, String relationship) {
        boolean tempStatus = true;

        if(firstName.trim().length()==0){
            Toast.makeText(this, "First name cannot be empty, key in a valid First name",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if(lastName.trim().length()==0){
            Toast.makeText(this, "Last name cannot be empty, key in a valid Last name",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if (nationalId.trim().length()==0){
            Toast.makeText(this, "National Id cannot be empty, key in a valid National Id",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if (emailAddress.trim().length()==0){
            Toast.makeText(this, "Email address cannot be empty, key in a valid Email address",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if (relationship.trim().length()==0){
            Toast.makeText(this, "Relationship cannot be empty, key in a valid Relationship",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if (phoneNumber.trim().length()==0){
            Toast.makeText(this, "Phone number cannot be empty, key in a valid Phone number",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        }
        return tempStatus;
    }

    private void persistNextOfKin(NextOfKin nextOfKin){
        ClientDetailsUtil clientDetailsUtil = new ClientDetailsUtil();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(clientDetailsUtil.ENDPOINT)
                .build();
        Toast.makeText(AddNextOfKinScreen.this, "About to Persist Data", Toast.LENGTH_LONG).show();
        ClientDetailsAPI api = adapter.create(ClientDetailsAPI.class);
        api.createNextOfKin(nextOfKin,
                new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject jsonObject, Response response) {
                        try {
                            successNextOfKinCreation("CRTNSUCCESS");
                            Toast.makeText(AddNextOfKinScreen.this, "Data Persisted",
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        errorNextOfKinCreation("ERROR >> " + error.toString());
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        Intent cancelClientCreationOperation = new Intent();
        cancelClientCreationOperation.putExtra("status", "CANCELED");
        setResult(RESULT_OK, cancelClientCreationOperation);
        finish();
    }

    private void successNextOfKinCreation(String status){
        Intent intent = new Intent();
        intent.putExtra("status", status);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void errorNextOfKinCreation(String status){
        Intent intent = new Intent();
        intent.putExtra("status", status);
        setResult(RESULT_CANCELED , intent);
        finish();
    }

    public void cancelCreation(View view) {
        Intent cancelClientCreationOperation = new Intent();
        cancelClientCreationOperation.putExtra("status", "CANCELED");
        setResult(RESULT_OK, cancelClientCreationOperation);
        finish();
    }

    private void initilizeUI() {
        emailAddressNextOfKin = (EditText) findViewById(R.id.etNOKEmailAddress);

        emailAddressNextOfKin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                Is_Valid_Email(emailAddressNextOfKin); // pass your EditText Obj here.
            }

            public void Is_Valid_Email(EditText edt) {
                if (edt.getText().toString() == null) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else if (isEmailValid(edt.getText().toString()) == false) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else {
                    valid_email = edt.getText().toString();
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            } // end of TextWatcher (email)
        });
    }
}
