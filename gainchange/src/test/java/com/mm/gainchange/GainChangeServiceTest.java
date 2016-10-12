package com.mm.gainchange;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mm.rest.gainchange.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@AutoConfigureMockMvc
public class GainChangeServiceTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void getGainchangeCallShouldInitiallyReturnNoResults() throws Exception {
		this.mockMvc.perform(get("/gainchange")).andDo(print()).andExpect(status().isOk()); // .andExpect(jsonPath("$length()").value("")
	}
	
//	@Test
//	public void postGainchangeCallShouldSave() throws Exception {
//		this.mockMvc.perform(post("/gainchange")).andDo(print()).andExpect(status().isOk()); // .andExpect(jsonPath("$length()").value("")
//	}

}