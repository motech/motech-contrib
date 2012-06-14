package org.motechproject.security.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.security.domain.MotechWebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllMotechWebUsers extends MotechBaseRepository<MotechWebUser> {

    @Autowired
    protected AllMotechWebUsers(@Qualifier("webSecurityDbConnector") CouchDbConnector db) {
        super(MotechWebUser.class, db);
        initStandardDesignDocument();
    }

    @GenerateView
    public MotechWebUser findByUserName(String userName) {
        if (userName == null)
            return null;
        userName = userName.toLowerCase();
        ViewQuery viewQuery = createQuery("by_userName").key(userName).includeDocs(true);
        return singleResult(db.queryView(viewQuery, MotechWebUser.class));
    }

    @View(name = "find_by_role", map = "function(doc) {if (doc.type ==='MotechWebUser') {for(i in doc.roles) {emit(doc.roles[i], [doc._id]);}}}")
    public List<MotechWebUser> findByRole(String role) {
        if (role == null)
            return null;
        ViewQuery viewQuery = createQuery("find_by_role").key(role).includeDocs(true);
        return db.queryView(viewQuery, MotechWebUser.class);
    }

    @Override
    public void add(MotechWebUser user) {
        if (findByUserName(user.getUserName()) != null)
            return;
        super.add(user);
    }

    @Override
    public void update(MotechWebUser motechWebUser) {
        super.update(motechWebUser);
    }
}
