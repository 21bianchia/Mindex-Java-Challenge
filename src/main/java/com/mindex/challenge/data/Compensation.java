package com.mindex.challenge.data;

import java.time.LocalDate;

public class Compensation {
    private String employeeId;
    private int salary;
    private LocalDate effectiveDate;

    //Note on parameters:
    // using string for id instead of storing employee object,
    //    since we don't we to duplicate data in the databases
    // using int for salary to represent annual salary in $
    // LocalDate will show employee start date in yyyy-mm-dd format

    public Compensation() {
    }

    public Compensation(String employeeId, int salary, LocalDate effectiveDate) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
