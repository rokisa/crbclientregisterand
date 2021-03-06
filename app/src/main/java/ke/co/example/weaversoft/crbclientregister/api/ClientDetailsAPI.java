package ke.co.example.weaversoft.crbclientregister.api;

import org.json.JSONObject;

import java.util.List;

import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;
import ke.co.example.weaversoft.crbclientregister.model.NextOfKin;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by weaversoft on 2/18/2015.
 */
public interface ClientDetailsAPI {

    @GET("/clientrec/list")
    public void fetchClientList(Callback<List<ClientDetails>> response);

    @POST("/clientrec/getnextofkin")
    public void fetchNextOfKin(@Body ClientDetails clientDetails,
                               Callback<List<NextOfKin>> response);

    @POST("/clientrec/save")
    public  void createClientDetails(@Body ClientDetails clientDetails,
                                     Callback<JSONObject> response);

    @POST("/clientrec/update")
    public  void updateClientDetails(@Body ClientDetails clientDetails,
                                     Callback<JSONObject> response);

    @Multipart
    @PUT("/clientrec/uploadidphoto")
    public void uploadClientIdPhoto(@Part("photo") TypedFile photo,
                                    @Part("clientId") String clientId,
                                    Callback<JSONObject> response);

    @Multipart
    @PUT("/clientrec/uploadprofilephoto")
    public void uploadClientProfilePhoto(@Part("photo") TypedFile photo,
                                    @Part("clientId") String clientId,
                                    Callback<JSONObject> response);

    @POST("/clientrec/addnextofkin")
    public void createNextOfKin(@Body NextOfKin nextOfKin,
                                Callback<JSONObject> response);

    @POST("/clientrec/updatenextofkin")
    public void updateNextOfKin(@Body NextOfKin nextOfKin,
                                Callback<JSONObject> response);


    @GET("/assets/images/clientphotos/{filename}")
    @Streaming
    void getClientPhoto(@Path("filename") String filename,
                Callback<Response> callback);

}
