package adeo.leroymerlin.cdp.domain.event;

import adeo.leroymerlin.cdp.domain.event.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EventRepository extends Repository<Event, Long> {

    void delete(Long eventId);

    List<Event> findAllBy();

    Optional<Event> findById(Long eventId);
}
