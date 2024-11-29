package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationCreateUrl;
    private String compensationReadUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationCreateUrl = "http://localhost:" + port + "/compensation";
        compensationReadUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    /*
        Tests adding a compensation to the database then reading it back
     */
    @Test
    public void testCreateReadCompensation() {
        String id = "62c1084e-6e34-4630-93fd-9153afb65309";
        LocalDate testDate = LocalDate.of(2020, 2, 2);
        Compensation testCompensation = new Compensation(id, 90000, testDate);

        Compensation compensation = restTemplate.postForEntity(compensationCreateUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(compensation);
        assertEquals(testCompensation.getEffectiveDate(), compensation.getEffectiveDate());
        assertEquals(testCompensation.getSalary(), compensation.getSalary());
        assertEquals(testCompensation.getEmployeeId(), compensation.getEmployeeId());

        Compensation compensationRead = restTemplate.getForEntity(compensationReadUrl, Compensation.class, testCompensation.getEmployeeId()).getBody();

        assertNotNull(compensationRead);
        assertEquals(testCompensation.getEffectiveDate(), compensationRead.getEffectiveDate());
        assertEquals(testCompensation.getSalary(), compensationRead.getSalary());
        assertEquals(testCompensation.getEmployeeId(), compensationRead.getEmployeeId());
    }

    /*
        Tests reading an existing compensation from the database
    */
    @Test
    public void testReadDBCompensation() {
        String id = "c0c2293d-16bd-4603-8e08-638a9d18b22c";
        LocalDate testDate = LocalDate.of(2020, 7, 26);
        int testSalary = 60000;

        Compensation compensationRead = restTemplate.getForEntity(compensationReadUrl, Compensation.class, id).getBody();

        assertNotNull(compensationRead);
        assertEquals(testDate, compensationRead.getEffectiveDate());
        assertEquals(testSalary, compensationRead.getSalary());
        assertEquals(id, compensationRead.getEmployeeId());
    }

}
