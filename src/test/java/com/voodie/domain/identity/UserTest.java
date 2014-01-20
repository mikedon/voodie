package com.voodie.domain.identity;

import com.voodie.domain.AbstractJpaTest;
import org.junit.Test;

/**
 * Voodie
 * User: MikeD
 */
public class UserTest extends AbstractJpaTest {

    @Test
    public void testPersistWithoutUsername(){
        User user = new User();
        user.setEmailAddress("test@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        persistExpectingError(user);
    }

    @Test
    public void testPersistWithoutPassword(){
        User user = new User();
        user.setUsername("username");
        user.setEmailAddress("test@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        persistExpectingError(user);
    }

    @Test
    public void testPersistWithoutFirstName(){
        User user = new User();
        user.setUsername("username");
        user.setEmailAddress("test@email.com");
        user.setLastName("lastName");
        user.setPassword("password");
        persistExpectingError(user);
    }

    @Test
    public void testPersistWitoutLastName(){
        User user = new User();
        user.setUsername("username");
        user.setEmailAddress("test@email.com");
        user.setFirstName("lastName");
        user.setPassword("password");
        persistExpectingError(user);
    }

    @Test
    public void testPersistWithoutEmailAddress(){
        User user = new User();
        user.setUsername("username");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        persistExpectingError(user);
    }

    @Test
    public void testPersistWithDuplicate(){
        User user = new User();
        user.setUsername("username");
        user.setEmailAddress("test@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        persistExpectingSuccess(user);

        User user2 = new User();
        user2.setUsername("username");
        user2.setEmailAddress("test@email.com");
        user2.setFirstName("firstName");
        user2.setLastName("lastName");
        user2.setPassword("password");
        persistExpectingError(user2);
    }

    @Test
    public void testValidPersist(){
        User user = new User();
        user.setUsername("username2");
        user.setEmailAddress("test@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        persistExpectingSuccess(user);
    }
}
