package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;

/**
 * Created by weaversoft on 2/22/2015.
 */
public class EditClientScreen extends Activity {
    ClientDetails clientDetails;

    EditText nationalIdET;
    EditText firstNameET;
    EditText lastNameET;
    EditText emailAddressET;
    EditText phoneNumberET;
    EditText nationalityET;
    EditText physicalAddressET;
    EditText occupationET;
    EditText dateOfBirthET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_client);
        Intent originatorIntent = getIntent();

        clientDetails = (ClientDetails) originatorIntent.
                getSerializableExtra("clientInfo");

        initializeClientView();
    }

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
        dateOfBirthET.setText(getLongDate(clientDetails.getDateOfBirth()));
    }

    private  Date getDate(String strDate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");
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

    private String getLongDate(long dateInMillisecs){
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");
        String strDate = "";
        try {
            strDate = format.format(new Date(dateInMillisecs));
        } catch (Exception e){
        }
        return strDate;
    }
    //dow mon dd hh:mm:ss zzz yyyy
}
