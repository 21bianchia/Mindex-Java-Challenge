package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure create(String employeeId) {
        LOG.debug("Creating ReportingStructure for id [{}]", employeeId);

        Employee employee = employeeService.read(employeeId);

        //ensure there is a valid employee for the given ID
        if(employee == null){
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        int reports = calculateReports(employee);

        ReportingStructure reportingStructure = new ReportingStructure(employee,reports);
        return reportingStructure;
    }

    private int calculateReports(Employee employee){
        /*
        this function assumes a valid reporting structure exists for the organization
        an example of an invalid structure would be having two employees who report to each other,
        which would result in an infinite loop
        */

        int reports = 0;

        /*
        gets the employee info from the db to ensure all fields are filled correctly
        fixes an issue where the list of direct reports gives the employee ID,
        but not the rest of the data
        */

        employee = employeeService.read(employee.getEmployeeId());

        //only recurse if there are direct reports, otherwise return 0 as base case
        if(employee.getDirectReports() != null)
        {
            //add current direct report and calculate future direct reports
            for (Employee directReport : employee.getDirectReports()) {
                reports++;
                reports += calculateReports(directReport);
            }
        }

        return reports;
    }
}
