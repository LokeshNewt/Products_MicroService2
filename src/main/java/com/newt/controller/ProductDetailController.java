package com.newt.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.newt.model.ProductDetail;
import com.newt.repository.ProductDetailRepository;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/products")
public class ProductDetailController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getName(){
		return "Hi";
	}
	
	@Autowired
    private DiscoveryClient discoveryClient;
    private final ProductDetailRepository repository;
    
    
    String url=new String();

    
    @Autowired
    public ProductDetailController(ProductDetailRepository repository) {
        this.repository = repository;
    }
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all Products")
    public Iterable findAllProducts(){
    	return repository.findAll();
    }
    
    
    @ApiOperation(value = "Get all Cars - api call")
    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public Object findAll() {
    	return getapi();
    }
    
    @ApiOperation(value = "Get the product using id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductDetail find(@PathVariable String id) {
           return repository.findOne(id);   
    }
    
    @ApiOperation(value = "post a product")
    @RequestMapping(method = RequestMethod.POST)
    public ProductDetail create(@RequestBody ProductDetail detail) {
        return repository.save(detail);
    }
    
    @ApiOperation(value="Update ProductDetails By productName")
	@RequestMapping(value="/update/{productName}/{productId}", method=RequestMethod.PUT)
	public int updateBysal(@PathVariable String productName,@PathVariable String productId){
		return repository.updateByName(productName, productId);
		
	}
    
    @ApiOperation(value = "Delete the product using id")
    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id){
		 repository.delete(id);
    	
    }
    
    public HttpEntity<String> prepareGet() throws  IOException {
    	  HttpEntity<String> entity = new HttpEntity<String>(prepareHeader());
    	  return entity;
    	 }
    
    public HttpHeaders prepareHeader() throws  IOException {
    	  HttpHeaders headers = new HttpHeaders();
    	  headers.add("Content-Type", "application/json");
    	  return headers;
    	 }
    
   
    public RestTemplate restTemplate = new RestTemplate();
    
    public Object getapi(){
    	ResponseEntity<Object> response = null;

    	HttpEntity<String> requestEntity;
    	try {
    		requestEntity = prepareGet();
    		
    		discoveryClient.getInstances("CARSERVICE").forEach((ServiceInstance s) -> {
                url=s.getUri().toString();
            });
    		
    		System.out.println("URL :"+url);
    		response = restTemplate.exchange(url+"/cars", HttpMethod.GET, requestEntity, Object.class);
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		return response.getBody();
    }
}