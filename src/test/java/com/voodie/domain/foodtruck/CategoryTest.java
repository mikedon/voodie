package com.voodie.domain.foodtruck;

import com.voodie.domain.AbstractJpaTest;
import org.junit.Test;

/**
 * Voodie
 * User: MikeD
 */
public class CategoryTest extends AbstractJpaTest {

    @Test
    public void testPersistWithoutName(){
        Category category = new Category();
        persistExpectingError(category);
    }

    @Test
    public void testPersistWithDuplicate(){
        Category category1 = new Category();
        category1.setName("category1");
        persistExpectingSuccess(category1);
        Category category2 = new Category();
        category2.setName("category1");
        persistExpectingError(category2);
    }

    @Test
    public void testValidPersist(){
        Category category = new Category();
        category.setName("testCategory");
        persistExpectingSuccess(category);
    }

}
