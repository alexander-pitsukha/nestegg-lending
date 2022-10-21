package by.nestegg.lending.repositoty;

import by.nestegg.lending.BasicTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.io.IOException;

public abstract class AbstractRepositoryTests extends BasicTests {

    @Autowired
    private TestEntityManager entityManager;

    protected <E> E saveTestEntity(String fileSource, Class<E> valueType) throws IOException {
        E entity = getObjectFromJson(fileSource, valueType);
        saveTestEntity(entity, valueType);
        return entity;
    }

    protected <E> E saveTestEntity(E entity, Class<E> valueType) {
        try {
            return entityManager.persist(entity);
        } finally {
            entityManager.flush();
        }
    }

}
