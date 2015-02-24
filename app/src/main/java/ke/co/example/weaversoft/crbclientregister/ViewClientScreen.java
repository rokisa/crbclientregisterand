package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;

import ke.co.example.weaversoft.crbclientregister.api.ClientDetailsAPI;
import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;
import ke.co.example.weaversoft.crbclientregister.util.ClientDetailsUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by weaversoft on 2/21/2015.
 */
public class ViewClientScreen extends Activity {
    TextView tvClientName;
    TextView tvNationality;
    TextView emailAddress;
    TextView tvDateOB;
    TextView tvCVPhoneNumber;
    ClientDetails clientDetails;
    ClientDetailsUtil detailsUtil = new ClientDetailsUtil();
    ImageView imgProfile;
    ImageView imgIdPhoto;
    File photo;
    private static final int SELECTED_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_client);
        Intent originatorIntent = getIntent();

        clientDetails = (ClientDetails) originatorIntent.
                getSerializableExtra("clientInfo");

        imgProfile = (ImageView) findViewById(R.id.imgViewProfile);

        initializeClientView();
    }

    public void changeClientImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PICTURE);

    }

    private void initializeClientView() {
       //imgProfile.setImageResource(R.drawable.placeholder);
        tvClientName = (TextView) findViewById(R.id.vwClientName);
        tvNationality = (TextView) findViewById(R.id.tvNationality);
        emailAddress = (TextView) findViewById(R.id.tvEmailAddress);
        tvDateOB = (TextView) findViewById(R.id.tvDateOB);
        tvCVPhoneNumber = (TextView) findViewById(R.id.tvCVPhoneNumber);

        tvClientName.setText(clientDetails.getFirstName()+" "+clientDetails.getLastName());
        tvNationality.setText(clientDetails.getNationality());
        emailAddress.setText(clientDetails.getEmailAddress());
        tvCVPhoneNumber.setText(clientDetails.getPhoneNumber());
        tvDateOB.setText(detailsUtil.getLongDate(clientDetails.getDateOfBirth()));
    }

    @Override
    public void onBackPressed() {
        Intent cancelClientCreationOperation = new Intent();
        cancelClientCreationOperation.putExtra("status", "VIEW CANCELED");
        setResult(RESULT_OK, cancelClientCreationOperation);
        finish();
    }

    public void openClientEditScreen(View view){
        final int result=1;
        Intent clientIntent = new Intent(this, EditClientScreen.class);
        clientIntent.putExtra("clientInfo", clientDetails);

        startActivityForResult(clientIntent, result);
    }

    public void viewNextOfKin(View view){
        final int result=1;
        Intent nextOfKinViewIntent = new Intent(this, ViewNextOfKin.class);
        nextOfKinViewIntent.putExtra("clientInfo", clientDetails);

        startActivityForResult(nextOfKinViewIntent, result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SELECTED_PICTURE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri,
                        projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                Drawable drawable = new BitmapDrawable(selectedImage);
                imgProfile.setBackground(drawable);

                photo = new File(filePath);
                //String ext = FilenameUtils.getExtension(
                TypedFile typedFile = new TypedFile(detailsUtil.getMimeType(photo), photo);
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(detailsUtil.ENDPOINT)
                        .build();

                Toast.makeText(ViewClientScreen.this, "Almost posting the profile pic",
                        Toast.LENGTH_LONG).show();

                TypedString typedString = new TypedString(clientDetails.getClientId().toString());

                ClientDetailsAPI api = adapter.create(ClientDetailsAPI.class);
                api.uploadClientIdPhoto(typedFile, typedString,
                        new Callback<JSONObject>() {
                            @Override
                            public void success(JSONObject jsonObject, Response response) {
                                try {
                                    //successClientCreation("SUCCESS");
                            Toast.makeText(ViewClientScreen.this, "Data Persisted",
                                    Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                               // errorClientCreation("ERROR");
                            }
                        }
                );

/*                Toast.makeText(ViewClientScreen.this, filePath.toString(),
                        Toast.LENGTH_LONG).show();*/
            }
        } else {
            Intent returningIntent = data;
            String status = returningIntent.getExtras().getString("status");

            if (status.equals("ERROR")) {
                Toast.makeText(this, "Client Details Update Failed", Toast.LENGTH_LONG).show();
            } else if (status.equals("SUCCESS")) {
                clientDetails = (ClientDetails) returningIntent.
                        getSerializableExtra("clientInfo");
                initializeClientView();
                Toast.makeText(this, "Client Updated Successfully", Toast.LENGTH_LONG).show();
            } else if (status.equals("CANCELED")) {
                Toast.makeText(this, "Client Updated Canceled", Toast.LENGTH_LONG).show();
            }
        }
    }

}