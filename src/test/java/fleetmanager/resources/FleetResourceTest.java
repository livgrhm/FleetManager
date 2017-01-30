/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fleetmanager.resources;

import fleetmanager.api.FleetEngineersResponse;
import javax.ws.rs.core.Response;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author oliviagraham
 */
public class FleetResourceTest {
    private FleetResource resource;
    
    @Before
    public void setup() {
        // Before each test, re-instantiate resource
        // Good practice for classes with mutable data so tests run independently
        resource = new FleetResource();
    }
    
    @Test
    public void badRequestOnEmptyScooterArray() {
        Response resp = resource.getFleetEngineers("[]", 12, 5);
        assertThat(resp.getStatus()).isEqualTo(400);
    }
    
    @Test
    public void badRequestOnNullScooterArray() {
        Response resp = resource.getFleetEngineers(null, 12, 5);
        assertThat(resp.getStatus()).isEqualTo(400);
    }
    
    @Test
    public void badRequestOnEmptyStringScooterArray() {
        Response resp = resource.getFleetEngineers("", 12, 5);
        assertThat(resp.getStatus()).isEqualTo(400);
    }
    
    @Test
    public void badRequestOnMalformedScooterArray() {
        Response resp = resource.getFleetEngineers("[15;10]", 12, 5);
        assertThat(resp.getStatus()).isEqualTo(400);
    }
    
    @Test
    public void badRequestOnZeroFMCapacity() {
        Response resp = resource.getFleetEngineers("[15,10]", 0, 5);
        assertThat(resp.getStatus()).isEqualTo(400);
    }
    
    @Test
    public void badRequestOnTooLargeFMCapacity() {
        Response resp = resource.getFleetEngineers("[15,10]", 1000, 5);
        assertThat(resp.getStatus()).isEqualTo(400);
    }
    
    @Test
    public void badRequestOnZeroFECapacity() {
        Response resp = resource.getFleetEngineers("[15,10]", 12, 0);
        assertThat(resp.getStatus()).isEqualTo(400);
    }
    
    @Test
    public void badRequestOnTooLargeFECapacity() {
        Response resp = resource.getFleetEngineers("[15,10]", 1001, 0);
        assertThat(resp.getStatus()).isEqualTo(400);
    }
    
    @Test
    public void simpleValidTestOne() {
        int expectedFleetEngineers = 3;
        Response resp = resource.getFleetEngineers("[15,10]", 12, 5);
        FleetEngineersResponse feResp = (FleetEngineersResponse)resp.getEntity();
        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(feResp.getFleetEngineers()).isEqualTo(expectedFleetEngineers);
    }
    
    @Test
    public void simpleValidTestTwo() {
        int expectedFleetEngineers = 7;
        Response resp = resource.getFleetEngineers("[11,15,13]", 9, 5);
        FleetEngineersResponse feResp = (FleetEngineersResponse)resp.getEntity();
        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(feResp.getFleetEngineers()).isEqualTo(expectedFleetEngineers);
    }
    
    @Test
    public void simpleValidTestThree() {
        int expectedFleetEngineers = 997;
        Response resp = resource.getFleetEngineers("[447,413,726,644]", 237, 2);
        FleetEngineersResponse feResp = (FleetEngineersResponse)resp.getEntity();
        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(feResp.getFleetEngineers()).isEqualTo(expectedFleetEngineers);
    }
}
