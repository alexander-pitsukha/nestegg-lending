package by.nestegg.lending.repositoty;

import by.nestegg.lending.entity.LendingRequest;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.entity.enums.TermType;
import by.nestegg.lending.repository.LendingRequestRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LendingRequestRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private LendingRequestRepository lendingRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        saveTestEntity("request/lending_request.json", LendingRequest.class);
    }

    @Test
    void testExistsByUserId() {
        boolean isExist = lendingRequestRepository.existsByUserId(1L);

        assertTrue(isExist);
    }

    @Test
    void testFindAllBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType() throws Exception {
        User user = saveTestEntity("user/user_phone_number.json", User.class);
        List<LendingRequest> lendingRequests = objectMapper.readValue(new ClassPathResource(
                "request/lending_requests.json").getInputStream(), new TypeReference<>() {
        });
        lendingRequests.forEach(lendingRequest -> saveTestEntity(lendingRequest, LendingRequest.class));

        List<LendingRequest> entities = lendingRequestRepository
                .findAllBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(BigDecimal.valueOf(1200),
                        (double) 5, 10, TermType.MONTH);

        assertNotNull(entities);
        assertEquals(lendingRequests.size(), entities.size());
    }

}
