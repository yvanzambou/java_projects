package de.yvanzambou.nachhilfe_einstein.service.impl;

import de.yvanzambou.nachhilfe_einstein.entity.Child;
import de.yvanzambou.nachhilfe_einstein.repository.ChildRepository;
import de.yvanzambou.nachhilfe_einstein.service.ChildService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;

    public ChildServiceImpl(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Override
    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }

    @Override
    public void deleteChildById(Long id) {
        childRepository.deleteById(id);
    }

    @Override
    public void saveChild(Child child) {
        childRepository.save(child);
    }

    @Override
    public Child getChildById(Long id) {
        return childRepository.findById(id).get();
    }
}
