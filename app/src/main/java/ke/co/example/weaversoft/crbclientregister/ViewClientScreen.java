package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;

/**
 * Created by weaversoft on 2/21/2015.
 */
public class ViewClientScreen extends Activity {
    TextView tvClientName;
    TextView tvNationality;
    TextView emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_client);
        Intent originatorIntent = getIntent();

        ClientDetails clientDetails = (ClientDetails) originatorIntent.
                getSerializableExtra("clientInfo");

        initializeClientView(clientDetails);
    }

    private void initializeClientView(ClientDetails clientDetails) {
        tvClientName = (TextView) findViewById(R.id.tvClientName);
        tvNationality = (TextView) findViewById(R.id.tvNationality);
        emailAddress = (TextView) findViewById(R.id.tvEmailAddress);

        tvClientName.setText(clientDetails.getFirstName()+" "+clientDetails.getLastName());
        tvNationality.setText(clientDetails.getNationality());
        emailAddress.setText(clientDetails.getEmailAddress());
    }
}
