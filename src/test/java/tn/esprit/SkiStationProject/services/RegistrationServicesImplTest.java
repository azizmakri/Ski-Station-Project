package tn.esprit.SkiStationProject.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.entities.Registration;
import tn.esprit.SkiStationProject.entities.Skier;
import tn.esprit.SkiStationProject.entities.enums.TypeCourse;
import tn.esprit.SkiStationProject.repositories.CourseRepository;
import tn.esprit.SkiStationProject.repositories.InstructorRepository;
import tn.esprit.SkiStationProject.repositories.RegistrationRepository;
import tn.esprit.SkiStationProject.repositories.SkierRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistrationServicesImplTest {

    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private SkierRepository skierRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMock(this);
    }



    @Test
    void assignRegistrationToCourse() {
        // Given
        Long registrationId = 1L;
        Long courseId = 1L;
        Registration registration = new Registration();
        Course course = new Course();
        when(registrationRepository.findById(registrationId)).thenReturn(Optional.of(registration));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Registration updatedRegistration = registrationServices.assignRegistrationToCourse(registrationId, courseId);

        // Then
        assertNotNull(updatedRegistration.getCourse());
        assertEquals(course, updatedRegistration.getCourse());
        verify(registrationRepository).save(registration);
        verify(courseRepository).findById(courseId);
    }

    @Test
    void addRegistrationAndAssignToSkierAndCourse() {
        // Given
        Registration registration = new Registration();
        registration.setNumWeek(1);
        Long skierId = 1L;
        Long courseId = 1L;
        Skier skier = mock(Skier.class);
        Course course = new Course();
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Set up specific conditions required for this method to work
        when(skier.getDateOfBirth()).thenReturn(LocalDate.now().minusYears(10)); // Child age for testing
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(0L);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, skierId, courseId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getSkier());
        assertNotNull(result.getCourse());
        verify(registrationRepository).save(registration);
        verify(skierRepository).findById(skierId);
        verify(courseRepository).findById(courseId);
    }


}
