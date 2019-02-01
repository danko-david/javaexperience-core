package eu.javaexperience.semantic.designedfor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation means, that the request for Object identified by the same
 * invariance results the same Object on every request.
 * 
 * That means if you have a DatabaseMapper (if it is an {@link InstanceSupervisor})
 * and you get a record (identified by id) and you get again the same record
 * the both object will be identically equals (DatabaseMapper shall care about)
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface InstanceSupervisor{}