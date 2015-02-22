package ke.co.example.weaversoft.crbclientregister;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ke.co.example.weaversoft.crbclientregister.api.ClientDetailsAPI;
import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    TextView clientName;
    TextView statusTv;
    ProgressBar pb;
    List<ClientDetails> clientDetailsList;
    public static final String ENDPOINT
            ="http://10.0.2.2:9000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        requestData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNewScreenClient(View view) {
        Intent getAddClientScreen = new Intent(this,
                AddClientScreen.class);
        final int result =1;

        getAddClientScreen.putExtra("ClientId", "NONE");
        startActivityForResult(getAddClientScreen, result);
    }

    private void requestData(){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();
        ClientDetailsAPI api = adapter.create(ClientDetailsAPI.class);

        api.fetchClientList(new Callback<List<ClientDetails>>() {
            @Override
            public void success(List<ClientDetails> clientDetails, Response response) {
                clientDetailsList = clientDetails;
                updateScreen();
            }

            @Override
            public void failure(RetrofitError error) {
                statusTv = (TextView) findViewById(R.id.statusTextView);

                if(error.toString().equals("retrofit.RetrofitError: " +
                        "com.google.gson.JsonSyntaxException: 0")){
                    Toast.makeText(MainActivity.this, "Database is Empty",
                            Toast.LENGTH_LONG).show();
                } else {
                    statusTv.setText(error.toString());
                    reportClientFetchError();
                }
            }
        });
    }

    private void reportClientFetchError() {
        Toast.makeText(this, "Error occurred while fetching data", Toast.LENGTH_LONG).show();
    }

    public void updateScreen(){

        ClientAdapter clientAdapter = new ClientAdapter(this, R.layout.item_client,
                clientDetailsList);

        ListView listView = getListView();
        listView.setOnItemClickListener(this);

        setListAdapter(clientAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent returningIntent = data;

        String status = returningIntent.getExtras().getString("status");

        statusTv = (TextView) findViewById(R.id.statusTextView);
        statusTv.setText(status);

        if(status.equals("ERROR")){
            Toast.makeText(this, "Client Creation Failed", Toast.LENGTH_LONG).show();
        } else if(!status.equals("CRTNSUCCESS")) {
            requestData();
            Toast.makeText(this, "Client Created Successfully", Toast.LENGTH_LONG).show();
        } else if(status.equals("CANCELED")){
            Toast.makeText(this, "Client Creation Canceled", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        ClientDetails clientDetails = (ClientDetails) adapter.getItemAtPosition(position);
        final int result=1;
        Intent clientIntent = new Intent(this, ViewClientScreen.class);
        clientIntent.putExtra("clientInfo", clientDetails);

        startActivityForResult(clientIntent, result);

    }
}