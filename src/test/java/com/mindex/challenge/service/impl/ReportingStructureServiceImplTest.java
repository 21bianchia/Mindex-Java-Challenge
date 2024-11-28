package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureUrl;

    @Autowired
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    /*
        Testing an employee with no direct reports, George Harrison.
        The org chart for all tests is below:

                    John Lennon
                /               \
         Paul McCartney         Ringo Starr
                               /        \
                          Pete Best     George Harrison
     */
    @Test
    public void testCreateZero() {
        Employee testEmployeeZero = employeeRepository.findByEmployeeId("c0c2293d-16bd-4603-8e08-638a9d18b22c");
        ReportingStructure testReportingStructureZero = new ReportingStructure(testEmployeeZero, 0);

        ReportingStructure reportingStructureZero = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testEmployeeZero.getEmployeeId()).getBody();

        assertNotNull(reportingStructureZero.getEmployee());
        assertEquals(testReportingStructureZero.getNumberOfReports(), reportingStructureZero.getNumberOfReports());
    }

    /*
        Testing an employee with two direct reports, Ringo Starr. The org chart is below:
    */
    @Test
    public void testCreateTwo() {
        Employee testEmployeeTwo = employeeRepository.findByEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f");
        ReportingStructure testReportingStructureTwo = new ReportingStructure(testEmployeeTwo, 2);

        ReportingStructure reportingStructureTwo = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testEmployeeTwo.getEmployeeId()).getBody();

        assertNotNull(reportingStructureTwo.getEmployee());
        assertEquals(testReportingStructureTwo.getNumberOfReports(), reportingStructureTwo.getNumberOfReports());
    }

    /*
        Testing an employee (John Lennon) with four direct reports, two of which are under another employee.
     */
    @Test
    public void testCreateFour() {
        Employee testEmployeeFour = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        ReportingStructure testReportingStructureFour = new ReportingStructure(testEmployeeFour, 4);

        ReportingStructure reportingStructureFour = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testEmployeeFour.getEmployeeId()).getBody();

        assertNotNull(reportingStructureFour.getEmployee());
        assertEquals(testReportingStructureFour.getNumberOfReports(), reportingStructureFour.getNumberOfReports());
    }

}
