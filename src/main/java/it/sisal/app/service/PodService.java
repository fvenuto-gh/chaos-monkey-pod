package it.sisal.app.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;


@Service
public class PodService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PodService.class);
	
	@Autowired
	private CoreV1Api api;
	
	
	public V1PodList getPods(String namespace) throws Exception{
    	LOGGER.info("Retrieving all pods into namespace: " + namespace);
		try {						   	
			// get pods into a Kubernetes namespace
			return api.listNamespacedPod(namespace, "true", null, null, null, null, null, null, null, 10, false);
		} catch (ApiException apiex) {			
			throw new Exception("Error while retrieving pods: " + apiex.getResponseBody());
		}     
    }
    
	public V1PodList getPodsByLabel(String label, String namespace) throws Exception{    	
		try {			
			LOGGER.info("Target pods selected by label [{}] into namespace [{}]", label, namespace);			
			return api.listNamespacedPod(namespace, "true", null, null, null, label, null, null, null, 10, false);
		} catch (ApiException apiex) {			
			throw new Exception("Error while retrieving pods by label: " + apiex.getResponseBody());
		}		     
    }
    
	public String deletePod(V1Pod targetPod) throws Exception{    	
		try {
			LOGGER.info("Deleting target pod [{}] into namespace [{}]", targetPod.getMetadata().getName(), targetPod.getMetadata().getNamespace());							    	
			V1Pod response = api.deleteNamespacedPod(targetPod.getMetadata().getName(), targetPod.getMetadata().getNamespace(), "true", null, null, null, null, null);
			LOGGER.info("Target pod: delete request sent.");
			return targetPod.getMetadata().getName();
		} catch (ApiException apiex) {			
			throw new Exception("Error while deleting pod: " + apiex.getResponseBody());
		}        
    }
    
	public List<String> deletePod(List<V1Pod> targetPods) throws Exception{    	
		try {
			List<String> deletedPods = new ArrayList<String>();
			// delete pods
			for (V1Pod tp : targetPods) {				
				api.deleteNamespacedPod(tp.getMetadata().getName(), tp.getMetadata().getNamespace(), "true", null, null,null, null, null);
				deletedPods.add(tp.getMetadata().getName());
				LOGGER.info("Target pod [{}] into namespace [{}]: delete request sent.", tp.getMetadata().getName(), tp.getMetadata().getNamespace());
			}
			return deletedPods;
		} catch (ApiException apiex) {			
			throw new Exception("Error while deleting pods: " + apiex.getResponseBody());
		}       
    }
}
