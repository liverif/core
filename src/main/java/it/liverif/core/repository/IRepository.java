package it.liverif.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface IRepository<T extends AModelBean> {

    Optional<T> findOne(@Nullable Specification<T> whereCondition);

    List<T> findAll(@Nullable Specification<T> whereCondition);

    List<T> findAll(@Nullable Specification<T> whereCondition, @Nullable Sort sort);

    Page<T> findAll(@Nullable Specification<T> whereCondition, @Nullable Pageable pageable);

    long count(@Nullable Specification<T> whereCondition);

}
