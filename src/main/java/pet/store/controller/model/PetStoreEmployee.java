package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Employee;

@Data
@NoArgsConstructor
public class PetStoreEmployee {
	private Long employeeId;
	private Long storeId;
	private String employeeFirstName;
	private String employeeLastName;
	private Long employeePhone;
	private String jobTitle;
	
	public PetStoreEmployee(Employee employee) {
		
	}
}
