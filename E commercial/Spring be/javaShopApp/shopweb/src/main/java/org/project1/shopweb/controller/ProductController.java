package org.project1.shopweb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project1.shopweb.component.LocalizationUtils;
import org.project1.shopweb.dto.ProductDTO;
import org.project1.shopweb.dto.ProductImageDTO;
import org.project1.shopweb.model.Product;
import org.project1.shopweb.model.ProductCacheWrapper;
import org.project1.shopweb.model.ProductImage;
import org.project1.shopweb.repository.ProductRepository;
import org.project1.shopweb.service.ProductRedisService;
import org.project1.shopweb.service.ProductService;
import org.project1.shopweb.respon.ProductListRespon;
import org.project1.shopweb.respon.ProductRespon;
import org.project1.shopweb.utils.MessageKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final LocalizationUtils localizationUtils;
    private final ProductRepository productRepository;
    private final ProductRedisService productRedisService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllCategories(@PathVariable long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping({"/images/{imageName}"})

    public ResponseEntity<?> viewImage(@PathVariable String imageName)throws Exception{
        log.info("da vao dc req");

            Path imagePath = Paths.get("uploads").resolve(imageName);

            // Nếu file không tồn tại, sử dụng ảnh 404
            if (!Files.exists(imagePath)) {
                imagePath = Paths.get("uploads/404notfound.png");
            }

            UrlResource resource = new UrlResource(imagePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);

    }






    @GetMapping()
    public ResponseEntity<?> getAllProduct(@RequestParam(defaultValue = "0") int pageNum,
                                           @RequestParam(defaultValue = "id") String sortField,
                                           @RequestParam(defaultValue = "asc") String sortDir,
                                           @RequestParam(required = false,defaultValue = "0") Long categoryId,
                                           @RequestParam(defaultValue = "",required = false) String keyword) throws JsonProcessingException {
        int pageSize = 12;
        int totalPages = 0;
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());

         ProductCacheWrapper productCacheWrapper = productRedisService
                .getAllProducts(keyword, categoryId, pageRequest);

        if (productCacheWrapper != null
                && productCacheWrapper.getProducts() != null
                && !productCacheWrapper.getProducts().isEmpty()) {
            totalPages = productCacheWrapper.getTotalPages();
        }


         if(productCacheWrapper == null){
             productCacheWrapper = new ProductCacheWrapper();
             Page<ProductRespon> productPage = productService.findAll(categoryId,keyword,pageRequest);
             productCacheWrapper.setTotalPages(productPage.getTotalPages());
             productCacheWrapper.setProducts(productPage.getContent());
             totalPages = productPage.getTotalPages();

             log.info("lay tu db");


             productRedisService.saveAllProducts(
                     productCacheWrapper,
                     keyword,
                     categoryId,
                     pageRequest
             );

         }
        return ResponseEntity.ok(ProductListRespon
                .builder()
                .products(productCacheWrapper.getProducts())
                .totalPages(totalPages)
                .build());
    }
    @PostMapping(value = "upload/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertCategories(@RequestParam("files") List<MultipartFile> files,
                                              @PathVariable int id) throws Exception{

            Product existProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            int sizeOfFile = files.size();
            if(sizeOfFile > ProductImage.MAXiMUM){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you can upload only 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();

            for( MultipartFile file : files) {
                // despite that there is no upload file,it stil has obne element is filename = "",check again
                if(file.getSize() == 0){
                    continue;
                }
                if (file.getSize() > 10 * 1024 * 1024) {
                    return new ResponseEntity<>(localizationUtils.getLocalizationMessages(MessageKeys.MAX_MB), HttpStatus.PAYLOAD_TOO_LARGE);
                }
                String contenttype = file.getContentType();
                if (contenttype == null || !contenttype.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(localizationUtils.getLocalizationMessages(MessageKeys.MUST_IMG));

                }
                String filename = storeFile(file);
                ProductImageDTO productImageDTO = ProductImageDTO.builder()
                        .productId(existProduct.getId())
                        .imageUrl(filename)
                        .build();
                ProductImage productImage = productService.createProductImage(productImageDTO,sizeOfFile);
                productImages.add(productImage);
                sizeOfFile--;
            }
            return ResponseEntity.ok("creat image successfully");

    }
    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @PostMapping("")
    //POST http://localhost:8088/v1/api/products
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO
    ) {
            Product newProduct = productService.creatProduct(productDTO);
            return ResponseEntity.ok(newProduct);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id,@Valid @RequestBody ProductDTO productDTO){
        try {
            productService.updateProduct(id, productDTO);
            return ResponseEntity.ok("put method with id: " + id);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product with id: " + id + " is successfully deleted");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/generateFakeProduct")
    private ResponseEntity<String> generateFakeProduct(){
        Faker faker = new Faker();
        for(int i = 0; i < 200;i++){
            String productName = faker.commerce().productName();
            if(productService.existByName(productName)){
                continue;
            }
            ProductDTO newProduct = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(50,100000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long)faker.number().numberBetween(1,3))
                    .build();
            try {
                productService.creatProduct(newProduct);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Products created successfully");
        }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids){
        try{
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            logger.info("test loggin");

            List<Product> productList = productService.findProductsbyIds(productIds);
            return ResponseEntity.ok(productList);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    }