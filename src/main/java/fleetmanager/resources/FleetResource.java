package fleetmanager.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import fleetmanager.api.FleetEngineersResponse;
import fleetmanager.core.FleetConfig;
import java.util.Arrays;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Endpoints for fleet management
 * 
 * @author oliviagraham
 */
@Path("/fleet")
@Produces(MediaType.APPLICATION_JSON)
public class FleetResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FleetResource.class);
    
    public FleetResource () { }
    
    /**
     * Returns the minimum number of fleet engineers to maintain all scooters
     * within a specified fleet
     * 
     * $ curl -i localhost:8080/fleet
     * 
     * @param scooters      fleet array; elements represent number of 
     *                      scooters by district
     * @param fmCapacity    num scooters the fleet manager can maintain
     * @param feCapacity    num scooters a fleet engineer can maintain
     * @return              num fleet engineers required
     */
    @GET
    @Timed
    public Response getFleetEngineers (
            @QueryParam("scooters") String scooters,
            @QueryParam("fmCapacity") int fmCapacity, 
            @QueryParam("feCapacity") int feCapacity) {
        
        // Check for well formed parameters
        if (!this.checkParams(scooters, fmCapacity, feCapacity)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        // Deserialize the scooter array
        ObjectMapper mapper = new ObjectMapper();
        double[] scooterList;
        try {
            scooterList = mapper.readValue(scooters, double[].class);
            if (scooterList.length < 1 || scooterList.length > 100) {
                LOGGER.error("Scooter array must have between 1 and 100 elements.");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            LOGGER.error("Error deserializing scooter array: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        // Create fleet config, calculate fleet engineers required
        FleetConfig cfg = new FleetConfig(scooterList, fmCapacity, feCapacity);
        cfg.calculateFleetEngineersv2();
        
        // Return response
        FleetEngineersResponse response = new FleetEngineersResponse(cfg.getTotalEngineersRequired());
        return Response.status(Response.Status.OK).entity(response).build();
    }
    
    private Boolean checkParams(String scooters, int fmCapacity, int feCapacity) {
        return !(scooters == null || scooters.trim().equals("")
                || fmCapacity < 1 || feCapacity < 1
                || fmCapacity > 999 || feCapacity > 1000);
    }

    @GET
    @Timed
    @Path("/test")
    public Response testDelegationLogic () {

        // 1. an array of scooters, with 0 - 100 elements
        int scooterArraySize = (int)(Math.random() * 100 + 1);   // num between 1 and 100
        double[] scooterList = new double[scooterArraySize];
        
        for (int i = 0; i < scooterList.length; i++) {
            scooterList[i] = Math.ceil(Math.random() * 1000 + 1);
        }

        // 2. C = a number between 1 and 999
        int c = (int)(Math.random() * 999 + 1);   // num between 1 and 999

        // 3. P = a number between 1 and 1000
        int p = (int)(Math.random() * 1000 + 1);   // num between 1 and 1000

        System.out.println("==========");
        System.out.println("RANDOM TEST!");
        System.out.println("Scooters: " + Arrays.toString(scooterList));
        System.out.println("Scooters Size: " + scooterList.length);
        System.out.println("FM Capacity: " + c);
        System.out.println("FE Capacity: " + p);
        System.out.println("==========");

        // Create fleet config, calculate fleet engineers required
        FleetConfig cfg = new FleetConfig(scooterList, c, p);
        cfg.calculateFleetEngineersv2();

        // Return response
        FleetEngineersResponse response = new FleetEngineersResponse(cfg.getTotalEngineersRequired());
        return Response.status(Response.Status.OK).entity(response).build();
    }
}
