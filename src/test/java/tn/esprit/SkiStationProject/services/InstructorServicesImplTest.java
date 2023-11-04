package tn.esprit.SkiStationProject.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.entities.Instructor;
import tn.esprit.SkiStationProject.repositories.CourseRepository;
import tn.esprit.SkiStationProject.repositories.InstructorRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InstructorServicesImplTest {

    private InstructorServicesImpl instructorServicesImpl;
    private InstructorRepository instructorRepository;
    private CourseRepository courseRepository;
    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        instructorRepository = mock(InstructorRepository.class);
        courseRepository = mock(CourseRepository.class);
        instructorServicesImpl = new InstructorServicesImpl(instructorRepository, courseRepository);

        // Create a default instructor and course for use in the tests
        instructor = new Instructor();
        // ... set properties on instructor as needed for your domain logic

        course = new Course();
        // ... set properties on course as needed for your domain logic

        // Configure mocks
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
    }

    @AfterEach
    void tearDown() {
        // Perform any necessary cleanup after tests
    }

    @Test
    void addInstructor() {
        // Act
        Instructor savedInstructor = instructorServicesImpl.addInstructor(instructor);

        // Assert
        assertNotNull(savedInstructor, "Instructor should be saved and returned");
        verify(instructorRepository).save(instructor);
    }

    @Test
    void retrieveAllInstructors() {
        // Arrange
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(instructor));

        // Act
        List<Instructor> allInstructors = instructorServicesImpl.retrieveAllInstructors();

        // Assert
        assertFalse(allInstructors.isEmpty(), "List of instructors should not be empty");
        assertEquals(1, allInstructors.size(), "There should be one instructor in the list");
        verify(instructorRepository).findAll();
    }

    @Test
    void updateInstructor() {
        // Act
        Instructor updatedInstructor = instructorServicesImpl.updateInstructor(instructor);

        // Assert
        assertNotNull(updatedInstructor, "Instructor should be updated and returned");
        verify(instructorRepository).save(instructor);
    }

    @Test
    void retrieveInstructor() {
        // Arrange
        Long instructorId = 1L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        // Act
        Instructor foundInstructor = instructorServicesImpl.retrieveInstructor(instructorId);

        // Assert
        assertNotNull(foundInstructor, "Instructor should be retrieved");
        assertEquals(instructor, foundInstructor, "Retrieved instructor should match the expected one");
        verify(instructorRepository).findById(instructorId);
    }

    @Test
    void addInstructorAndAssignToCourse() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        Instructor savedInstructor = instructorServicesImpl.addInstructorAndAssignToCourse(instructor, courseId);

        // Assert
        assertNotNull(savedInstructor, "Instructor should be saved");
        assertTrue(savedInstructor.getCourses().contains(course), "Instructor's courses should contain the assigned course");
        verify(instructorRepository).save(instructor);
        verify(courseRepository).findById(courseId);
    }
}
