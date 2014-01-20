package com.voodie.domain.identity;

import com.voodie.domain.AbstractJpaTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Voodie
 * User: MikeD
 */
public class AuthoritiesTest extends AbstractJpaTest {

    @Test
    public void testPersistWithoutAuthority(){
        Authorities authority = new Authorities();
        persistExpectingError(authority);
    }

    @Test
    @Ignore("unique constraint not created for unit tests")
    public void testPersitWithDuplicate(){
        Authorities authority = new Authorities();
        authority.setAuthority("authority");
        persistExpectingSuccess(authority);

        Authorities authority2 = new Authorities();
        authority2.setAuthority("authority");
        persistExpectingError(authority2);
    }

    @Test
    public void testValidPersist(){
        Authorities authority = new Authorities();
        authority.setAuthority("authority");
        persistExpectingSuccess(authority);
    }
}
