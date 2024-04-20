package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreData {
	private Long petStoreId;
	private String storeName;
	private String storeAddress;
	private String storeCity;
	private String storeState;
	private String storeZip;
	private String storePhone;
	private Set<PetStoreCustomer> customers = new HashSet<>();
	private Set<PetStoreEmployee> employees = new HashSet<>();

	public PetStoreData(PetStore petStore) {
		petStoreId = petStore.getPetStoreId();
		storeName = petStore.getStoreName();
		storeAddress = petStore.getStoreAddress();
		storeCity = petStore.getStoreCity();
		storeState = petStore.getStoreState();
		storeZip = petStore.getStoreZip();
		storePhone = petStore.getStorePhone();

		for (Customer customer : petStore.getCustomers()) {
			customers.add(new PetStoreCustomer(customer));
		}
		
		for (Employee employee : petStore.getEmployees()) {
			employees.add(new PetStoreEmployee(employee));
		}
	}
}
