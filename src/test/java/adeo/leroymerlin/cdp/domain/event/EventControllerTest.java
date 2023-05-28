package adeo.leroymerlin.cdp.domain.event;

import adeo.leroymerlin.cdp.domain.event.Event;
import adeo.leroymerlin.cdp.domain.event.EventController;
import adeo.leroymerlin.cdp.domain.event.EventService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventControllerTest {
	@InjectMocks
	private EventController eventController;

	@Mock
	private EventService eventService;

	@Test
	public void findEvents_ReturnsListOfEvents() {
		List<Event> expectedEvents = new ArrayList<>();
		expectedEvents.add(new Event());
		expectedEvents.add(new Event());
		Mockito.when(eventService.getEvents()).thenReturn(expectedEvents);

		List<Event> actualEvents = eventController.findEvents();

		assertEquals(expectedEvents, actualEvents);
	}

	@Test
	public void findEvents_WithQuery_ReturnsFilteredEvents() {
		String query = "Queen";
		List<Event> expectedEvents = new ArrayList<>();
		expectedEvents.add(new Event());
		expectedEvents.add(new Event());
		Mockito.when(eventService.getFilteredEvents(query)).thenReturn(expectedEvents);

		List<Event> actualEvents = eventController.findEvents(query);

		assertEquals(expectedEvents, actualEvents);
	}

	@Test
	public void deleteEvent_WithValidId_CallsEventServiceDelete() {
		Long eventId = 1L;

		eventController.deleteEvent(eventId);

		verify(eventService, Mockito.times(1)).delete(eventId);
	}

	@Test
	public void updateEvent_WithValidId_ReturnsUpdatedEvent() {
		Long eventId = 1L;
		Event updatedEvent = new Event();
		updatedEvent.setId(eventId);
		Mockito.when(eventService.findById(eventId)).thenReturn(Optional.of(new Event()));
		Mockito.when(eventService.updateReview(any(), any())).thenReturn(updatedEvent);

		ResponseEntity<Event> response = eventController.updateEvent(eventId, updatedEvent);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedEvent, response.getBody());
	}

	@Test
	public void updateEvent_WithInvalidId_ReturnsNotFound() {
		Long eventId = 1L;
		Event updatedEvent = new Event();
		updatedEvent.setId(eventId);
		Mockito.when(eventService.findById(eventId)).thenReturn(Optional.empty());

		ResponseEntity<Event> response = eventController.updateEvent(eventId, updatedEvent);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
