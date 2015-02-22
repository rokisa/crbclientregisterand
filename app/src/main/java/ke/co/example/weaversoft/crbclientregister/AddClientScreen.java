package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by weaversoft on 2/17/2015.
 */
public class AddClientScreen extends Activity {
    public static final String ENDPOINT
            ="http://10.0.2.2:9000/";
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    EditText dateOfBirthET;
    TextView tvLabel;
    EditText emailAddressET;
    private String valid_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_client);
        Intent originatorActivity = getIntent();
        String clientId = originatorActivity.getExtras().getString("ClientId");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        initilizeUI();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            setDateOfBirth(arg1, arg2, arg3);

        }
    };

    private void setDateOfBirth(int arg1, int arg2, int arg3){
        dateOfBirthET.setText(new StringBuilder().append(day).append("/")
          .append(month).append("/").append(year));

        Toast.makeText(AddClientScreen.this, new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year), Toast.LENGTH_LONG).show();
    }

    public void createClient(View view) {
        EditText nationalIdET = (EditText)
                findViewById(R.id.etNationalId);
        EditText firstNameET = (EditText)
                findViewById(R.id.etFirstName);
        EditText lastNameET = (EditText)
                findViewById(R.id.etLastName);
        emailAddressET = (EditText)
                findViewById(R.id.etEmailAddress);
        EditText phoneNumberET = (EditText)
                findViewById(R.id.etPhoneNumber);
        EditText nationalityET = (EditText)
                findViewById(R.id.etNationality);
        EditText physicalAddressET = (EditText)
                findViewById(R.id.etPhysicalAddress);

        EditText occupationET = (EditText)
                findViewById(R.id.etOccupation);
        Date dateOfBirth = getDate(dateOfBirthET.getText().toString());

        boolean validationStatus = validateClientInput(dateOfBirth, firstNameET.getText().toString(),
                lastNameET.getText().toString(), nationalityET.getText().toString(),
                nationalIdET.getText().toString(), physicalAddressET.getText().toString(),
                occupationET.getText().toString(), phoneNumberET.getText().toString());

        if(validationStatus){
            ClientDetails clientDetails = new ClientDetails();
            clientDetails.setDateOfBirth(dateOfBirth.getTime());
            clientDetails.setEmailAddress(emailAddressET.getText().toString());
            clientDetails.setFirstName(firstNameET.getText().toString());
            clientDetails.setLastName(lastNameET.getText().toString());
            clientDetails.setNationalId(nationalIdET.getText().toString());
            clientDetails.setNationality(nationalityET.getText().toString());
            clientDetails.setOccupation(occupationET.getText().toString());
            clientDetails.setPhysicalAddress(physicalAddressET.getText().toString());
            clientDetails.setPhoneNumber(phoneNumberET.getText().toString());

            persisClientDetails(clientDetails);
        }
    }

    private boolean validateClientInput(Date dateOfBirth, String firstName, String lastName,
                                        String nationality, String nationalId,
                                        String physicalAddress, String occupation,
                                        String phoneNumber) {
        boolean tempStatus = true;

        if(dateOfBirth==null){
            Toast.makeText(this, "Invalid Date Of Birth, please enter a valid Date",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        }
        if(firstName.trim().length()==0){
            Toast.makeText(this, "First name cannot be empty, key in a valid First name",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if(lastName.trim().length()==0){
            Toast.makeText(this, "Last name cannot be empty, key in a valid Last name",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if(nationality.trim().length()==0){
            Toast.makeText(this, "Nationality cannot be empty, key in a valid Nationality",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if (nationalId.trim().length()==0){
            Toast.makeText(this, "National Id cannot be empty, key in a valid National Id",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if (physicalAddress.trim().length()==0){
            Toast.makeText(this, "Physical address cannot be empty, key in a valid Physical address",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if (occupation.trim().length()==0){
            Toast.makeText(this, "Occupation cannot be empty, key in a valid Occupation",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        } else if (phoneNumber.trim().length()==0){
            Toast.makeText(this, "Phone number cannot be empty, key in a valid Phone number",
                    Toast.LENGTH_LONG).show();
            tempStatus = false;
        }
        return tempStatus;
    }

    private void persisClientDetails(ClientDetails clientDetails) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();
        Toast.makeText(AddClientScreen.this, "About to Persist Data", Toast.LENGTH_LONG).show();
        ClientDetailsAPI api = adapter.create(ClientDetailsAPI.class);
        api.createClientDetails(clientDetails,
                new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject jsonObject, Response response) {
                        try {
                            successClientCreation("CRTNSUCCESS");
                            Toast.makeText(AddClientScreen.this, "Data Persisted",
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        errorClientCreation("ERROR >> "+error.toString());
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

    private void successClientCreation(String status){
        Intent intent = new Intent();
        intent.putExtra("status", status);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void errorClientCreation(String status){
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

    /**
     * This method is used to initialize UI Components
     */
    private void initilizeUI() {

        dateOfBirthET  = (EditText)
                findViewById(R.id.etDateOfBirth);

        dateOfBirthET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDialog(999);
                }
            }
        });

        emailAddressET = (EditText) findViewById(R.id.etEmailAddress);

        emailAddressET.addTextChangedListener(new TextWatcher() {

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
                Is_Valid_Email(emailAddressET); // pass your EditText Obj here.
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

    private  Date getDate(String strDate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        if (strDate.trim().length()!=0){
            try {
                return date = formatter.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else{
            return null;
        }
    }
}
