package adeo.leroymerlin.cdp.domain.band;

public final class BandTestUtils {

	private BandTestUtils(){

	}

	public static Band createBand(String name) {
		Band band = new Band();
		band.setName(name);
		return band;
	}
}
