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

    @Override
    public ReportingStructure create(String employeeId) {
        LOG.debug("Creating ReportingStructure for id [{}]", employeeId);

        Employee employee = employeeService.read(employeeId);

        //ensure there is a valid employee for the given ID
        if(employee == null){
            LOG.error("Error: Invalid employeeId");
            return null;
        }

        int reports = calculateReports(employee);

        ReportingStructure reportingStructure = new ReportingStructure(employee,reports);
        return reportingStructure;
    }

    private int calculateReports(Employee employee){
        //this assumes a valid reporting structure exists for the organization
        //an example of an invalid structure would be having two employees who report to each other

        int reports = 0;

        //only recurse if there are direct reports, otherwise return 0 as base case
        if(employee.getDirectReports() != null)
        {
            //add current direct report and calculate their direct reports
            for (Employee directReport : employee.getDirectReports()) {
                reports++;
                reports += calculateReports(directReport);
            }
        }

        return reports;
    }
}
