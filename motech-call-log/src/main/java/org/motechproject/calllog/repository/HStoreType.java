package org.motechproject.calllog.repository;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HStoreType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class returnedClass() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isMutable() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
