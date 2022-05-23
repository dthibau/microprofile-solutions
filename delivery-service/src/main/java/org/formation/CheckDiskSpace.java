package org.formation;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

@ApplicationScoped
@Liveness
public class CheckDiskSpace implements HealthCheck {

	@ConfigProperty(name = "disk.threshold", defaultValue = "2048")
	long threshold;
	
    @Override
    public HealthCheckResponse call() {
    	HealthCheckResponseBuilder builder;
    	try {
			FileStore store = Files.getFileStore(Path.of(System.getProperty("user.dir")));
			builder = HealthCheckResponse.named("diskspace")
	                .withData("free", store.getUsableSpace()/(1024*1024) + " mB");
	        if ( store.getUsableSpace()/(1024*1024) > threshold ) {
	        	builder.up();
	        } else {
	        	builder.down();
	        }
		} catch (IOException e) {
			builder = HealthCheckResponse.named("diskspace")
	                .withData("free", "Unable to get free space")
	                .down();
		}
    	
        return builder.build();
    }
}