package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
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
public class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @BeforeEach
    @AfterAll
    public void setup(){
        employeRepository.deleteAll();
    }

    @Test
    public void testFindLastMatricule0Employe(){
        //Give
        //When
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isNull();
    }
    @Test
    public void testFindLastMatricule2Employes(){
        //Give
        Employe employe1 = employeRepository.save(new Employe("Doe","John","M12345", LocalDate.now(),
                1500d, 1, 1.0));
        Employe employe2 = employeRepository.save(new Employe("Doe","Jane","T01234", LocalDate.now(),
                1500d, 1, 1.0));
        //When
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    public void testavgPerformanceWhereMatriculeStartsWith(){
        //Give
        Employe employe1 = employeRepository.save(new Employe("Doe","John","C12345", LocalDate.now(),
                1500d, 2, 1.0));
        Employe employe2 = employeRepository.save(new Employe("Doe","Jane","C01234", LocalDate.now(),
                1500d, 6, 1.0));
        Employe employe3 = employeRepository.save(new Employe("Doe","Joe","C12345", LocalDate.now(),
                1500d, 4, 1.0));
        Employe employe4= employeRepository.save(new Employe("Doe","Jules","T01234", LocalDate.now(),
                1500d, 1, 1.0));

        //When
        Double resultatMoyPerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");
        //Then
        Assertions.assertThat(resultatMoyPerf).isEqualTo(4);
    }
}
