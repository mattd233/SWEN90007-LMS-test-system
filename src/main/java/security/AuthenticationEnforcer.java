package main.java.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class AuthenticationEnforcer {
//    private AuthenticationEnforcer instance;
//
//    public AuthenticationEnforcer getInstance() {
//        if (instance == null) {
//            instance = new AuthenticationEnforcer();
//        }
//        return instance;
//    }


    public static void setSecurityUtils(String path) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(path);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
    }

//    public LogInUser()
}
