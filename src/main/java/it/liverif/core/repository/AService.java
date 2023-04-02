package it.liverif.core.repository;

import it.liverif.core.auth.AUserAuth;
import it.liverif.core.exeptions.DatabaseException;
import it.liverif.core.web.beans.SearchField;
import it.liverif.core.web.view.detail.ADetailResponse;
import it.liverif.core.web.view.list.AListResponse;
import it.liverif.core.repository.predicates.*;
import it.liverif.core.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@EnableTransactionManagement
public abstract class AService<T extends AModelBean, J extends JpaRepository<T, Long> & IRepository<T>> extends AUserAuth {

    @Autowired
    HttpServletRequest request;

    @Autowired
    protected EntityManager entityManager;
    
    @Autowired
    protected J repository;

    @Autowired
    protected LocalDateTimePredicate<T> localDateTimePredicate;

    @Autowired
    protected LocalDatePredicate<T> localDatePredicate;

    @Autowired
    protected StringPredicate<T> stringPredicate;

    @Autowired
    protected IntegerPredicate<T> integerPredicate;

    @Autowired
    protected DoublePredicate<T> doublePredicate;

    @Autowired
    protected LongPredicate<T> longPredicate;

    @Autowired
    protected BooleanPredicate<T> booleanPredicate;

    @Transactional(readOnly = true)
    public T get(final Long id) throws Exception {
        return repository.getById(id);
    }

    @Transactional(readOnly = true)
    public T get() throws Exception {
        if (repository.findById(1L).isPresent()) return repository.getById(1L);
        return null;
    }

    public void detach(T entity){
        entityManager.detach(entity);
    }

    public abstract boolean beforeSave(T entity) throws Exception;

    public abstract void afterSave(T entity) throws Exception;

