package by.vedom.library.business.service;

import by.vedom.library.business.entity.Task;
import by.vedom.library.business.repository.TaskRepository;
import by.vedom.library.business.search.TaskSearchValues;
import by.vedom.library.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskService {

    private static final String ID_COLUMN = "id";
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(String email) {
        return taskRepository.findByUserEmailOrderByTitleAsc(email);
    }

    public Task add(Task task) throws NotFoundException {

//        if (task.getId() != null || task.getId() != 0) {
//            throw new NotFoundException("missed param id");
//        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            throw new NotFoundException("missed param title");
        }
        return taskRepository.save(task);
    }

    public Task update(Task task) throws NotFoundException {

        if (task.getId() == null || task.getId() == 0L) {
            throw new NotFoundException("missed param id");
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            throw new NotFoundException("missed param title");
        }

        return taskRepository.save(task);
    }

    public void delete(Long id) throws NotFoundException {
        if (id == null || id == 0) {
            throw new NotFoundException("missed param id");
        }

        try {
            taskRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Task with id = " + id + " was not found");
        }
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Page<Task> find(TaskSearchValues taskSearchValues) {
        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle(): null;
        Integer completed = taskSearchValues.getCompleted() != null ? taskSearchValues.getCompleted(): null;
        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId(): null;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId(): null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn(): ID_COLUMN;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection(): null;

        int pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber(): 0;
        int pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize(): 10;

        String email = taskSearchValues.getEmail() != null ? taskSearchValues.getEmail(): null;

        Date dateFrom = taskSearchValues.getDateFrom() != null ? createDateInterval(0, 0, 0, 0) : null;
        Date dateTo = taskSearchValues.getDateTo() != null ? createDateInterval(23, 59, 59, 999): null;

        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0
                || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return taskRepository.find(title, completed, priorityId, categoryId, email, dateFrom, dateTo, pageRequest);
    }

    private Date createDateInterval(int hoursOfDay, int minute, int second, int millisecond) {
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hoursOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        date = calendar.getTime();
        return date;
    }
}
