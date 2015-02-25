package ke.co.example.weaversoft.crbclientregister;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import ke.co.example.weaversoft.crbclientregister.api.ClientDetailsAPI;
import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;
import ke.co.example.weaversoft.crbclientregister.util.ClientDetailsUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by weaversoft on 2/21/2015.
 */
public class ViewClientScreen extends Activity {
    public final String APP_TAG = "MyCustomApp";
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
    private static final int SELECTED_PROFILE_PICTURE = 99;
    private static final int SELECTED_ID_PICTURE = 98;
    public String photoFileName = "profphoto.jpg";
    public String photoFileNameId = "idphoto.jpg";

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

    public void captureClientImage(View view) {
/*        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        capturePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
        startActivityForResult(capturePhotoIntent, SELECTED_PROFILE_PICTURE);

    }

    public void captureIdImage(View view){
        Intent idCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        idCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileNameId));
        startActivityForResult(idCaptureIntent, SELECTED_ID_PICTURE);
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
        if(clientDetails.getPhoto()!=null){
            if(clientDetails.getPhoto().trim().length()!=0){
                Picasso.with(getApplicationContext()).load("http://localhost:9000/assets/images/clientphotos/"+
                        clientDetails.getPhoto()).into(imgProfile);
            }
        }
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

        if(requestCode==SELECTED_PROFILE_PICTURE) {

            String filePath = "";

            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                try {
                    takenImage = rotateBitmapOrientation(takenPhotoUri.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filePath = takenPhotoUri.getPath();
                // Load the taken image into a preview
                imgProfile.setImageBitmap(takenImage);

                photo = new File(filePath);
                TypedFile typedFile = new TypedFile(detailsUtil.getMimeType(photo), photo);
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(detailsUtil.ENDPOINT)
                        .build();

                Toast.makeText(ViewClientScreen.this, "Uploading client's picture",
                        Toast.LENGTH_LONG).show();

                ClientDetailsAPI api = adapter.create(ClientDetailsAPI.class);
                api.uploadClientProfilePhoto(typedFile, clientDetails.getClientId().toString(),
                        new Callback<JSONObject>() {
                            @Override
                            public void success(JSONObject jsonObject, Response response) {
                                try {
                                    Toast.makeText(ViewClientScreen.this, "Client's picture " +
                                                    "uploaded successfully",
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(ViewClientScreen.this, error.toString(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                );

            } else { // Result was a failure
                Toast.makeText(ViewClientScreen.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == SELECTED_ID_PICTURE) {

            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                try {
                    takenImage = rotateBitmapOrientation(takenPhotoUri.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String filePath = takenPhotoUri.getPath();
                // Load the taken image into a preview
                imgProfile.setImageBitmap(takenImage);

                photo = new File(filePath);
                TypedFile typedFile = new TypedFile(detailsUtil.getMimeType(photo), photo);
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(detailsUtil.ENDPOINT)
                        .build();

                Toast.makeText(ViewClientScreen.this, "Uploading client's id photo",
                        Toast.LENGTH_LONG).show();

                ClientDetailsAPI apiId = adapter.create(ClientDetailsAPI.class);
                apiId.uploadClientIdPhoto(typedFile, clientDetails.getClientId().toString(),
                        new Callback<JSONObject>() {
                            @Override
                            public void success(JSONObject jsonObject, Response response) {
                                try {
                                    Toast.makeText(ViewClientScreen.this, "Client's id picture " +
                                                    "uploaded Successfully",
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(ViewClientScreen.this, error.toString(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                );

            } else { // Result was a failure
                Toast.makeText(ViewClientScreen.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == 1) {

            Intent returningIntent = data;
            String status = returningIntent.getExtras().getString("status");
            if (returningIntent.getExtras().getString("status") != null) {
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

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
    }

    public Bitmap rotateBitmapOrientation(String photoFilePath) throws IOException {
        Uri file = getPhotoFileUri(photoFileName);
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = new ExifInterface(photoFilePath);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

}