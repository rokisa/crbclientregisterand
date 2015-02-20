package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;

/**
 * Created by weaversoft on 2/18/2015.
 */
public class ClientAdapter extends ArrayAdapter<ClientDetails> {
    private Context context;
    private List<ClientDetails> clientDetailsList;

    public ClientAdapter(Context context, int resource, List<ClientDetails> clientDetailsList){
        super(context, resource, clientDetailsList);
        this.context = context;
        this.clientDetailsList = clientDetailsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_client, parent, false);

        ClientDetails clientDetails = clientDetailsList.get(position);
        TextView textView = (TextView) view.findViewById(R.id.textView1);
        textView.setText(clientDetails.getFirstName() + " " + clientDetails.getLastName());
        TextView tvDateOfBirth = (TextView) view.findViewById(R.id.tvDateOfbirth);
        tvDateOfBirth.setText(new Date(clientDetails.getDateOfBirth()).toString());

        return view;

    }
}
