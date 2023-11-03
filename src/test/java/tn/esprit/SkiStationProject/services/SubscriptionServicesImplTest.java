package tn.esprit.SkiStationProject.services;

import org.junit.jupiter.api.*;
import org.mockito.*;
import tn.esprit.SkiStationProject.entities.Subscription;
import tn.esprit.SkiStationProject.entities.Skier;
import tn.esprit.SkiStationProject.entities.enums.TypeSubscription;
import tn.esprit.SkiStationProject.repositories.SkierRepository;
import tn.esprit.SkiStationProject.repositories.SubscriptionRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServicesImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMock(this);
    }


    @Test
    void updateSubscription() {
        // Given
        Subscription subscription = new Subscription();
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // When
        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);

        // Then
        assertSame(subscription, updatedSubscription);
        verify(subscriptionRepository).save(subscription);
    }

    @Test
    void retrieveSubscriptionById() {
        // Given
        Long numSubscription = 1L;
        Optional<Subscription> subscription = Optional.of(new Subscription());
        when(subscriptionRepository.findById(numSubscription)).thenReturn(subscription);

        // When
        Subscription foundSubscription = subscriptionServices.retrieveSubscriptionById(numSubscription);

        // Then
        assertNotNull(foundSubscription);
        verify(subscriptionRepository).findById(numSubscription);
    }

    @Test
    void getSubscriptionByType() {
        // Given
        TypeSubscription type = TypeSubscription.ANNUAL;
        Set<Subscription> subscriptions = new HashSet<>(Arrays.asList(new Subscription()));
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(subscriptions);

        // When
        Set<Subscription> foundSubscriptions = subscriptionServices.getSubscriptionByType(type);

        // Then
        assertSame(subscriptions, foundSubscriptions);
        verify(subscriptionRepository).findByTypeSubOrderByStartDateAsc(type);
    }

    @Test
    void retrieveSubscriptionsByDates() {
        // Given
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = Arrays.asList(new Subscription());
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        // When
        List<Subscription> foundSubscriptions = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        // Then
        assertEquals(subscriptions, foundSubscriptions);
        verify(subscriptionRepository).getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    

    @Test
    void showMonthlyRecurringRevenue() {
        // Given
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(1000f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(3000f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(12000f);

        // When
        subscriptionServices.showMonthlyRecurringRevenue();

        // Then
        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);

        // Since this method does not return any value and its purpose is to log information,
        // you might want to assert the behavior (e.g., that a logging interaction happened).
        // This typically involves using a logging framework's API to capture log messages
        // and asserting their content, which can get complex.

        // Alternatively, you could refactor `showMonthlyRecurringRevenue` to return the computed
        // revenue for easier testing. But for the current method, we'll assume that it's sufficient
        // to verify the interactions with the repository.

        // Note: Since the method `showMonthlyRecurringRevenue` doesn't return any value and only logs the output,
        // it is not possible to directly test its functionality with a return type test.
        // However, if you were to refactor your code to return the revenue instead of just logging it,
        // you could then write a test to check that the revenue is calculated correctly.
    }
}