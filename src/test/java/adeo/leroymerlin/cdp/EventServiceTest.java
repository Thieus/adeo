package adeo.leroymerlin.cdp;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

	@Mock
	private EventRepository eventRepository;

	@InjectMocks
	private EventService eventService;

	@Test
	public void testFindById() {
		Long id = 1L;
		eventService.findById(id);
		verify(eventRepository).findById(id);
	}

	@Test
	public void testGetEvents() {
		eventService.getEvents();
		verify(eventRepository).findAllBy();
	}

	@Test
	public void testDelete() {
		Long id = 1L;
		eventService.delete(id);
		verify(eventRepository).delete(id);
	}

	@Test
	public void testUpdateReview(){
		Event eventSaved = createEvent(
			1L,
			"Comment Saved",
			5,
			"Title test",
			"img url");

		Set<Band> bands = new HashSet<>();
		Band band1 = createBand("Band1");
		bands.add(band1);

		Event eventNotified = createEvent(
			1L,
			"Comment Notified",
			4,
			"Title Notified",
			"img url Notified");
		eventNotified.setBands(bands);

		Event eventResult = eventService.updateReview(eventSaved, eventNotified);
		// not updated
		assertEquals(eventResult.getId(), eventSaved.getId());
		assertEquals(eventResult.getTitle(), eventSaved.getTitle());
		assertEquals(eventResult.getImgUrl(), eventSaved.getImgUrl());
		assertEquals(eventResult.getBands(), eventSaved.getBands());
		// authorization updated
		assertEquals(eventResult.getComment(), eventNotified.getComment());
		assertEquals(eventResult.getNbStars(), eventNotified.getNbStars());
	}

	@Test
	public void getFilteredEvents_ReturnsFilteredEvents() {
		// Arrange
		String query = "Queen";
		Event event1 = createEvent(1L, "", 0, "Event 1", "" );
		Event event2 = createEvent(2L, "", 0,  "Event 2", "");
		Event event3 = createEvent(3L, "", 0, "Event 3", "");
		Band band1 = createBand("Band 1");
		band1.setMembers(Collections.singleton(createMember("Queen")));
		Band band2 = createBand("Band 2");
		band2.setMembers(Collections.singleton(createMember("King")));
		Band band3 = createBand("Band 3");
		band3.setMembers(new HashSet<>(Arrays.asList(
			createMember("Queen"),
			createMember("Felix")
		)));
		event1.setBands(new HashSet<>(Arrays.asList(
			band1,
			band2
		)));
		event2.setBands(new HashSet<>(Arrays.asList(
			band1,
			band3
		)));
		event3.setBands(new HashSet<>(Collections.singletonList(
			band2
		)));


		List<Event> allEvents = Arrays.asList(event1, event2, event3);
		when(eventRepository.findAllBy()).thenReturn(allEvents);

		List<Event> filteredEvents = eventService.getFilteredEvents(query);

		assertEquals(2, filteredEvents.size());

		Event eventResult1 = filteredEvents.stream().filter(event -> event.getId().equals(1L))
			.findFirst().orElseThrow(() -> new RuntimeException("Event not found"));
		assertEquals("Event 1 [1]", eventResult1.getTitle());

		Set<Band> bandsOfEvent1 = eventResult1.getBands();
		Band band1Result = bandsOfEvent1.stream().filter(band -> band.equals(band1)).findFirst()
			.orElseThrow(() -> new RuntimeException("Band not found"));
		Band band2Result = bandsOfEvent1.stream().filter(band -> band.equals(band2)).findFirst()
				.orElseThrow(() -> new RuntimeException("Band not found"));
		assertEquals("Band 1 [1]", band1Result.getName());
		assertEquals("Band 2", band2Result.getName());

		Event eventResult2 = filteredEvents.stream().filter(event -> event.getId().equals(2L))
				.findFirst().orElseThrow(() -> new RuntimeException("Event not found"));
		assertEquals("Event 2 [2]", eventResult2.getTitle());

		Set<Band> bandsOfEvent2 = eventResult2.getBands();
		Band band1Result2 = bandsOfEvent2.stream().filter(band -> band.equals(band1)).findFirst()
			.orElseThrow(() -> new RuntimeException("Band not found"));
		Band band3Result2 = bandsOfEvent2.stream().filter(band -> band.equals(band3)).findFirst()
			.orElseThrow(() -> new RuntimeException("Band not found"));
		assertEquals("Band 1 [1]", band1Result2.getName());
		assertEquals("Band 3 [1]", band3Result2.getName());
	}

	private Band createBand(String name) {
		Band band = new Band();
		band.setName(name);
		return band;
	}

	private Member createMember(String name) {
		Member member = new Member();
		member.setName(name);
		return member;
	}

	private Event createEvent(Long id,
		String comment,
		Integer nbStars,
		String title,
		String imgUrl) {

		Event event = new Event();

		event.setId(id);
		event.setComment(comment);
		event.setNbStars(nbStars);
		event.setTitle(title);
		event.setImgUrl(imgUrl);

		return event;
	}
}