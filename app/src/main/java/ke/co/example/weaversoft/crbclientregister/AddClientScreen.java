package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ke.co.example.weaversoft.crbclients.R;
import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;

/**
 * Created by weaversoft on 2/17/2015.
 */
public class AddClientScreen extends Activity {

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
        clientDetails.setDateOfBirth(getDate(dateOfBirthET.getText().toString()));
        clientDetails.setEmailAddress(emailAddressET.getText().toString());
        clientDetails.setFirstName(firstNameET.getText().toString());
        clientDetails.setLastName(lastNameET.getText().toString());
        clientDetails.setNationalId(nationalIdET.getText().toString());
        clientDetails.setNationality(nationalityET.getText().toString());
        clientDetails.setOccupation(occupationET.getText().toString());
        clientDetails.setPhysicalAddress(physicalAddressET.getText().toString());
        clientDetails.setPhoneNumber(phoneNumberET.getText().toString());

        status = persisClientDetails(clientDetails);

    }

    private String persisClientDetails(ClientDetails clientDetails) {

        return null;
    }

    public void cancelCreation(View view) {

        Intent cancelClientCreationOperation = new Intent();

        cancelClientCreationOperation.putExtra("status", "CANCELED");

        setResult(RESULT_OK, cancelClientCreationOperation);

        finish();

    }

    private static Date getDate(String strDate){

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
