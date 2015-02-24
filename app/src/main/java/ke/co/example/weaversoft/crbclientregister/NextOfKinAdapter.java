package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ke.co.example.weaversoft.crbclientregister.model.NextOfKin;
import ke.co.example.weaversoft.crbclientregister.util.ClientDetailsUtil;

/**
 * Created by weaversoft on 2/24/2015.
 */
public class NextOfKinAdapter extends ArrayAdapter<NextOfKin> {
    private Context context;
    private List<NextOfKin> nextOfKinList;

    public NextOfKinAdapter(Context context, int resource, List<NextOfKin> nextOfKinList){
        super(context, resource, nextOfKinList);
        this.context = context;
        this.nextOfKinList = nextOfKinList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.next_of_kin_item, parent, false);

        NextOfKin nextOfKin = nextOfKinList.get(position);
        TextView nextOfKinName = (TextView) view.findViewById(R.id.tv_next_of_kin_name);
        TextView relationship = (TextView) view.findViewById(R.id.tv_relationship);

        nextOfKinName.setText(nextOfKin.getFirstName() + " " + nextOfKin.getLastName());
        relationship.setText(nextOfKin.getRelationship());

        return view;
    }
}
