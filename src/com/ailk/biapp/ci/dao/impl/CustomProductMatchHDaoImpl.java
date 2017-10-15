package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICustomProductMatchHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CustomProductMatch;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

@Repository
public class CustomProductMatchHDaoImpl extends HibernateBaseDao<CustomProductMatch, String> implements ICustomProductMatchHDao {
    public CustomProductMatchHDaoImpl() {
    }

    public List<CustomProductMatch> selectByListTableName(String listTableName) {
        return super.find("from CustomProductMatch where listTableName = ? order by matchPropotion desc", new Object[]{listTableName});
    }

    public List<CustomProductMatch> selectByCustomProductMatch(CustomProductMatch customProductMatch) {
        Criteria c = this.getSession().createCriteria(CustomProductMatch.class);
        Example example = Example.create(customProductMatch);
        c.add(example);
        return c.list();
    }

    public void insertOrUpdate(CustomProductMatch customProductMatch) {
        super.save(customProductMatch);
    }

    public void delete(CustomProductMatch customProductMatch) {
        super.delete(customProductMatch);
    }

    public void deleteByListTableName(String listTableName) {
        super.batchExecute("delete from CustomProductMatch where listTableName = ? ", new Object[]{listTableName});
    }
}
