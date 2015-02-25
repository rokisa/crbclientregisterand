package ke.co.example.weaversoft.crbclientregister;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
import ke.co.example.weaversoft.crbclientregister.model.NextOfKin;
import ke.co.example.weaversoft.crbclientregister.util.ClientDetailsUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by weaversoft on 2/24/2015.
 */
public class ViewNextOfKin extends ListActivity implements AdapterView.OnItemClickListener {
    List<NextOfKin> nextOfKinList;
    ClientDetails clientDetails;
    TextView nextOfKinTV;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_of_kin);

        Intent originatorIntent = getIntent();

        clientDetails = (ClientDetails) originatorIntent.
                getSerializableExtra("clientInfo");

        pb = (ProgressBar) findViewById(R.id.progressBarNOK);
        pb.setVisibility(View.INVISIBLE);

        requestData();
    }

    private void requestData(){
        nextOfKinTV = (TextView) findViewById(R.id.nextOfKinLabel);
        nextOfKinTV.setText(clientDetails.getFirstName() + " " + clientDetails.getLastName() +"'s Next of Kin");

        ClientDetailsUtil clientDetailsUtil = new ClientDetailsUtil();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(clientDetailsUtil.ENDPOINT)
                .build();
        ClientDetailsAPI api = adapter.create(ClientDetailsAPI.class);
        pb.setVisibility(View.VISIBLE);
        api.fetchNextOfKin(clientDetails, new Callback<List<NextOfKin>>() {
            @Override
            public void success(List<NextOfKin> nextOfKinListNw, Response response) {
                nextOfKinList = nextOfKinListNw;
                updateScreen();
            }

            @Override
            public void failure(RetrofitError error) {

                if(error.toString().equals("retrofit.RetrofitError: " +
                        "com.google.gson.JsonSyntaxException: 0")){
                    Toast.makeText(ViewNextOfKin.this, "Database is Empty",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ViewNextOfKin.this, error.toString(),
                            Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void updateScreen(){

        NextOfKinAdapter nextOfKinAdapter = new NextOfKinAdapter(this, R.layout.next_of_kin_item,
                nextOfKinList);

        ListView listView = getListView();
        listView.setOnItemClickListener(this);

        setListAdapter(nextOfKinAdapter);
        pb.setVisibility(View.INVISIBLE);
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

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        NextOfKin nextOfKin = (NextOfKin) adapter.getItemAtPosition(position);
        nextOfKin.setCustomerId(clientDetails.getClientId());
        final int result=1;
        Intent nextOfKinIntent = new Intent(this, EditNextOfKinScreen.class);

        nextOfKinIntent.putExtra("nextOfKinInfo", nextOfKin);
        startActivityForResult(nextOfKinIntent, result);

    }

    public void createNextOfKin(View view){
        int result = 1;
        Intent createNextOfKinIntent = new Intent(this, AddNextOfKinScreen.class);
        createNextOfKinIntent.putExtra("ClientId", clientDetails.getClientId());
        startActivityForResult(createNextOfKinIntent, result);
    }

    @Override
    public void onBackPressed() {
        Intent cancelClientCreationOperation = new Intent();
        cancelClientCreationOperation.putExtra("status", "CANCELED");
        setResult(RESULT_OK, cancelClientCreationOperation);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent returningIntent = data;

        String status = returningIntent.getExtras().getString("status");

        if(status.equals("ERROR")){
            Toast.makeText(this, "Next Of Kin Creation Failed", Toast.LENGTH_LONG).show();
        } else if(status.equals("CRTNSUCCESS")) {
            requestData();
            Toast.makeText(this, "Next Of Kin Created Successfully", Toast.LENGTH_LONG).show();
        } else if(status.equals("CANCELED")){
            Toast.makeText(this, "Next Of Kin Creation Canceled", Toast.LENGTH_LONG).show();
        }
    }
}
