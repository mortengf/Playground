package mockito_playground;

public interface ProductDAO {
  public void create(Product product) throws DuplicateProductException;
  public Product find(int id);
  public Product update(int id);
  public Product update(String name);
  public void delete(int id);
  public void delete(String name);
}
