package eu.javaexperience.resource.pool;

/**
 * Tracked resource pool is used for controlled resources,
 * the resource wrapped into an {@link IssuedResource} instance,
 * which can be freed in place or directly give back for the issuer for release.
 * This method of issuing prevents from poisoning the bookkeeping of free instances
 * and gives (implementation shall give) an indirect reference to the issued resource
 * in order to developer can identify the leaking issuer. 
 * */
public interface TrackedResourcePool<T> extends ResourcePool<IssuedResource<T>>
{

}