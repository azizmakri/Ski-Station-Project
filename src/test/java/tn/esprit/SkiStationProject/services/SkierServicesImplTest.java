package tn.esprit.SkiStationProject.services;

import org.junit.jupiter.api.*;
import org.mockito.*;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.entities.Piste;
import tn.esprit.SkiStationProject.entities.Skier;
import tn.esprit.SkiStationProject.entities.Subscription;
import tn.esprit.SkiStationProject.entities.enums.TypeSubscription;
import tn.esprit.SkiStationProject.repositories.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SkierServicesImplTest {

    @Mock private SkierRepository skierRepository;
    @Mock private PisteRepository pisteRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private RegistrationRepository registrationRepository;
    @Mock private SubscriptionRepository subscriptionRepository;

    @InjectMocks private SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMock(this);
    }

    @Test
    void retrieveAllSkiers() {
        // Given
        List<Skier> expectedSkiers = Collections.singletonList(new Skier());
        when(skierRepository.findAll()).thenReturn(expectedSkiers);

        // When
        List<Skier> actualSkiers = skierServices.retrieveAllSkiers();

        // Then
        assertSame(expectedSkiers, actualSkiers);
        verify(skierRepository).findAll();
    }

    @Test
    void assignSkierToSubscription() {
        // Given
        Long skierId = 1L;
        Long subscriptionId = 1L;
        Skier skier = new Skier();
        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(new Subscription()));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // When
        Skier resultSkier = skierServices.assignSkierToSubscription(skierId, subscriptionId);

        // Then
        assertNotNull(resultSkier.getSubscription());
        verify(skierRepository).save(skier);
    }

    @Test
    void addSkierAndAssignToCourse() {
        // Given
        Skier newSkier = mock(Skier.class);
        Long courseId = 1L;
        Course course = new Course();
        when(newSkier.getRegistrations()).thenReturn(Collections.emptySet());
        when(skierRepository.save(any(Skier.class))).thenReturn(newSkier);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // When
        Skier resultSkier = skierServices.addSkierAndAssignToCourse(newSkier, courseId);

        // Then
        assertSame(newSkier, resultSkier);
        verify(skierRepository).save(newSkier);
        verify(courseRepository).findById(courseId);
        // Additional verification can be added for registrationRepository if needed.
    }

    @Test
    void removeSkier() {
        // Given
        Long skierId = 1L;
        doNothing().when(skierRepository).deleteById(skierId);

        // When
        skierServices.removeSkier(skierId);

        // Then
        verify(skierRepository).deleteById(skierId);
    }

    @Test
    void retrieveSkier() {
        // Given
        Long skierId = 1L;
        Skier expectedSkier = new Skier();
        when(skierRepository.findById(skierId)).thenReturn(Optional.of(expectedSkier));

        // When
        Skier actualSkier = skierServices.retrieveSkier(skierId);

        // Then
        assertSame(expectedSkier, actualSkier);
        verify(skierRepository).findById(skierId);
    }

    @Test
    void assignSkierToPiste() {
        // Given
        Long skierId = 1L, pisteId = 1L;
        Skier skier = mock(Skier.class);
        Piste piste = new Piste();
        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // When
        Skier resultSkier = skierServices.assignSkierToPiste(skierId, pisteId);

        // Then
        verify(skierRepository).save(skier);
        // You should also verify that the piste was added to the skier's pistes, if there's a getter for pistes.
    }

    @Test
    void retrieveSkiersBySubscriptionType() {
        // Given
        TypeSubscription subscriptionType = TypeSubscription.ANNUAL;
        List<Skier> expectedSkiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findBySubscription_TypeSub(subscriptionType)).thenReturn(expectedSkiers);

        // When
        List<Skier> actualSkiers = skierServices.retrieveSkiersBySubscriptionType(subscriptionType);

        // Then
        assertEquals(expectedSkiers, actualSkiers);
        verify(skierRepository).findBySubscription_TypeSub(subscriptionType);
    }
}
