package com.example.springboot;

import com.example.springboot.service.QADService;
import com.example.springboot.wsdl.mapping.WmEnvioTransaccionesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController 
{
	@Autowired
	private QADService qadService;

	public HelloController() {
		this.qadService = new QADService();
	}

	@GetMapping("/ML")
	public String index() {
		try {
			WmEnvioTransaccionesResponse response = this.qadService.getQADInfo();
			String result = "Servicio Comerssia consumido correctamente: "+response.getWmEnvioTransaccionesResult().getContent().size();
			for(Object resultr : response.getWmEnvioTransaccionesResult().getContent())
				result = result + resultr.toString();
			this.qadService.email((result), "QAD Comerssia");
			return "OK";
		} catch (Exception e) {
			this.qadService.email(e.getMessage(), "QAD Comerssia");
		}
		return "FAIL";
	}

	@GetMapping("/enviarTransactionCallBack")
	public String enviarCallBack() {
		try {
			this.qadService.email("Servicio Comerssia consumido correctamente", "QAD Comerssia");
			return "OK";

		} catch (Exception e) {
			this.qadService.email(e.getMessage(), "QAD Comerssia");
		}
		return "FAIL";
	}

	@GetMapping("/reportes")
	public String reportes() {
		try {
			this.qadService.reportes();
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAIL";
	}


	@GetMapping("/reportesCallBack")
	public String reportesCallBack() {
		try {
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAIL";
	}
	

}
