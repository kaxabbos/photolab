package com.photolab.repo;

import com.photolab.model.Masters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MastersRepo extends JpaRepository<Masters, Long> {
    List<Masters> findAllByCategory(String category);
}
