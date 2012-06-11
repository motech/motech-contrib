package org.motechproject.security.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.security.domain.MotechWebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

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
        if (userName == null)
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

    @View(name = "find_by_role", map = "function(doc) {if (doc.type ==='MotechWebUser') {for(i in doc.roles) {emit(doc.roles[i], [doc._id]);}}}")
    public List<MotechWebUser> findByRole(String role) {
        if (role == null)
            return null;
        ViewQuery viewQuery = createQuery("find_by_role").key(role).includeDocs(true);
        return db.queryView(viewQuery, MotechWebUser.class);
    }

    @Override
    public void add(MotechWebUser webUser) {
        String encryptedPassword = encryptor.encrypt(webUser.getPassword());
        webUser.setPassword(encryptedPassword);
        super.add(webUser);
    }

    @Override
    public void update(MotechWebUser webUser) {
        String encryptedPassword = encryptor.encrypt(webUser.getPassword());
        webUser.setPassword(encryptedPassword);
        super.update(webUser);
    }

    public void changePassword(String userName, String newPassword) {
        MotechWebUser webUser = findByUserName(userName);
        if (webUser == null)
            return;
        String encryptedPassword = encryptor.encrypt(newPassword);
        webUser.setPassword(encryptedPassword);
        super.update(webUser);
    }

}
