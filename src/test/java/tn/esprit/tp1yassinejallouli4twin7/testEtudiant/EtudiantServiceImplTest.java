package tn.esprit.tp1yassinejallouli4twin7.testEtudiant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.esprit.tp1yassinejallouli4twin7.entities.Etudiant;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IEtudiantRepo;
import tn.esprit.tp1yassinejallouli4twin7.services.IMPL.EtudiantServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EtudiantServiceImplTest {

    @Mock
    private IEtudiantRepo etudiantRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Test
    public void testAjouterEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");
        etudiant.setPrenomet("Doe");
        etudiant.setCin(123456789);
        etudiant.setEcole("ABC School");
        etudiant.setEmail("john.doe@example.com");
        etudiant.setPassoword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Act
        Etudiant result = etudiantService.AjouterEtudiant(etudiant);

        // Assert
        assertEquals("user", result.getRole());
        assertEquals(1, result.getEtat());
        assertEquals("encodedPassword", result.getPassoword());
        verify(passwordEncoder).encode("password");
        verify(etudiantRepository, times(2)).save(any(Etudiant.class)); // Ensure save is called twice
    }


    @Test
    public void test_getAllEtudiant_returnsListOfEtudiants() {
        // Arrange
        List<Etudiant> expectedEtudiants = new ArrayList<>();
        expectedEtudiants.add(new Etudiant());
        expectedEtudiants.add(new Etudiant());
        expectedEtudiants.add(new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(expectedEtudiants);

        // Act
        List<Etudiant> actualEtudiants = etudiantService.GetAllEtudiant();

        // Assert
        assertEquals(expectedEtudiants, actualEtudiants);
    }


    @Test
    public void test_returns_etudiant_if_exists() {
        // Arrange
        long idEtudiant = 1;
        Etudiant expectedEtudiant = new Etudiant();
        expectedEtudiant.setIdEtudiant(idEtudiant);
        Mockito.when(etudiantRepository.findById(idEtudiant)).thenReturn(Optional.of(expectedEtudiant));

        // Act
        Etudiant actualEtudiant = etudiantService.GetEtudiant(idEtudiant);

        // Assert
        assertEquals(expectedEtudiant, actualEtudiant);
    }

    @Test
    public void test_valid_id() {
        // Arrange
        long validId = 1;
        Etudiant expectedEtudiant = new Etudiant();
        when(etudiantRepository.findById(validId)).thenReturn(Optional.of(expectedEtudiant));

        // Act
        Etudiant actualEtudiant = etudiantService.getEtudiant(validId);

        // Assert
        assertEquals(expectedEtudiant, actualEtudiant);
    }



    @Test
    public void test_valid_etudiant_id() {
        // Arrange
        long id = 1;
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(id);
        etudiant.setEtat(5);

        when(etudiantRepository.findById(id)).thenReturn(Optional.of(etudiant));
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        // Act
        Etudiant result = etudiantService.unblock(id);

        // Assert
        assertEquals(0, result.getEtat());
        verify(etudiantRepository, times(1)).findById(id);
        verify(etudiantRepository, times(1)).save(etudiant);
    }
}