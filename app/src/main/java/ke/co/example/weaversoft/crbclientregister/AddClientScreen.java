package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_client);

        Intent originatorActivity = getIntent();

        String clientId = originatorActivity.getExtras().getString("ClientId");

    }

    public void createClient(View view) {

        String status ="";

        EditText nationalIdET = (EditText)
                findViewById(R.id.etNationalId);
        EditText firstNameET = (EditText)
                findViewById(R.id.etFirstName);
        EditText lastNameET = (EditText)
                findViewById(R.id.etLastName);
        EditText emailAddressET = (EditText)
                findViewById(R.id.etEmailAddress);
        EditText phoneNumberET = (EditText)
                findViewById(R.id.etPhoneNumber);
        EditText nationalityET = (EditText)
                findViewById(R.id.etNationality);
        EditText physicalAddressET = (EditText)
                findViewById(R.id.etPhysicalAddress);
        EditText dateOfBirthET = (EditText)
                findViewById(R.id.etDateOfBirth);
        EditText occupationET = (EditText)
                findViewById(R.id.etOccupation);

        ClientDetails clientDetails = new ClientDetails();
        clientDetails.setDateOfBirth(dateOfBirthET.getText().toString());
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

    private void persisClientDetails(ClientDetails clientDetails) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();
        ClientDetailsAPI api = adapter.create(ClientDetailsAPI.class);

        api.createClientDetails(clientDetails,
                new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject jsonObject, Response response) {
                        try {
                            successClientCreation(jsonObject.toString());
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

}
