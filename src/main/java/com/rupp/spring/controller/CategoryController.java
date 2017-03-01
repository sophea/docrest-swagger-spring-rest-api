package com.rupp.spring.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupp.spring.domain.DCategory;
import com.rupp.spring.domain.ResponseList;
import com.rupp.spring.service.CategoryService;

@Controller
@RequestMapping(value = "categories", produces = { MediaType.APPLICATION_JSON_VALUE} )
@Api(tags = "categories", description = "Category apis")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService service;
    

    //@RequestMapping(value = "/v1", method = RequestMethod.GET)
    @GetMapping(value = "/v1/all", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @ApiOperation(value="get all categories", notes = "get all categories")
    public List<DCategory> getDCategories() {
        logger.debug("====get all categories====");
        return service.list();
    }
    
    @GetMapping(value = "/v1", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    @ApiOperation(value="get categories by paging",nickname = "get categories by paging")
    public ResponseList<DCategory> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
            @RequestParam(value = "cursorkey", required = false) String cursorkey) {
        logger.info("====get page {} , {} ====", pagesize, cursorkey);
        return service.getPage(pagesize, cursorkey);
    }

    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.GET)
    @GetMapping(value = "/v1/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value="get category by id.", notes = "get category by id", response = DCategory.class)
    public ResponseEntity<DCategory> getDCategory(@PathVariable("id") Long id) {

        logger.debug("====get category detail with id :[{}] ====", id);
        
        final DCategory category = service.get(id);
        if (category == null) {
            return new ResponseEntity("No DCategory found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //@RequestMapping(value = "/v1", method = RequestMethod.POST)
    @PostMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="create category", response = DCategory.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DCategory> createDCategory(@ModelAttribute DCategory category) {
        logger.debug("====create new category object ====");
        service.create(category);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.DELETE)
    @DeleteMapping(value = "/v1/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="delete category", response = Long.class)
    public ResponseEntity deleteDCategory(@PathVariable Long id) {
        logger.debug("====delete category detail with id :[{}] ====", id);
        if (null == service.delete(id)) {
            return new ResponseEntity("No DCategory found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(id, HttpStatus.OK);

    }
    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.PUT)
    @PutMapping(value = "/v1/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value="update category", response = DCategory.class)
    public ResponseEntity<DCategory> updateDCategory(@PathVariable Long id, @ModelAttribute DCategory category) {
        logger.debug("====update category detail with id :[{}] ====", id);
        category = service.update(id, category);

        if (null == category) {
            return new ResponseEntity("No DCategory found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    @PostMapping(value = "/v1/{id}/json", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value="update category by json", response = DCategory.class)
    public ResponseEntity<DCategory> updateByJson(@PathVariable Long id, @RequestBody DCategory category) {
        logger.debug("====update category detail with id :[{}] ====", id);
        category = service.update(id, category);

        if (null == category) {
            return new ResponseEntity("No DCategory found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/json", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value="create category by json", response = DCategory.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DCategory> createByJson(@RequestBody DCategory category) {
        logger.debug("====create new category object with json====");
        service.create(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    
    /**
     * 
     * schema api : Content-Type: application/x-www-form-urlencoded 
     * example json value
     * 
     *   {
     *       primaryKeyName: "id",
     *       tableName: "Country",
     *       primaryKeyType: "long",
     *       columns: {
     *           comingSoon: "boolean",
     *           flagImageUrl: "text",
     *           isoCode: "text",
     *           name: "text",
     *           state: "long",
     *           tcsUrl: "text",
     *           createdDate: "datetime"
     *        }
     *   }
     * 
     * @param request
     */
    @RequestMapping(value = "v1/schema", method = { RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value="get category schema", response = Map.class,
            notes = "get category schema return as json key and value ex : {\" primaryKeyName: \"id\", tableName:country,...")
    public ResponseEntity<Map<String, Object>> getschma(HttpServletRequest request) {
        final Map<String, Object> body = new HashMap<String, Object>();
        final Map<String,String> columns = new HashMap<>();
        
        columns.put("name", "text");
        columns.put("createdDate", "datetime");
        
        body.put("columns", columns);
        body.put("tableName", "category");
        body.put("primaryKeyName", "id");
        body.put("primaryKeyType", "long");
        
        return new ResponseEntity<Map<String, Object>>(body, HttpStatus.OK);
    }
    
}
