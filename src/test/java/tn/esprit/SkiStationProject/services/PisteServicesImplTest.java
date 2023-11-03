package tn.esprit.SkiStationProject.services;

import org.junit.jupiter.api.*;
import org.mockito.*;
import tn.esprit.SkiStationProject.entities.Piste;
import tn.esprit.SkiStationProject.repositories.PisteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock
    private PisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMock(this);
    }

    @Test
    void retrieveAllPistes() {
        // Given
        List<Piste> expectedPistes = Arrays.asList(new Piste(), new Piste());
        when(pisteRepository.findAll()).thenReturn(expectedPistes);

        // When
        List<Piste> actualPistes = pisteServices.retrieveAllPistes();

        // Then
        assertSame(expectedPistes, actualPistes);
        verify(pisteRepository).findAll();
    }

    @Test
    void addPiste() {
        // Given
        Piste newPiste = new Piste();
        when(pisteRepository.save(any(Piste.class))).thenReturn(newPiste);

        // When
        Piste savedPiste = pisteServices.addPiste(newPiste);

        // Then
        assertSame(newPiste, savedPiste);
        verify(pisteRepository).save(newPiste);
    }

    @Test
    void removePiste() {
        // Given
        Long pisteId = 1L;
        doNothing().when(pisteRepository).deleteById(pisteId);

        // When
        pisteServices.removePiste(pisteId);

        // Then
        verify(pisteRepository).deleteById(pisteId);
    }

    @Test
    void retrievePiste() {
        // Given
        Long pisteId = 1L;
        Piste expectedPiste = new Piste();
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(expectedPiste));

        // When
        Piste actualPiste = pisteServices.retrievePiste(pisteId);

        // Then
        assertSame(expectedPiste, actualPiste);
        verify(pisteRepository).findById(pisteId);
    }

    @Test
    void retrievePiste_NotFound() {
        // Given
        Long pisteId = 1L;
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pisteServices.retrievePiste(pisteId);
        });

        // Then
        assertTrue(exception.getMessage().contains("no piste found with this id " + pisteId));
        verify(pisteRepository).findById(pisteId);
    }
}
