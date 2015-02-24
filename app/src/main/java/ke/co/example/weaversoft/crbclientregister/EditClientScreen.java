package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import ke.co.example.weaversoft.crbclientregister.api.ClientDetailsAPI;
import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;
import ke.co.example.weaversoft.crbclientregister.util.ClientDetailsUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by weaversoft on 2/22/2015.
 */
public class EditClientScreen extends Activity {
    ClientDetails clientDetails;
    ClientDetailsUtil detailsUtil;
    EditText nationalIdET;
    EditText firstNameET;
    EditText lastNameET;
    EditText emailAddressET;
    EditText phoneNumberET;
    EditText nationalityET;
    EditText physicalAddressET;
    EditText occupationET;

    EditText dateOfBirthET;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        detailsUtil = new ClientDetailsUtil();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_client);
        Intent originatorIntent = getIntent();

        clientDetails = (ClientDetails) originatorIntent.
                getSerializableExtra("clientInfo");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        initializeClientView();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            dateOfBirthET.setText(new StringBuilder().append(day).append("/")
                    .append(month+1).append("/").append(year));

            Toast.makeText(EditClientScreen.this, dateOfBirthET.getText().toString(), Toast.LENGTH_LONG).show();

        }
    };

    private void initializeClientView() {

        nationalIdET = (EditText)
                findViewById(R.id.etEdNationalId);
        firstNameET = (EditText)
                findViewById(R.id.etEdFirstName);
        lastNameET = (EditText)
                findViewById(R.id.etEdLastName);
        emailAddressET = (EditText)
                findViewById(R.id.etEdEmailAddress);
        phoneNumberET = (EditText)
                findViewById(R.id.etEdPhoneNumber);
        nationalityET = (EditText)
                findViewById(R.id.etEdNationality);
        physicalAddressET = (EditText)
                findViewById(R.id.etEdPhysicalAddress);
        occupationET = (EditText)
                findViewById(R.id.etEdOccupation);
        dateOfBirthET =(EditText)
                findViewById(R.id.etEdDateOfBirth);

        nationalIdET.setText(clientDetails.getNationalId().toString());
        firstNameET.setText(clientDetails.getFirstName().toString());
        lastNameET.setText(clientDetails.getLastName().toString());
        emailAddressET.setText(clientDetails.getEmailAddress().toString());
        phoneNumberET.setText(clientDetails.getPhoneNumber().toString());
        nationalityET.setText(clientDetails.getNationality().toString());
        physicalAddressET.setText(clientDetails.getPhysicalAddress());
        occupationET.setText(clientDetails.getOccupation().toString());
        dateOfBirthET.setText(detailsUtil.getLongDate(clientDetails.getDateOfBirth()));

        dateOfBirthET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDialog(999);
                }
            }
        });
    }

    public void updateClient(View view){
        Date dateOfBirth = detailsUtil.getDate(dateOfBirthET.getText().toString());
        boolean validationStatus = validateClientInput(dateOfBirth, firstNameET.getText().toString(),
                lastNameET.getText().toString(), nationalityET.getText().toString(),
                nationalIdET.getText().toString(), physicalAddressET.getText().toString(),
                occupationET.getText().toString(), phoneNumberET.getText().toString());
        if(validationStatus){
            clientDetails.setDateOfBirth(dateOfBirth.getTime());
            clientDetails.setEmailAddress(emailAddressET.getText().toString());
            clientDetails.setFirstName(firstNameET.getText().toString());
            clientDetails.setLastName(lastNameET.getText().toString());
            clientDetails.setNationalId(nationalIdET.getText().toString());
            clientDetails.setNationality(nationalityET.getText().toString());
            clientDetails.setOccupation(occupationET.getText().toString());
            clientDetails.setPhysicalAddress(physicalAddressET.getText().toString());
            clientDetails.setPhoneNumber(phoneNumberET.getText().toString());
            persisClientDetails();
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

    private void persisClientDetails() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(detailsUtil.ENDPOINT)
                .build();

        ClientDetailsAPI api = adapter.create(ClientDetailsAPI.class);
        api.updateClientDetails(clientDetails,
                new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject jsonObject, Response response) {
                        try {
                            successClientCreation("SUCCESS");
                         /*   Toast.makeText(EditClientScreen.this, "Data Persisted",
                                    Toast.LENGTH_LONG).show();*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        errorClientCreation("ERROR");
                    }
                }
        );
    }

    private void successClientCreation(String status){
        Intent intent = new Intent();
        intent.putExtra("status", status);
        intent.putExtra("clientInfo",clientDetails);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void errorClientCreation(String status){
        Intent intent = new Intent();
        intent.putExtra("status", status);
        setResult(RESULT_CANCELED , intent);
        finish();
    }
    //dow mon dd hh:mm:ss zzz yyyy
    @Override
    public void onBackPressed() {
        Intent cancelClientUpdateOperation = new Intent();
        cancelClientUpdateOperation.putExtra("status", "CANCELED");
        cancelClientUpdateOperation.putExtra("clientInfo",clientDetails);
        setResult(RESULT_OK, cancelClientUpdateOperation);
        finish();
    }

    public void cancelCreation(View view) {
        Intent cancelClientUpdateOperation = new Intent();
        cancelClientUpdateOperation.putExtra("status", "CANCELED");
        cancelClientUpdateOperation.putExtra("clientInfo",clientDetails);
        setResult(RESULT_OK, cancelClientUpdateOperation);
        finish();
    }
}