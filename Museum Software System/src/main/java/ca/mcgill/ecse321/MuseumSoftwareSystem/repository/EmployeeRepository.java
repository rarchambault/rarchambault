package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, String> {
	public Employee findEmployeeByEmailAddress(String emailAddress);
}