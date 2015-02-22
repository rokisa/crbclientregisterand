package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;

/**
 * Created by weaversoft on 2/21/2015.
 */
public class ViewClientScreen extends Activity {
    TextView tvClientName;
    TextView tvNationality;
    TextView emailAddress;
    ClientDetails clientDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_client);
        Intent originatorIntent = getIntent();

        clientDetails = (ClientDetails) originatorIntent.
                getSerializableExtra("clientInfo");

        initializeClientView();
    }

    private void initializeClientView() {
        tvClientName = (TextView) findViewById(R.id.tvClientName);
        tvNationality = (TextView) findViewById(R.id.tvNationality);
        emailAddress = (TextView) findViewById(R.id.tvEmailAddress);

        tvClientName.setText(clientDetails.getFirstName()+" "+clientDetails.getLastName());
        tvNationality.setText(clientDetails.getNationality());
        emailAddress.setText(clientDetails.getEmailAddress());
    }

    @Override
    public void onBackPressed() {
        Intent cancelClientCreationOperation = new Intent();
        cancelClientCreationOperation.putExtra("status", "VIEW CANCELED");
        setResult(RESULT_OK, cancelClientCreationOperation);
        finish();
    }

    void openClientEditScreen(){
        final int result=1;
        Intent clientIntent = new Intent(this, EditClientScreen.class);
        clientIntent.putExtra("clientInfo", clientDetails);

        startActivityForResult(clientIntent, result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent returningIntent = data;

        String status = returningIntent.getExtras().getString("status");

        if(status.equals("ERROR")){
            Toast.makeText(this, "Client Details Update Failed", Toast.LENGTH_LONG).show();
        } else if(!status.equals("SUCCESS")) {
            clientDetails = (ClientDetails) returningIntent.
                    getSerializableExtra("clientInfo");
            initializeClientView();
            Toast.makeText(this, "Client Updated Successfully", Toast.LENGTH_LONG).show();
        } else if(status.equals("CANCELED")){
            Toast.makeText(this, "Client Updated Canceled", Toast.LENGTH_LONG).show();
        }
    }
}
