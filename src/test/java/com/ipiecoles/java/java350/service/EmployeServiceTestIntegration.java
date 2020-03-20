package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class EmployeServiceTestIntegration {
    @Autowired
    EmployeRepository employeRepository;

    @Autowired
    EmployeService employeService;

    @BeforeEach
    @AfterAll
    public void setup(){
        employeRepository.deleteAll();
    }

    @Test
    public void testcalculPerformanceCommercial() throws EmployeException {
        //Give
        Employe employe2 = employeRepository.save(new Employe("Doe","Jane","C01234", LocalDate.now(),
                1500d, 6, 1.0));
        Employe employe3 = employeRepository.save(new Employe("Doe","Joe","C12345", LocalDate.now(),
                1500d, 4, 1.0));
        Employe employe4= employeRepository.save(new Employe("Doe","Jules","T01234", LocalDate.now(),
                1500d, 1, 1.0));
        String matriculeRecherche = "C12345";
        Long caTraite= 95000L;
        Long objectifCa=100000L;

        //When
        employeService.calculPerformanceCommercial(matriculeRecherche, caTraite,objectifCa);
        Employe employe = employeRepository.findByMatricule(matriculeRecherche);

        //Then
        Assertions.assertThat(employe.getNom()).isEqualTo("Doe");
        Assertions.assertThat(employe.getPrenom()).isEqualTo("Joe");
        Assertions.assertThat((employe.getSalaire())).isEqualTo(1500d);
        Assertions.assertThat(employe.getPerformance()).isEqualTo(4);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
    }
}
