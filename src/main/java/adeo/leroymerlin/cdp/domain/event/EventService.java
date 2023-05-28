package adeo.leroymerlin.cdp.domain.event;

import adeo.leroymerlin.cdp.domain.band.Band;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.groupingBy;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    public void delete(Long id) {
        eventRepository.delete(id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Event updateReview(Event eventSaved, Event eventNotified) {
        eventSaved.setComment(eventNotified.getComment());
        eventSaved.setNbStars(eventNotified.getNbStars());
        return eventSaved;
    }

	public List<Event> getFilteredEvents(String query) {
		List<Event> events = eventRepository.findAllBy();
		// Filter the events list in pure JAVA here

		Map<Band, List<Event>> bandEvents = events.stream()
			.flatMap(event -> event.getBands().stream()
				.map(band -> new AbstractMap.SimpleEntry<>(band, event)))
			.collect(groupingBy(
				Map.Entry::getKey,
				Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        bandEvents.forEach((band, eventsForBand) -> {
            long countMemberWithNameContainQuery = band.getMembers().stream()
                .filter(member -> member.getName().contains(query))
                .count();

            if(countMemberWithNameContainQuery > 0){
                band.setName(band.getName() + " [" + countMemberWithNameContainQuery + "]");
                eventsForBand.forEach(event -> event.setBandCountForFilter(event.getBandCountForFilter() + 1));
            }
        });

        List<Event> result = events.stream()
            .filter(event -> event.getBandCountForFilter() > 0)
            .collect(Collectors.toList());

        result.forEach(event -> event.setTitle(event.getTitle() + " [" + event.getBandCountForFilter() + "]"));
        return result;
    }
}
