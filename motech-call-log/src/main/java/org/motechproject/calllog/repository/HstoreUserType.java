package org.motechproject.calllog.repository;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class HstoreUserType implements UserType {

    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object o) throws HibernateException {
        Map m = (Map) o;
        if(o != null)
            return new HashMap(m);
        return new HashMap();
    }

    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) o;
    }

    public boolean equals(Object o1, Object o2) throws HibernateException {
        Map m1 = (Map) o1;
        Map m2 = (Map) o2;
        return m1.equals(m2);
    }

    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
        String col = strings[0];
        String val = resultSet.getString(col);
        return HstoreHelper.toMap(val);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
        String s = null;
        if(o != null)
            s = HstoreHelper.toString((Map) o);
        preparedStatement.setObject(i, s, Types.OTHER);
    }

    public boolean isMutable() {
        return true;
    }

    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return Map.class;
    }

    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }
}