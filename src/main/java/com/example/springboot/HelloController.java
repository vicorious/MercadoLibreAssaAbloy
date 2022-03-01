package com.example.springboot;

import com.example.springboot.dto.AccessToken;
import com.example.springboot.service.FacadeQADService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.exception.RefreshTokenException;

@RestController
public class HelloController 
{
	@Autowired
	private FacadeQADService facadeQADService;

	public HelloController() {
		this.facadeQADService = new FacadeQADService();
	}

	@GetMapping("/ML")
	public String index() {
		try {
			AccessToken accessToken = this.facadeQADService.accessToken(this.facadeQADService.TGToken());
			this.facadeQADService.ProcessOrdersAndShipping(accessToken.getAccess_token());

		}catch (RefreshTokenException e) {
			e.printStackTrace();
			this.facadeQADService.email(e.getMessage(), "MercadoLibre Refresh error: ");
		} 
		catch (Exception e) {
			e.printStackTrace();
			this.facadeQADService.email(e.getMessage(), "MercadoLibre error: ");
		}
		
		return "FAIL";
	}



	

}
