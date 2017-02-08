# Spring REST-API with swagger rest documentation :

Start jetty server with maven

mvn jetty:run

# Swagger rest-api documentation

**rest api page : http://localhost:8080/api/swagger-ui.html#/
**rest api return as json form: http://localhost:8080/api/v2/api-docs
**maven dependency
```
<dependencies>
    <dependency>
     <groupId>io.springfox</groupId>
     <artifactId>springfox-swagger2</artifactId>
     <version>2.4.0</version>
    </dependency>
    
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger-ui</artifactId>
   <version>2.4.0</version>
  </dependency>     
```

**MvcConfig.java
```

@EnableWebMvc //mvc:annotation-driven
@Configuration
@ComponentScan(basePackages = {"com.rupp.spring.controller", "com.rupp.spring.service", "com.rupp.spring.dao"})
@PropertySource(name = "application", value = { "classpath:/application.properties" }) //import properties file
@EnableSwagger2 //for swagger annotation
public class MvcConfig extends WebMvcConfigurerAdapter {

...

  //<mvc:resources location="classpath:/META-INF/resources/" mapping="swagger-ui.html"></mvc:resources>
    //<mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"></mvc:resources>
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
          .addResourceLocations("classpath:/META-INF/resources/");
     
        registry.addResourceHandler("/webjars/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    

    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build().pathMapping("/api");
    }

}
```

**Controller sample code

```


@Controller
@RequestMapping("categories")
@Api(tags = "categories", description = "Category apis")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService service;
    

    //@RequestMapping(value = "/v1", method = RequestMethod.GET)
    @GetMapping("/v1/all")
    @ResponseBody
    
    public List<DCategory> getDCategories() {
        logger.debug("====get all categories====");
        return service.list();
    }
    
    @GetMapping("/v1")
    @ResponseBody
    @ApiOperation(value="get categories by paging", response = ResponseList.class)
    public ResponseList<DCategory> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
            @RequestParam(value = "cursorkey", required = false) String cursorkey) {
        logger.info("====get page {} , {} ====", pagesize, cursorkey);
        return service.getPage(pagesize, cursorkey);
    }

    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.GET)
    @GetMapping("/v1/{id}")
    @ApiOperation(value="get category by id.", notes = "categories", response = DCategory.class, produces = "json")
    public ResponseEntity<DCategory> getDCategory(@PathVariable("id") Long id) {

        logger.debug("====get category detail with id :[{}] ====", id);
        
        final DCategory category = service.get(id);
        if (category == null) {
            return new ResponseEntity("No DCategory found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

   ....
```
more detail : https://springfox.github.io/springfox/docs/snapshot/#introduction


# CRUD REST-APIs :

- GET http://localhost:8080/api/categories/v1/all

- GET http://localhost:8080/api/cagetoires/v1/{id}

- POST http://localhost:8080/api/cagetoires/v1/{id}

- DELETE http://localhost:8080/api/cagetoires/v1/{id}

- PUT http://localhost:8080/api/cagetoires/v1/{id}

# Client html page crud action 
- http://localhost:8080/bootstrap-schema.html

# Maven spring-jdbc  
```java
<dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>4.3.5.RELEAS</version>
</dependency>


 <!-- MySQL database driver -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.22</version>
    </dependency>
  <!-- common-dbcp2 -->
  <dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-dbcp2</artifactId>
    <version>2.1.1</version>
```
# Create database    
```java
=====initial sql schema.sql======== run it mysql console
DROP DATABASE IF EXISTS rupp_test;
CREATE DATABASE rupp_test;
USE rupp_test;
         
DROP TABLE IF EXISTS category;
CREATE TABLE category (
   id INT NOT NULL AUTO_INCREMENT,
   name VARCHAR(400) NOT NULL,
   createdDate timestamp DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
 
INSERT INTO category (name) values ('Restaurant');
INSERT INTO category (name) values ('Food and Drink');
INSERT INTO category (name) values ('Entertainment');
INSERT INTO category (name) values ('Outdoor');
INSERT INTO category (name) values ('Days Out');
INSERT INTO category (name) values ('Life Style');
INSERT INTO category (name) values ('Shopping');
INSERT INTO category (name) values ('Service');
INSERT INTO category (name) values ('Sports and Fitness');
INSERT INTO category (name) values ('Health and Beauty');

==============================================
```

