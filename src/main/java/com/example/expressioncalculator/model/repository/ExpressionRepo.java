package com.example.expressioncalculator.model.repository;

import com.example.expressioncalculator.model.entitie.Expression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpressionRepo extends JpaRepository<Expression, Integer> {

    @Query("select b from Expression b where b.result = :result")
    List<Expression> findByResultEqual(@Param("result") double result);

    @Query("select b from Expression b where b.result > :result")
    List<Expression> findByResultGreater(@Param("result") double result);

    @Query("select b from Expression b where b.result < :result")
    List<Expression> findByResultLess(@Param("result") double result);

}
