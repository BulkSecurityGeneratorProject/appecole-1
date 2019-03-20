package ma.xavion.appecole.service.impl;

import ma.xavion.appecole.service.NotificationService;
import ma.xavion.appecole.domain.Notification;
import ma.xavion.appecole.repository.NotificationRepository;
import ma.xavion.appecole.repository.search.NotificationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Notification.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationSearchRepository notificationSearchRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationSearchRepository notificationSearchRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationSearchRepository = notificationSearchRepository;
    }

    /**
     * Save a notification.
     *
     * @param notification the entity to save
     * @return the persisted entity
     */
    @Override
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        Notification result = notificationRepository.save(notification);
        notificationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Notification> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable);
    }


    /**
     * Get one notification by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
        notificationSearchRepository.deleteById(id);
    }

    /**
     * Search for the notification corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Notification> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notifications for query {}", query);
        return notificationSearchRepository.search(queryStringQuery(query), pageable);    }
}
