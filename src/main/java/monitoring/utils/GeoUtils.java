package monitoring.utils;

public class GeoUtils {

	public static double calcDistance(double lat1, double lon1, double lat2, double lon2) {
		long radius = 6371L;

		double rlat1 = Math.toRadians(lat1);
		double rlon1 = Math.toRadians(lon1);
		double rlat2 = Math.toRadians(lat2);	
		double rlon2 = Math.toRadians(lon2);

		double lat_delta = rlat2 - rlat1;
		double lon_delta = rlon2 - rlon1;
		
		return radius * 2 * Math.asin(
				Math.sqrt(
						Math.pow(Math.sin(lat_delta / 2), 2)
						+ Math.cos(rlat1) * Math.cos(rlat2) * Math.pow(Math.sin(lon_delta / 2),2)
					)
				);
	}

}
