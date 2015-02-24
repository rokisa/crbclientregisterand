package ke.co.example.weaversoft.crbclientregister.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * Created by weaversoft on 2/22/2015.
 */
public class ClientDetailsUtil {
    public static final String ENDPOINT
            ="http://10.0.2.2:9000/";

    public Date getDate(String strDate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");
        Date date = new Date();
        if (strDate.trim().length()!=0){
            try {
                return date = formatter.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else{
            return null;
        }
    }

    public String getLongDate(long dateInMillisecs){
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");
        String strDate = "";
        try {
            strDate = format.format(new Date(dateInMillisecs));
        } catch (Exception e){
        }
        return strDate;
    }

    public String getMimeType(File photo){
        try {
            return photo.toURL().openConnection().getContentType();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
