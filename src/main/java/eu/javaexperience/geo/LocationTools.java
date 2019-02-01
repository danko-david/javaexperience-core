package eu.javaexperience.geo;

public class LocationTools
{	
	//https://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
	public static double getMeterDistance(Location l1, Location l2)
	{
		double earthRadius = 6371000; //meters
		double dLat = Math.toRadians(l2.getLatitude()-l1.getLatitude());
		double dLng = Math.toRadians(l2.getLongitude()-l1.getLongitude());
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(l1.getLatitude())) * Math.cos(Math.toRadians(l2.getLatitude())) *
				Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return earthRadius * c;
	}
}
