package pet.store.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
		
		copyPetStoreFields(petStore, petStoreData);
		
		PetStore dbPetStore = petStoreDao.save(petStore);
		
		return new PetStoreData(dbPetStore);
	}
	
	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setStoreName(petStoreData.getStoreName());
		petStore.setStoreAddress(petStoreData.getStoreAddress());
		petStore.setStoreCity(petStoreData.getStoreCity());
		petStore.setStoreState(petStoreData.getStoreState());
		petStore.setStoreZip(petStoreData.getStoreZip());
		petStore.setStorePhone(petStoreData.getStorePhone());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {	
		if(Objects.isNull(petStoreId)) {
			return new PetStore();
		}
		else {
			return findPetStoreById(petStoreId);
		}
	}
	
	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException(
						"PetStore with ID=" + petStoreId + " was not found."));
	}
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {		
		PetStore petStore = findPetStoreById(petStoreId);
		
		Employee employee = findOrCreateEmployee(petStoreId, petStoreEmployee.getEmployeeId());
		
		copyEmployeeFields(employee, petStoreEmployee);
		
		employee.setPetStore(petStore);
		
		petStore.getEmployees().add(employee);
		
		Employee dbEmployee = employeeDao.save(employee);
		
		return new PetStoreEmployee(dbEmployee);
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setJobTitle(petStoreEmployee.getJobTitle());
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		Employee employee;
		
		if (Objects.isNull(employeeId)) {
			employee = new Employee();
		} else {
			employee = findOrCreateEmployeeById(petStoreId, employeeId);
		}
		
		return employee;
	}

	private Employee findOrCreateEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(
				() -> new NoSuchElementException("Employee with ID=" + employeeId + " was not found."));
		
		if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
			throw new IllegalArgumentException("Employee with ID=" + employeeId + " does not belong to pet store with ID=" + petStoreId);
		}
		
		return employee;
	}

	@Autowired
	private CustomerDao customerDao;
	
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		
		Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomer.getCustomerId());
		
		copyCustomerFields(customer, petStoreCustomer);
		
		customer.setPetStore(petStore);
		
		petStore.getCustomers().add(customer);
		
		Customer dbCustomer = customerDao.save(customer);
		
		return new PetStoreCustomer(dbCustomer);
	}
	
	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}
	
	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		Customer customer;
		
		if (Objects.isNull(customerId)) {
			customer = new Customer();
		} else {
			customer = findOrCreateCustomerById(petStoreId, customerId);
		}
		
		return customer;
	}
	
	private Customer findOrCreateCustomerById(Long petStoreId, Long customerId) {
		Customer customer= customerDao.findById(customerId).orElseThrow(
				() -> new NoSuchElementException("Employee with ID=" + customerId + " was not found."));
		
		if (!customer.getPetStore().getPetStoreId().equals(petStoreId)) {
			throw new IllegalArgumentException("Employee with ID=" + customerId + " does not belong to pet store with ID=" + petStoreId);
		}
		
		return customer;
	}

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		return petStoreDao.findAll().stream().map(PetStoreData::new).toList();
	}

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		return new PetStoreData(petStore);
	}

	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}
	
	
}
