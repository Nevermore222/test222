package com.gm.wj.dao;

import com.gm.wj.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review, Integer> {
    @Query(value = "select r from Review r where r.type= :type")
    List<Review> getReviewByType(@Param("type") String type);
}