    @Transactional(rollbackFor = Exception.class)
    public Long save(T entity) throws Exception {
        if(entity.getId()!=null && entity.getId()>0L) throw new DatabaseException("Entity saved");
        entity.beforePersist();
        boolean save=beforeSave(entity);
        Long id = 0L;
        if(save) id = repository.save(entity).getId();
        afterSave(entity);
        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveImmediate(T entity) {
        entity.beforePersist();
        Long id = repository.save(entity).getId();
        return id;
    }

    public abstract boolean beforeUpdate(T entity) throws Exception;

    public abstract void afterUpdate(T entity) throws Exception;

    @Transactional(rollbackFor = Exception.class)
    public Long update(T entity) throws Exception {
        if(entity.getId()==null || entity.getId()<=0L) throw new DatabaseException("Entity not saved");
        entity.beforePersist();
        boolean update=beforeUpdate(entity);
        Long id = entity.getId();
        if(update) id = repository.saveAndFlush(entity).getId();
        afterUpdate(entity);
        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateImmediate(T entity) throws Exception {
        if(entity.getId()==null || entity.getId()<=0L) throw new DatabaseException("Entity not saved");
        entity.beforePersist();
        Long id = repository.saveAndFlush(entity).getId();
        return id;
    }

    public abstract boolean beforeDelete(T entity) throws Exception;

    public abstract void afterDelete(T entity) throws Exception;

    @Transactional(rollbackFor = Exception.class)
    public void delete(T entity) throws Exception {
        boolean delete=beforeDelete(entity);
        if(delete) repository.delete(entity);
        afterDelete(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteImmediate(T entity) {
        repository.delete(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) throws Exception {
        T entity = repository.getById(id);
        boolean delete=beforeDelete(entity);
        if(delete) repository.delete(entity);
        afterDelete(entity);
    }

    public void deleteById(final Long id) throws Exception {
        T entity = repository.getById(id);
        delete(entity);
    }

    @Transactional(readOnly = true)
    public List<T> listAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<T> list(Specification<T> specification) {
        return repository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public List<T> list(Specification<T> specification,Sort sort) {
        return repository.findAll(specification,sort);
    }

    @Transactional(readOnly = true)
    public Page<T> list(int pageSelected, int viewRowNumber, Sort sort) {
        Pageable pageable = PageRequest.of(pageSelected, viewRowNumber, sort);
        Page<T> page = repository.findAll(pageable);
        return page;
    }

    @Transactional(readOnly = true)
    public Long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public Long count(Specification<T> specification) {
        return repository.count(specification);
    }

    public Page<T> list(int viewRowNumber, Sort sort, AListResponse listResponseModel) {
        return list(viewRowNumber, sort, listResponseModel, null);
    }

    @Transactional(readOnly = true)
    public Page<T> list(int viewRowNumber, Sort sort, AListResponse listResponse, List<Specification<T>> addWhereCondition) {
        int pageSelected = listResponse.getSelectPage() - 1;
        List<Specification<T>> whereCondition = new ArrayList();
        if (addWhereCondition != null) whereCondition.addAll(addWhereCondition);
        for (int i = 0; i < listResponse.getSearchFields().size(); i++) {
            SearchField searchField = (SearchField) listResponse.getSearchFields().get(i);
            if (searchField.getOperation() == null) break;
            Specification<T> specification = null;
            try {
                specification = new Specification<>() {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        if (searchField.getType().equals(LocalDateTime.class.getName())) {
                            return localDateTimePredicate.toPredicate(root, criteriaBuilder, searchField);
                        } else if (searchField.getType().equals(LocalDate.class.getName())) {
                            return localDatePredicate.toPredicate(root, criteriaBuilder, searchField);
                        } else if (searchField.getType().equals(String.class.getName())) {
                            return stringPredicate.toPredicate(root, criteriaBuilder, searchField);
                        } else if (searchField.getType().equals(Integer.class.getName())) {
                            return integerPredicate.toPredicate(root, criteriaBuilder, searchField);
                        } else if (searchField.getType().equals(Double.class.getName())) {
                            return doublePredicate.toPredicate(root, criteriaBuilder, searchField);
                        } else if (searchField.getType().equals(Long.class.getName())) {
                            return longPredicate.toPredicate(root, criteriaBuilder, searchField);
                        } else if (searchField.getType().equals(Boolean.class.getName())) {
                            return booleanPredicate.toPredicate(root, criteriaBuilder, searchField);
                        } else {
                            log.error("add new predicate type: " + searchField.getType());
                            return null;
                        }
                    }
                };
            } catch (Exception e) {
                log.error("Error from field " + searchField.getField());
                log.error(CommonUtils.errorPrintStackTrace(e).toString());
            }
            if (specification != null) whereCondition.add(specification);
        }

        Specification<T> spec = null;
        for (int i = 0; i < whereCondition.size(); i++) {
            Specification<T> s = whereCondition.get(i);
            if (i == 0)
                spec = Specification.where(s);
            else
                spec = spec.and(s);
        }

        Pageable pageable = PageRequest.of(pageSelected, viewRowNumber, sort);
        Page<T> page = repository.findAll(spec, pageable);
        return page;
    }

    protected Object getHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute);
    }

    protected void setHttpSession(String attribute, Object object) {
        request.getSession().setAttribute(attribute, object);
    }

    protected void removeHttpSession(String attribute) {
        request.getSession().removeAttribute(attribute);
    }

    protected AListResponse getListResponse() {
        return getListResponse(modelName());
    }

    protected AListResponse getListResponse(String modelName){
        AListResponse listResponse = (AListResponse) getHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName);
        return listResponse;
    }

    protected void removeListResponse(String modelName){
        removeHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName);
    }

    protected ADetailResponse getDetailResponse(String modelName) {
        ADetailResponse detailResponse = (ADetailResponse) getHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
        return detailResponse;
    }

    protected void removeDetailResponse(String modelName){
        removeHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
    }

    protected ADetailResponse getDetailResponse() {
        return getDetailResponse(modelName());
    }

    protected Class<T> modelEntityClass(){
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected String tableName(){
        return modelName().toLowerCase();
    }

    protected String modelName(){
        String className=CommonUtils.removeEntitySuffix(modelEntityClass().getSimpleName());
        return StringUtils.uncapitalize(className);
    }

}
