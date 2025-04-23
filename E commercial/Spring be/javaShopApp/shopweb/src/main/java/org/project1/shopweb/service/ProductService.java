package org.project1.shopweb.service;
import lombok.RequiredArgsConstructor;
import org.project1.shopweb.component.LocalizationUtils;
import org.project1.shopweb.dto.ProductDTO;
import org.project1.shopweb.dto.ProductImageDTO;
import org.project1.shopweb.exception.DemoI18nException;
import org.project1.shopweb.exception.ErrorCode;
import org.project1.shopweb.model.Category;
import org.project1.shopweb.model.Product;
import org.project1.shopweb.model.ProductImage;
import org.project1.shopweb.repository.CategoryRepository;
import org.project1.shopweb.repository.ProductImageRepository;
import org.project1.shopweb.repository.ProductRepository;
import org.project1.shopweb.exception.NotFoundException;
import org.project1.shopweb.respon.ProductRespon;
import org.project1.shopweb.utils.MessageKeys;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final LocalizationUtils localizationUtils;

    @Override
    public void deleteProduct(long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public Product      getProductById(long id) {
        return productRepository.findById(id).
                orElseThrow(() -> new NotFoundException("this product does not exist"));

    }

    @Override
    public Product creatProduct(ProductDTO productDTO) {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).
                orElseThrow(() -> new NotFoundException("This category does not exist"));

        Product newProduct =  Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) {
        Product fromdb = getProductById(id);
        if (fromdb != null) {
            //copy các thuộc tính từ DTO -> Product
            //Có thể sử dụng ModelMapper
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Cannot find category with id: " + productDTO.getCategoryId()));
            fromdb.setName(productDTO.getName());
            fromdb.setCategory(existingCategory);
            fromdb.setPrice(productDTO.getPrice());
            fromdb.setDescription(productDTO.getDescription());
            fromdb.setThumbnail(productDTO.getThumbnail());
        }
        return productRepository.save(fromdb);
    }

    @Override
    public Page<ProductRespon> findAll(Long categoryId,String keyword,Pageable pageable) {

        return productRepository.searchProducts(categoryId,keyword,pageable).map(ProductRespon::changeType);

    }




    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(ProductImageDTO productImageDTO,int inputimg) throws Exception{
        ProductImage newProductImage = ProductImage.builder()
                .product(productRepository.findById(productImageDTO.
                        getProductId()).orElseThrow(() -> new NotFoundException("not exist this production")))
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        int size = productImageRepository.findByProductId(productImageDTO.getProductId()).size();
        if(size + inputimg > 5){
            throw new DemoI18nException(ErrorCode.MAX_IMAGE_LIMIT);
        }

        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<Product> findProductsbyIds(List<Long> listId) {
        return productRepository.findProductsByIds(listId);
    }
}
