package org.motechproject.security.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.motechproject.security.domain.MotechWebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllMotechWebUsers extends CouchDbRepositorySupport<MotechWebUser> {

    @Autowired
    protected AllMotechWebUsers(@Qualifier("webSecurityDbConnector") CouchDbConnector db) {
        super(MotechWebUser.class, db);
        initStandardDesignDocument();
    }

    @View(name = "find_by_username",
            map = "  function(doc){" +
                    "   if(doc.type === 'MotechWebUser'){" +
                    "       emit(doc.userName, doc._id);" +
                    "   }" +
                    "}"
    )
    public MotechWebUser findByUserName(String userName) {
        ViewQuery viewQuery = createQuery("find_by_username").key(userName).includeDocs(true);
        List<MotechWebUser> users = db.queryView(viewQuery, MotechWebUser.class);
        return (users == null || users.isEmpty()) ? null : users.get(0);
    }
}
