package org.formation;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/api/v1")
@LoginConfig(authMethod = "MP-JWT", realmName = "quarkus-app")
public class DeliveryServiceApplication extends Application {

}
