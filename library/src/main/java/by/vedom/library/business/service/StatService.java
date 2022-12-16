package by.vedom.library.business.service;

import by.vedom.library.business.entity.Stat;
import by.vedom.library.business.repository.StatRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatService {

    private final StatRepository statRepository;

    public StatService(StatRepository repository) {
        this.statRepository = repository;
    }

    public Stat findStat(String email) {
        return statRepository.findByUserEmail(email);
    }
}
