package tn.esprit.SkiStationProject.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.repositories.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class CourseServicesImplTest {

    private CourseServicesImpl courseServicesImpl;
    private CourseRepository courseRepository;
    private Course course;

    @BeforeEach
    void setUp() {
        // Mock the repository
        courseRepository = Mockito.mock(CourseRepository.class);
        // Instantiate the service with the mocked repository
        courseServicesImpl = new CourseServicesImpl(courseRepository);

        // Create a course instance for use in tests
        course = new Course();
        // Preset data for course as necessary, e.g., course.setName("Ski Course");

        // Mocking repository responses for a standard course save operation
        Mockito.when(courseRepository.save(any(Course.class))).thenReturn(course);
        // Other properties and behaviors can be set up as needed for individual tests
    }

    @AfterEach
    void tearDown() {
        // Any tear down after tests if necessary
    }

    @Test
    void retrieveAllCourses() {
        // Arrange
        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(course);
        Mockito.when(courseRepository.findAll()).thenReturn(expectedCourses);

        // Act
        List<Course> retrievedCourses = courseServicesImpl.retrieveAllCourses();

        // Assert
        assertNotNull(retrievedCourses, "Courses should not be null");
        assertEquals(expectedCourses.size(), retrievedCourses.size(), "Course sizes should match");
        verify(courseRepository).findAll();
    }

    @Test
    void addCourse() {
        // Act
        Course savedCourse = courseServicesImpl.addCourse(course);

        // Assert
        assertNotNull(savedCourse, "Saved course should not be null");
        verify(courseRepository).save(course);
    }

    @Test
    void updateCourse() {
        // Arrange
        // Assume that the course has been modified and needs to be updated
        Mockito.when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course updatedCourse = courseServicesImpl.updateCourse(course);

        // Assert
        assertNotNull(updatedCourse, "Updated course should not be null");
        verify(courseRepository).save(course);
    }

    @Test
    void retrieveCourse() {
        // Arrange
        Long courseId = 1L; // Replace with actual ID used in your test
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        Course foundCourse = courseServicesImpl.retrieveCourse(courseId);

        // Assert
        assertNotNull(foundCourse, "The retrieved course should not be null");
        assertEquals(course, foundCourse, "The retrieved course should be the one saved");
        verify(courseRepository).findById(courseId);
    }
}
