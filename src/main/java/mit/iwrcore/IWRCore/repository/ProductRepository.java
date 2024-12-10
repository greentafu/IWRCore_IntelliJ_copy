package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Partner;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.repositoryDSL.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query("select p from Product p where p.manuCode =:pno")
    Product findProduct(Long pno);

    @Query("select count(p) from Product p where p.mater_imsi=1 and p.mater_check=0")
    Long newProductCount();

    @Query("select distinct p from PreRequest r " +
            "left join Product p on (p.manuCode=r.proPlan.product.manuCode) " +
            "where r.allCheck=0 order by p.manuCode desc")
    List<Product> getRequestProduct();

    @Query("select p from Product p where p.proS.proScode=:code")
    List<Product> getProductByCategoryS(Long code);

    @Query("select p from Product p where p.proS.proM.proMcode=:code")
    List<Product> getProductByCategoryM(Long code);

    @Query("select p from Product p where p.proS.proM.proL.proLcode=:code")
    List<Product> getProductByCategoryL(Long code);


}