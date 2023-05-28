package adeo.leroymerlin.cdp.domain.event;

public final class EventTestUtils {

	private EventTestUtils() {

	}

	public static Event createEvent(Long id,
		String comment,
		Integer nbStars,
		String title,
		String imgUrl) {

		Event event = new Event();

		event.setId(id);
		event.setTitle(title);
		event.setImgUrl(imgUrl);
		event.setComment(comment);
		event.setNbStars(nbStars);

		return event;
	}
}
