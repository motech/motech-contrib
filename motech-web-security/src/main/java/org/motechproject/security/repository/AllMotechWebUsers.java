package org.motechproject.security.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.security.domain.MotechWebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AllMotechWebUsers extends MotechBaseRepository<MotechWebUser> {

    private PBEStringEncryptor encryptor;

    @Autowired
    protected AllMotechWebUsers(@Qualifier("webSecurityDbConnector") CouchDbConnector db, PBEStringEncryptor encryptor) {
        super(MotechWebUser.class, db);
        this.encryptor = encryptor;
        initStandardDesignDocument();
    }

    @GenerateView
    public MotechWebUser findByUserName(String userName) {
        if(userName == null)
            return null;
        userName = userName.toLowerCase();
        ViewQuery viewQuery = createQuery("by_userName").key(userName).includeDocs(true);
        MotechWebUser motechWebUser = singleResult(db.queryView(viewQuery, MotechWebUser.class));
        if (motechWebUser != null) {
            String decryptedPassword = encryptor.decrypt(motechWebUser.getPassword());
            motechWebUser.setPassword(decryptedPassword);
        }
        return motechWebUser;
    }

    @Override
    public void add(MotechWebUser user) {
        String encryptedPassword = encryptor.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        super.add(user);
    }

    @Override
    public void update(MotechWebUser user) {
        String encryptedPassword = encryptor.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        super.update(user);
    }

    public void changePassword(String userName, String newPassword) {
        MotechWebUser user = findByUserName(userName);
        if (user == null)
            return;
        String encryptedPassword = encryptor.encrypt(newPassword);
        user.setPassword(encryptedPassword);
        super.update(user);
    }

}
