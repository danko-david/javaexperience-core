package eu.javaexperience.semantic.designedfor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation means, that returning or published object
 * is supervised, the provider for every request will return (or anyway publish)
 * the same instance. (This objects will be identically equals == )
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentityObject{}