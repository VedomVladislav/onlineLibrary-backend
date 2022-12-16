package by.vedom.library.business.service;

import by.vedom.library.business.entity.Category;
import by.vedom.library.business.repository.CategoryRepository;
import by.vedom.library.business.search.CategorySearchValues;
import by.vedom.library.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findByUserEmail(String email) {
        return categoryRepository.findByUserEmailOrderByIdAsc(email);
    }

    public Category add(Category category) throws NotFoundException {

        if (category.getId() != null) {
            throw new NotFoundException("missed param id");
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            throw new NotFoundException("missed param title");
        }
        return categoryRepository.save(category);
    }

    public Category update(Category category) throws NotFoundException {

        if (category.getId() == null || category.getId() == 0L) {
            throw new NotFoundException("missed param id");
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            throw new NotFoundException("missed param title");
        }

        return categoryRepository.save(category);
    }

    public void delete(Long id) throws NotFoundException {
        if (id == null || id == 0) {
            throw new NotFoundException("missed param id");
        }

        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Category with id = " + id + " was not found");
        }
    }

    public List<Category> searchCategoriesByTitleAndEmail(CategorySearchValues categorySearchValues) {
        return categoryRepository.findByTitleAndEmail(categorySearchValues.getTitle(), categorySearchValues.getEmail());
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

}