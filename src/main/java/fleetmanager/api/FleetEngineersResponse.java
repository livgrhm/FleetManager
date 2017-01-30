package fleetmanager.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard response for endpoint to find minimum number of FE's required.
 * 
 * @author oliviagraham
 */
public class FleetEngineersResponse {
    
    private int fleetEngineers;
    
    public FleetEngineersResponse () { }
    
    public FleetEngineersResponse (int fleetEngineers) {
        this.fleetEngineers = fleetEngineers;
    }
    
    @JsonProperty
    public int getFleetEngineers () {
        return fleetEngineers;
    }
}
