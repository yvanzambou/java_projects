package de.yvanzambou.nachhilfe_einstein.service;

import de.yvanzambou.nachhilfe_einstein.entity.Child;

import java.util.List;

public interface ChildService {

    List<Child> getAllChildren();

    void deleteChildById(Long id);

    void saveChild(Child child);

    Child getChildById(Long id);
}
