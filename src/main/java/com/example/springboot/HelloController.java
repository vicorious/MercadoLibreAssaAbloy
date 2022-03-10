package com.example.springboot;

import com.example.springboot.exception.RefreshTokenException;
import com.example.springboot.service.FacadeQADService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController 
{
	@Autowired
	private FacadeQADService facadeQADService;

	public HelloController() {
		this.facadeQADService = new FacadeQADService();
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	@GetMapping("/ML")
	public synchronized String index(@RequestParam String token, @RequestParam String ffin) {
		try {
			this.facadeQADService.ProcessOrdersAndShipping(token, ffin);
			return "OK";
		}catch (Exception e) {
			e.printStackTrace();
			this.facadeQADService.email(e.getMessage(), "MercadoLibre error: ");
		}
		
		return "FAIL";
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	@GetMapping("/accessToken")
	public synchronized String accessToken(@RequestParam String code) {
		try {
			return this.facadeQADService.accessTokenML(code).toString();
		}catch (RefreshTokenException e) {
			e.printStackTrace();
			this.facadeQADService.email(e.getMessage(), "AccessToken Refresh error: ");
		} 
		catch (Exception e) {
			e.printStackTrace();
			this.facadeQADService.email(e.getMessage(), "accessToken error: ");
		}
		
		return "FAIL";
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	@GetMapping("/refreshToken")
	public synchronized String refreshToken(@RequestParam String code) {
		try {
			return this.facadeQADService.accessTokenML(code).toString();
		}catch (RefreshTokenException e) {
			e.printStackTrace();
			this.facadeQADService.email(e.getMessage(), "AccessToken Refresh error: ");
		} 
		catch (Exception e) {
			e.printStackTrace();
			this.facadeQADService.email(e.getMessage(), "accessToken error: ");
		}
		
		return "FAIL";
	}	



	

}
