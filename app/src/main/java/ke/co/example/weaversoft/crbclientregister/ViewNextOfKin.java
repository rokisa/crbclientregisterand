package ke.co.example.weaversoft.crbclientregister;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import ke.co.example.weaversoft.crbclientregister.model.NextOfKin;

/**
 * Created by weaversoft on 2/24/2015.
 */
public class ViewNextOfKin extends ListActivity implements AdapterView.OnItemClickListener {
    List<NextOfKin> nextOfKinList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_of_kin);

        Intent originatorIntent = getIntent();

        nextOfKinList = (List<NextOfKin>) originatorIntent.
                getSerializableExtra("nextOfKinListInfo");
        updateScreen();
    }

    public void updateScreen(){

        NextOfKinAdapter nextOfKinAdapter = new NextOfKinAdapter(this, R.layout.next_of_kin_item,
                nextOfKinList);

        ListView listView = getListView();
        listView.setOnItemClickListener(this);

        setListAdapter(nextOfKinAdapter);
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
        final int result=1;
        Intent nextOfKinIntent = new Intent(this, EditNextOfKinScreen.class);
        nextOfKinIntent.putExtra("nextOfKinInfo", nextOfKin);

        startActivityForResult(nextOfKinIntent, result);

    }
}
