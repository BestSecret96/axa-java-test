package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.CustomRuntimeException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jp.co.axa.apidemo.exception.EmployeeException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service(value = "EmployeeServiceImpl")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    /**
     * @return - a list of All employees present in the System.
     */
    @Cacheable(value = "employees")
    public List<Employee> listAllEmployees() {
        List<Employee> employees;
        try {
            employees = employeeRepository.findAll();
            if (employees.isEmpty()) {
                logger.warn("No records found for Employees.");
            }
        } catch (Throwable t) {
            logger.error("Error occurred while fetching Employee records.", t);
            throw EmployeeException.prepareExceptionDetails("Get all operation failed!",
                    HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employees;
    }

    /**
     * @param employeeId - for retrieving the record of an employee for given employeeId.
     * @return - an employee record using a ResponseEntity object.
     */
    @Cacheable(cacheNames = "employees", key = "#employeeId")
    public Employee getEmployee(Long employeeId) {
        try {
            return findFirst(employeeId, false);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw entityNotFoundException;
        } catch (Throwable t) {
            logger.error("Unable to fetch record for Employee [{}]", employeeId, t);
            throw EmployeeException.prepareExceptionDetails("Get operation failed!",
                    HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
    }

    /**
     * @param employee - for saving the record of an Employee in the System.
     * @return - Only the HttpStatus as CREATED using ResponseEntity object.
     */
    public Employee saveEmployee(Employee employee) {
        try {
            if (employee != null && findFirst(employee.getId(), true) != null) {
                logger.error("Employee record already exists for id [{}]", employee.getId());
                throw EmployeeException.prepareExceptionDetails("Record already exists in the System" +
                        " for given Id [" + employee.getId() + "]", HttpStatus.BAD_REQUEST);
            }
            return  employee = employeeRepository.save(employee);
        } catch (CustomRuntimeException customRuntimeException) {
            throw customRuntimeException;
        } catch (Throwable t) {
            logger.error("Unable to add a new record for Employee [{}]", employee);
            t.printStackTrace();
            throw EmployeeException.prepareExceptionDetails("Save operation failed!",
                    HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
    }

    /**
     * @param employeeId - for updating a particular employee record in the System.
     * @param employee   - for updating the record of an Employee in the System for a given EmployeeId.
     * @return - Only the HttpStatus as NO_CONTENT using ResponseEntity object.
     */
    @CachePut(cacheNames = "employees", key = "#employeeId")
    public Employee updateEmployee(Long employeeId, Employee employee) {
        try {
            if (!Objects.equals(employeeId, employee.getId())) {
                throw EmployeeException.prepareExceptionDetails("Invalid Payload provided," +
                        " please check if EmployeeId is correct or not.", HttpStatus.BAD_REQUEST);
            }
            if (findFirst(employeeId, false) != null)
                employeeRepository.save(employee);
        } catch (EntityNotFoundException | CustomRuntimeException apiException) {
            throw apiException;
        } catch (Throwable t) {
            logger.error("Unable to update the record for Employee [{}] with employee details [{}]",
                    employee.getId(), employee);
            t.printStackTrace();
            throw EmployeeException.prepareExceptionDetails("Update operation failed!",
                    HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employee;
    }

    /**
     * @param employeeId - Delete the record of an employee from the System given an employeeId
     * @return only the HttpStatus as OK using the ResponseEntity object.
     */
    @CacheEvict(cacheNames = "employees", key = "#employeeId", allEntries = false)
    public Boolean deleteEmployee(Long employeeId) {
        try {
            Employee employeeOptional = findFirst(employeeId, false);
            if (employeeOptional != null)
                employeeRepository.delete(employeeOptional);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw entityNotFoundException;
        } catch (Throwable t) {
            logger.error("Unable to delete the record for Employee [{}]", employeeId);
            t.printStackTrace();
            throw EmployeeException.prepareExceptionDetails("Delete operation failed!",
                    HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * @param employeeId      - for checking if an employee record exists in the system for given employeeId.
     * @param isSaveOperation - decider flag to decide if to throw an EntityNotFoundException
     *                       in case of save/update operation.
     * @return
     */
    private Employee findFirst(Long employeeId, boolean isSaveOperation) {
        Optional<Employee> optionalOfEmployee = employeeRepository.findById(employeeId);
        if (!isSaveOperation) {
            return optionalOfEmployee.orElseThrow(() ->
                    new EntityNotFoundException("Given employee Id [" + employeeId + "] is invalid" +
                            " or record does not exist."));
        } else {
            return optionalOfEmployee.orElse(null);
        }
    }
}