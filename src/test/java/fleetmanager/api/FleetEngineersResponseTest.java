/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fleetmanager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author oliviagraham
 */
public class FleetEngineersResponseTest {
    
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    
    @Test
    public void serializesToJSON() throws Exception {
        final FleetEngineersResponse feResp = new FleetEngineersResponse(7);

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/fleetEngineersResponse.json"), 
                FleetEngineersResponse.class)
        );

        assertThat(MAPPER.writeValueAsString(feResp)).isEqualTo(expected);
    }
}