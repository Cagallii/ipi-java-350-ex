package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    //Employé dateEmbauche avec date 2 ans avant aujourd'hui =>
    //2 années d'ancienneté

    @Test
    public void testAncienneteDateEmbaucheNmoins2(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(2));

        //When
        Integer nbAnnees = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnees).isEqualTo(2);

    }

    //Date dans le futur
    @Test
    public void testAncienneteDateEmbaucheNplus2(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));

        //When
        Integer nbAnnees = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnees).isEqualTo(0);

    }

    //Date d'aujourd'hui =>
    //ancienneté = 0
    @Test
    public void testAncienneteDateEmbaucheAujourdhui(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When
        Integer nbAnnees = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnees).isEqualTo(0);

    }

    //Date d'embauche indéfini =>
    //ancienneté = 0
    @Test
    public void testAncienneteDateEmbaucheNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer nbAnnees = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnees).isEqualTo(0);

    }

    //Matricule / performance / date d'embauche(années d'ancienneté)/temps partiel / prime
    @ParameterizedTest(name = "ok ca passe !")
    @CsvSource({
            "1, 'T12345', 0, 1.0, 1000.0",
            "1, 'T12345', 0, 0.5, 500.0",
            "1, 'M12345', 0, 1.0, 1700.0",
            "2, 'T12345', 0, 1.0, 2300.0"
    })
    public void testPrimeAnnuelle( Integer performance, String matricule, Integer nbAnneesAnciennete, Double tempsPartiel, Double prime){
        //Given
        Employe employe = new Employe();
        employe.setMatricule(matricule);
        employe.setPerformance(performance);
        employe.setDateEmbauche(LocalDate.now().minusYears(nbAnneesAnciennete));
        employe.setTempsPartiel(tempsPartiel);

        //When
        Double primeCalculee = employe.getPrimeAnnuelle();
        //Then
        Assertions.assertThat(primeCalculee).isEqualTo(prime);
    }
}
