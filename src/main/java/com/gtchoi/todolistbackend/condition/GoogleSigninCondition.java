package com.gtchoi.todolistbackend.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class GoogleSigninCondition implements Condition {

    Logger logger = LoggerFactory.getLogger(GoogleSigninCondition.class);

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        if (env.containsProperty("google.oauth.client-id") && env.containsProperty("google.oauth.client-secret")) {
            return true;
        } else {
            logger.info("GoogleSigninService not created.");
            return false;
        }
    }
}
