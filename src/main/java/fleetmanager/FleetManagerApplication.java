package fleetmanager;

import fleetmanager.health.ServiceHealthCheck;
import fleetmanager.resources.FleetResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FleetManagerApplication extends Application<FleetManagerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new FleetManagerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Fleet Manager";
    }

    @Override
    public void initialize(final Bootstrap<FleetManagerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final FleetManagerConfiguration configuration,
                    final Environment environment) {
        
        // Setup webservice health check
        environment.healthChecks().register("serviceCheck", new ServiceHealthCheck());
        
        // Register fleet service
        final FleetResource fleetResource = new FleetResource();
        environment.jersey().register(fleetResource);
    }

}
