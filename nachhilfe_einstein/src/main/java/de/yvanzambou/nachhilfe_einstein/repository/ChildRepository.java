package de.yvanzambou.nachhilfe_einstein.repository;

import de.yvanzambou.nachhilfe_einstein.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {}
