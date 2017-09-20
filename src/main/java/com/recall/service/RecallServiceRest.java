package com.recall.service;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Ryan
 */

@Path("service")
public class RecallServiceRest {
    
    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(){
        JSONObject obj = new JSONObject();
        obj.put("test", "pass");
        return Response.ok(obj.toJSONString()).build();
    }
    
    @GET
    @Path("/vehicle-list/{make}/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVehicleList(@PathParam("make") String make, @PathParam("year") String year){
        String urlString = "https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformakeyear/make/"+make+"/modelyear/"+year+"?format=json";
        return Response.ok(getServiceResponse(urlString).toJSONString()).build();
    }     
    
    @GET
    @Path("/recall-list/{model}/{make}/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecallList(@PathParam("model") String model, @PathParam("make") String make, @PathParam("year") String year){
        String urlString = "https://one.nhtsa.gov/webapi/api/Recalls/vehicle/modelyear/"+year+"/make/"+make+"/model/"+model+"?format=json";
        return Response.ok(getServiceResponse(urlString).toJSONString()).build();
    }
    
    public JSONObject getServiceResponse(String urlString){
        JSONObject jsonObject;
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(is, "UTF-8"));  
        } catch(Exception e){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return jsonObject;
    }
}
