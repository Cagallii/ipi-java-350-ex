package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

   @Test
    public void testEmbaucheEmployeCommercialPleinTempsBTS() throws EmployeException {
        //Given
        String nom ="Doe";
        String prenom ="John";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        // Cas nominal donc pas d'exception
        //findLastMatricule => 00345 || null
       Mockito.when(employeRepository.findLastMatricule()).thenReturn("00345");
        //findByMatricule=> null
       //(matricule =C00346 car c'est le comportement de l'algo attendu)
        Mockito.when(employeRepository.findByMatricule("C00346")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        //BDD si l'employe est bien créé (nom,prenom,matricule,salaire,
        //date d'embauche, salaire, performance, temps partiel

       //Initialisation des capteurs d'arguments
       ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass((Employe.class));
       Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
       Assertions.assertThat(employeCaptor.getValue().getNom()).isEqualTo("Doe");
       Assertions.assertThat(employeCaptor.getValue().getPrenom()).isEqualTo("John");
       Assertions.assertThat(employeCaptor.getValue().getTempsPartiel()).isEqualTo(1.0);
       Assertions.assertThat(employeCaptor.getValue().getMatricule()).isEqualTo("C00346");
       //Attention aux heures et minutes, ce sera jamais égal, on peut soit mocké l'appel à LocalDate.now()(pas possible avec Mokito)
       //soit on récupère tout et on compare que jour/mois/année ( voir la correction avec DateTimeFormatter.ofPattern
       Assertions.assertThat(employeCaptor.getValue().getDateEmbauche()).isEqualTo(LocalDate.now());
       //1521.22*1.2*1.0
       Assertions.assertThat(employeCaptor.getValue().getSalaire()).isEqualTo(1825.46);
       Assertions.assertThat(employeCaptor.getValue().getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);

       //possiblee de créer deux employés employeVerif = new Employe (nom,prenom,...Etc)
       //Assertions.assertThat(employe).isEqualTo(employeVerif);
    }
    @Test
    public void testEmbaucheEmployeCommercialSansMatricule() throws EmployeException {
        //Given
        String nom ="Doe";
        String prenom ="John";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        // Cas nominal donc pas d'exception
        //findLastMatricule => 00345 || null
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        //findByMatricule=> null
        //
        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        //BDD si l'employe est bien créé (nom,prenom,matricule,salaire,
        //date d'embauche, salaire, performance, temps partiel

        //Initialisation des capteurs d'arguments
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass((Employe.class));
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Assertions.assertThat(employeCaptor.getValue().getNom()).isEqualTo("Doe");
        Assertions.assertThat(employeCaptor.getValue().getPrenom()).isEqualTo("John");
        Assertions.assertThat(employeCaptor.getValue().getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employeCaptor.getValue().getMatricule()).isEqualTo("C00001");
        //Attention aux heures et minutes, ce sera jamais égal, on peut soit mocké l'appel à LocalDate.now()(pas possible avec Mokito)
        //soit on récupère tout et on compare que jour/mois/année ( voir la correction avec DateTimeFormatter.ofPattern
        Assertions.assertThat(employeCaptor.getValue().getDateEmbauche()).isEqualTo(LocalDate.now());
        //1521.22*1.2*1.0
        Assertions.assertThat(employeCaptor.getValue().getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employeCaptor.getValue().getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);

        //possiblee de créer deux employés employeVerif = new Employe (nom,prenom,...Etc)
        //Assertions.assertThat(employe).isEqualTo(employeVerif);
    }
    @Test
    public void testEmbaucheEmployeCommercialLimiteMatricule() throws EmployeException {
        //Given
        String nom ="Doe";
        String prenom ="John";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");

        //When/Then AssertJ
        Assertions.assertThatThrownBy(() ->{employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);})
                .isInstanceOf(EmployeException.class).hasMessage("Limite des 100000 matricules atteinte !");
        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("Aurait du planter !");
        } catch(Exception e){
            //Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }
    }
}
