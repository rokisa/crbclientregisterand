package ke.co.example.weaversoft.crbclientregister;

import org.json.JSONObject;

import java.util.List;

import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by weaversoft on 2/18/2015.
 */
public interface ClientDetailsAPI {

    @GET("/clientrec/list")
    public void fetchClientList(Callback<List<ClientDetails>> response);

    @POST("/clientrec/save")
    public  void createClientDetails(@Body ClientDetails clientDetails,
                                     Callback<JSONObject> response);
}
