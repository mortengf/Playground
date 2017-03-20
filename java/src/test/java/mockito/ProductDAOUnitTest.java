package mockito_playground;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;

public class ProductDAOUnitTest {
	
	@Test
	public void testCreateSuccess() {
		ProductDAO productDAO = mock(ProductDAO.class);
		
		Product deathRay = new Product();
		deathRay.setId(1);
		deathRay.setName("The Death Ray");
		deathRay.setDescription("It only has evil implications");

		try {
			productDAO.create(deathRay);
			verify(productDAO).create(deathRay);
			when(productDAO.find(1)).thenReturn(deathRay);
			Assert.assertEquals(1, deathRay.getId());
		} catch (DuplicateProductException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateFailureDuplicateProduct() {
		ProductDAO productDAO = mock(ProductDAO.class);
		
		Product deathRay = new Product();
		deathRay.setId(1);
		deathRay.setName("The Death Ray");
		deathRay.setDescription("It only has evil implications");
		
		try {
			// Create first product - no problem
			productDAO.create(deathRay);
			verify(productDAO).create(deathRay);

			// Attempt to create second, identical product => expect a "duplicate product" error!
			doThrow(new DuplicateProductException()).when(productDAO).create(deathRay);
			productDAO.create(deathRay);
			Assert.fail("Expected DuplicateProductException was not thrown!");
		} catch (DuplicateProductException e) {
			// Create a third but DIFFERENT product? No problem!
			Product improvedDeathRay = new Product();
			deathRay.setId(2);
			deathRay.setName("The Improved Death Ray");
			deathRay.setDescription("Still only has evil implications, though");
			
			try {
				productDAO.create(improvedDeathRay);
				verify(productDAO).create(improvedDeathRay);
			} catch (DuplicateProductException e1) {
				e1.printStackTrace();
			}
		}

	}
	
} 
