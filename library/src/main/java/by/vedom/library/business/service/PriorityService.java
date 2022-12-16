package by.vedom.library.business.service;

import by.vedom.library.business.entity.Priority;
import by.vedom.library.business.repository.PriorityRepository;
import by.vedom.library.business.search.PrioritySearchValues;
import by.vedom.library.exception.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public PriorityService(PriorityRepository repository) {
        this.priorityRepository = repository;
    }

    public List<Priority> findAll(String email) {
        return priorityRepository.findByUserEmailOrderByIdAsc(email);
    }

    public Priority add(Priority priority) throws NotFoundException {

        if (priority.getId() != null || priority.getId() != 0) {
            throw new NotFoundException("missed param id");
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            throw new NotFoundException("missed param title");
        }
        return priorityRepository.save(priority);
    }

    public Priority update(Priority priority) throws NotFoundException {

        if (priority.getId() == null || priority.getId() == 0L) {
            throw new NotFoundException("missed param id");
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            throw new NotFoundException("missed param title");
        }

        return priorityRepository.save(priority);
    }

    public void delete(Long id) throws NotFoundException {
        if (id == null || id == 0) {
            throw new NotFoundException("missed param id");
        }

        try {
            priorityRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Category with id = " + id + " was not found");
        }
    }

    public Priority findById(Long id) {
        return priorityRepository.findById(id).orElse(null);
    }

    public List<Priority> findPrioritiesByTitleAndUserEmail(PrioritySearchValues prioritySearchValues) {
        return priorityRepository.findPrioritiesByTitleAndUserEmail(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());
    }
}
