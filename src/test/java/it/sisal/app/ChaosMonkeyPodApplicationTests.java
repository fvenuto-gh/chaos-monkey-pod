package it.sisal.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import it.sisal.app.controller.PodController;
import it.sisal.app.service.PodService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;


@SpringBootTest
class ChaosMonkeyPodApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @MockBean
	private PodController podController;
    @MockBean
    private PodService podService;
    
	private MockMvc mockMvc;
	
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    
	@Test
	public void deleteRandomPodWhenNamespaceIsTesting_success() throws Exception {
		when(podController.randomPod(Optional.of("testing")))
				.thenReturn(new ResponseEntity<String>(new String(), HttpStatus.OK));
		
		MvcResult result = mockMvc.perform(delete("/pods/random?namespace=testing"))
                .andExpect(status().isOk())
                .andReturn();

		Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());	
	}

	
	
}
