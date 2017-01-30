package fleetmanager.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * Fleet Manager Webservice Health Check
 * Exposes healthcheck endpoint to check service is up and running
 * 
 * $ curl -i localhost:8081/healthcheck
 * 
 * Will return HTTP 200 status if service is up and running.
 *
 * @author oliviagraham
 */
public class ServiceHealthCheck extends HealthCheck {
    
    
    public ServiceHealthCheck () {
        super();
    }
    
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
