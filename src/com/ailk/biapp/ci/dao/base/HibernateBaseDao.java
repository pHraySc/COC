package com.ailk.biapp.ci.dao.base;

import com.ailk.biapp.ci.util.ReflectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HibernateBaseDao<T, PK extends Serializable> {
    protected Logger logger = Logger.getLogger(this.getClass());
    protected SessionFactory sessionFactory;
    protected Class<T> entityClass;

    public HibernateBaseDao() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(this.getClass());
    }

    public HibernateBaseDao(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Autowired
    public void setSessionFactory(@Qualifier("ciSessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public void save(T entity) {
        Assert.notNull(entity, "entity can not be null!");
        this.getSession().saveOrUpdate(entity);
        this.logger.debug("save entity: {}" + entity);
    }

    public void delete(T entity) {
        Assert.notNull(entity, "entity can not be null!");
        this.getSession().delete(entity);
        this.logger.debug("delete entity: {}" + entity);
    }

    public void delete(PK id) {
        Assert.notNull(id, "id can not be null!");
        this.delete(this.get(id));
        this.logger.debug("save entity: {}" + this.entityClass.getSimpleName());
    }

    public T get(PK id) {
        Assert.notNull(id, "id can not be null!");
        return (T)this.getSession().get(this.entityClass, id);
    }

    public List<T> getAll() {
        return this.find(new Criterion[0]);
    }

    public List<T> getAll(String orderBy, boolean isAsc) {
        Criteria c = this.createCriteria(new Criterion[0]);
        if(isAsc) {
            c.addOrder(Order.asc(orderBy));
        } else {
            c.addOrder(Order.desc(orderBy));
        }

        return c.list();
    }

    public List<T> findBy(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName can not be null!");
        SimpleExpression criterion = Restrictions.eq(propertyName, value);
        return this.find(new Criterion[]{criterion});
    }

    public T findUniqueBy(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName can not be null!");
        SimpleExpression criterion = Restrictions.eq(propertyName, value);
        return (T)this.createCriteria(new Criterion[]{criterion}).uniqueResult();
    }

    public List<T> findByIds(List<PK> ids) {
        return this.find(new Criterion[]{Restrictions.in(this.getIdName(), ids)});
    }

    public <X> List<X> find(String hql, Object... values) {
        return this.createQuery(hql, values).list();
    }

    public <X> List<X> find(String hql, Map<String, ?> values) {
        return this.createQuery(hql, values).list();
    }

    public <X> X findUnique(String hql, Object... values) {
        return (X)this.createQuery(hql, values).uniqueResult();
    }

    public <X> X findUnique(String hql, Map<String, ?> values) {
        return (X)this.createQuery(hql, values).uniqueResult();
    }

    public int batchExecute(String hql, Object... values) {
        return this.createQuery(hql, values).executeUpdate();
    }

    public int batchExecute(String hql, Map<String, ?> values) {
        return this.createQuery(hql, values).executeUpdate();
    }

    public Query createQuery(String queryString, Object... values) {
        Assert.hasText(queryString, "queryString can not be null!");
        Query query = this.getSession().createQuery(queryString);
        if(values != null) {
            for(int i = 0; i < values.length; ++i) {
                query.setParameter(i, values[i]);
            }
        }

        return query;
    }

    public Query createQuery(String queryString, Map<String, ?> values) {
        Assert.hasText(queryString, "queryString can not be null!");
        Query query = this.getSession().createQuery(queryString);
        if(values != null) {
            query.setProperties(values);
        }

        return query;
    }

    public List<T> find(Criterion... criterions) {
        return this.createCriteria(criterions).list();
    }

    public T findUnique(Criterion... criterions) {
        return (T)this.createCriteria(criterions).uniqueResult();
    }

    public Criteria createCriteria(Criterion... criterions) {
        Criteria criteria = this.getSession().createCriteria(this.entityClass);
        Criterion[] arr$ = criterions;
        int len$ = criterions.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Criterion c = arr$[i$];
            criteria.add(c);
        }

        return criteria;
    }

    public void initEntity(T entity) {
        Hibernate.initialize(entity);
    }

    public void initEntity(List<T> entityList) {
        Iterator i$ = entityList.iterator();

        while(i$.hasNext()) {
            Object entity = i$.next();
            Hibernate.initialize(entity);
        }

    }

    public void flush() {
        this.getSession().flush();
    }

    public Query distinct(Query query) {
        query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return query;
    }

    public Criteria distinct(Criteria criteria) {
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    public String getIdName() {
        ClassMetadata meta = this.getSessionFactory().getClassMetadata(this.entityClass);
        return meta.getIdentifierPropertyName();
    }
}
