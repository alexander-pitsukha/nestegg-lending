package by.nestegg.lending.repositoty;

import by.nestegg.lending.entity.BorrowingRequest;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.entity.enums.TermType;
import by.nestegg.lending.repository.BorrowingRequestRepository;
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
class BorrowingRequestRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private BorrowingRequestRepository borrowingRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        saveTestEntity("request/borrowing_request.json", BorrowingRequest.class);
    }

    @Test
    void testExistsByUserId() {
        boolean isExist = borrowingRequestRepository.existsByUserId(1L);

        assertTrue(isExist);
    }

    @Test
    void testFindBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType() throws Exception {
        User user = saveTestEntity("user/user_phone_number.json", User.class);
        List<BorrowingRequest> borrowingRequests = objectMapper.readValue(new ClassPathResource(
                "request/borrowing_requests.json").getInputStream(), new TypeReference<>() {
        });
        borrowingRequests.forEach(borrowingRequest -> saveTestEntity(borrowingRequest, BorrowingRequest.class));

        List<BorrowingRequest> entities = borrowingRequestRepository
                .findBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(
                        BigDecimal.valueOf(1200), (double) 5, 10, TermType.MONTH);

        assertNotNull(entities);
        assertEquals(borrowingRequests.size(), entities.size());
    }

}
