package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.repositoryDSL.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface  ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query("select p from Product p where p.manuCode =:pno")
    Product findProduct(Long pno);

    @Query("select count(p) from Product p where p.mater_imsi=1 and p.mater_check=0")
    Long newProductCount();
}