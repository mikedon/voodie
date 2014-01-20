package com.voodie.domain.election;

import com.voodie.domain.AbstractJpaTest;
import org.junit.Test;

/**
 * Voodie
 * User: MikeD
 */
public class DistrictTest extends AbstractJpaTest{

    @Test
    public void testValidPersist(){
        District district = new District();
        district.setName("validDistrict");
        persistExpectingSuccess(district);
    }

    @Test
    public void testPersistWithDuplicate(){
        District district1 = new District();
        district1.setName("district1");
        persistExpectingSuccess(district1);

        District district2 = new District();
        district2.setName("district1");
        persistExpectingError(district2);
    }

    @Test
    public void testPersistWithoutName(){
        District district = new District();
        persistExpectingError(district);
    }

}
