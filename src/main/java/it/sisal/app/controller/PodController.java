package it.sisal.app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import it.sisal.app.service.PodService;



@RestController
@RequestMapping(value = {"/pods"}, produces = {"application/json"})
public class PodController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PodController.class);
	
	@Autowired
	private PodService podService;
	
	
	@GetMapping(value = "/print", produces = "application/json")
	public ResponseEntity<String> print(@RequestParam Optional<String> namespace) {		
		try {
			String nsTarget = namespace.isPresent() ? namespace.get() : "default";
			LOGGER.info("Printing all pods into namespace: " + nsTarget);
			V1PodList nsPods = this.podService.getPods(nsTarget);			
			if (nsPods.getItems().isEmpty()) {
				LOGGER.info("No pods found into namespace: " + namespace);
				return new ResponseEntity<String>("No pods found into namespace: " + namespace, HttpStatus.OK);				
			}else {
				// get pods name
				ArrayList<String> names = new ArrayList<String>();
				nsPods.getItems().stream().map((pod) -> pod.getMetadata().getName()).forEach(name -> {
					LOGGER.info("pod: " + name);
					names.add(name);
				});	
				return new ResponseEntity<String>("Pods found:" + names.toString(), HttpStatus.OK);
			}
		} catch (ApiException apiex) {
			LOGGER.error("Error during printing pod into namespace:" + apiex.getResponseBody());
			return new ResponseEntity<String>("Error during printing pod into namespace:" + apiex.getResponseBody(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOGGER.error("Error during printing pod into namespace:" + e.getMessage());
			return new ResponseEntity<String>("Error during printing pod into namespace:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
    
    
	@DeleteMapping("/random")
    public ResponseEntity<String> randomPod(@RequestParam Optional<String> namespace){
		try {
    		String nsTarget = namespace.isPresent() ? namespace.get() : "default";
    		LOGGER.info("Deleting random pod into namespace: " + namespace);
	    	V1PodList nsPods = this.podService.getPods(nsTarget);
	    	List<V1Pod> candidates = nsPods.getItems();
	    	if(candidates.isEmpty()) {
	    		LOGGER.info("No pods found into namespace: " + namespace);
	    		return new ResponseEntity<String>("No pods found into namespace: " + namespace, HttpStatus.OK);	    		
	    	}
	    	Collections.shuffle(candidates);
	    	String deletedPod = this.podService.deletePod(candidates.get(0));
	    	return new ResponseEntity<String>("Delete request sent to random pod into namespace " + namespace + ": " + deletedPod, HttpStatus.OK);	    	
		} catch (ApiException apiex) {
			LOGGER.error("Error during deleting random pod into namespace:" + apiex.getResponseBody());
			return new ResponseEntity<String>("Error during deleting random pod into namespace:" + apiex.getResponseBody(), HttpStatus.INTERNAL_SERVER_ERROR);	
		} catch (Exception e) {
			LOGGER.error("Error during deleting random pod into namespace:"+e.getMessage());
			return new ResponseEntity<String>("Error during deleting random pod into namespace:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
	@DeleteMapping("/bylabel")
    public ResponseEntity<String> byLabel(@RequestParam String label, @RequestParam Optional<String> namespace){
		try {
    		String nsTarget = namespace.isPresent() ? namespace.get() : "default";
    		LOGGER.info("Pods selected by label into namespace: " + namespace);
	    	// get pods by label selector
    		V1PodList nsPods = this.podService.getPodsByLabel(label, nsTarget);
	    	List<V1Pod> targetPods = nsPods.getItems();
	    	if(targetPods.isEmpty()) {
	    		LOGGER.info("No pods found into namespace: " + namespace);
	    		return new ResponseEntity<String>("No pods found into namespace: " + namespace, HttpStatus.OK);
	    	}
	    	// delete pods
	    	List<String> deletedPods = this.podService.deletePod(targetPods);
	    	return new ResponseEntity<String>("Pods selected by label into namespace " + namespace + " deleted: " + deletedPods, HttpStatus.OK);
		} catch (ApiException apiex) {
			LOGGER.error("Error during deleting pods into namespace:" + apiex.getResponseBody());
			return new ResponseEntity<String>("Error during deleting pods into namespace:" + apiex.getResponseBody(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOGGER.error("Error during deleting pods into namespace:" + e.getMessage());
			return new ResponseEntity<String>("Error during deleting pods into namespace:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
    }
}
