package ke.co.example.weaversoft.crbclientregister;

import java.util.List;

import ke.co.example.weaversoft.crbclientregister.model.ClientDetails;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by weaversoft on 2/18/2015.
 */
public interface ClientDetailsAPI {

    @GET("/clientrec/list")
    public void fetchClientList(Callback<List<ClientDetails>> response);
}
