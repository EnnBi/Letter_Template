package com.aaratech.letter.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aaratech.letter.dao.AttributeRepo;
import com.aaratech.letter.dao.LetterTemplateRepo;
import com.aaratech.letter.model.Attribute;
import com.aaratech.letter.model.LetterTemplate;
import com.itextpdf.html2pdf.HtmlConverter;

@Controller
@RequestMapping("/letter")
public class LetterTemplateController {

	@Autowired
	LetterTemplateRepo letterTemplateRepo;
	@Autowired
	AttributeRepo attributeRepo;
	
	final String ACTIVE="Active";
	final String PATH = "D:\\reports_output\\";
	
	/*@RequestMapping(value="/template",method=RequestMethod.GET)
	public String getTemplate(Model model){
		
		if(!model.asMap().containsKey("letterTemplate"))
			model.addAttribute("letterTemplate", new LetterTemplate());
		
		model.addAttribute("keys", attributeRepo.findAllKeys(ACTIVE));
		return "template";
	}*/
	
	
	@RequestMapping(value="/template",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> saveTemplate(LetterTemplate letterTemplate,Model model){
		if(letterTemplate.getId()>0)
			letterTemplate.setUpdatedOn(new Date());
		else
			letterTemplate.setCreatedOn(new Date());
		
		 letterTemplate.setStatus(ACTIVE);
		 letterTemplate = letterTemplateRepo.save(letterTemplate);
		return ResponseEntity.ok(letterTemplate);
	}
	
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public String searchTemplate(Model model){
		
		if(!model.asMap().containsKey("letterTemplate"))
			model.addAttribute("letterTemplate", new LetterTemplate());
		
		model.addAttribute("keys", attributeRepo.findAllKeys(ACTIVE));
		
		model.addAttribute("templates", letterTemplateRepo.findByStatusAndApproved(ACTIVE,true));
		return "search";
	}
	
	@RequestMapping(value="/authorize",method=RequestMethod.GET)
	public String authorizeTemplate(Model model){
		model.addAttribute("letterTemplate", new LetterTemplate());
		model.addAttribute("templates", letterTemplateRepo.findByStatusAndApproved(ACTIVE,false));
		return "authorize";
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> edit(@PathVariable("id") long id,Model model){
		Optional<LetterTemplate> template = letterTemplateRepo.findById(id);
		model.addAttribute("letterTemplate",template);
		model.addAttribute("keys",attributeRepo.findAllKeys(ACTIVE));
		return ResponseEntity.ok("");
		
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	public  @ResponseBody ResponseEntity<?> delete(@PathVariable("id") long id){
		letterTemplateRepo.deleteById(id);
		return ResponseEntity.ok("");
		
	}
	
	@RequestMapping(value="/template/{name}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> generateTemplate(@PathVariable("name") String name){
		LetterTemplate template= letterTemplateRepo.findByName(name);
		List<String> keys = new ArrayList<String>(Arrays.asList(StringUtils.substringsBetween(template.getBody(),"${","}")));
		 
		String body = template.getBody();
				
		List<Attribute> attributes = attributeRepo.findByKeyIn(keys);
		for (Attribute attribute : attributes) {	
			if(attribute.getWidth()>0) {
				String imgTag ="<img src="+attribute.getValue()+" width="+attribute.getWidth()+"  height="+attribute.getHeight()+"/>";
				body=body.replace(valueKey(attribute.getKey()), imgTag);
			}
			else
				body = body.replace(valueKey(attribute.getKey()),attribute.getValue());		
		}
		
		List<String> attachments = new ArrayList<>();
		attachments.add(PATH+"img1.jpg");
		attachments.add(PATH+"img2.jpg");
		attachments.add(PATH+"img3.jpg");
		body = body.concat("<br><br><br><br><br>");
		body=body.concat("<div style='page-break-before:always'></div>");
		for (String image : attachments) {
			String imgTag ="<img src="+image+"/><br><br>";
			body = body.concat(imgTag);
		}
		
		String fileName = PATH+template.getName()+".pdf";
		generatePdf(body, fileName);
		return ResponseEntity.ok("");
		
	}
	
	@GetMapping("/attributes")
	public @ResponseBody String addAttributes(){
		Attribute attribute = new Attribute("name",0, 0, "Ravi",ACTIVE);
		attributeRepo.save(attribute);
		Attribute attribute2 = new Attribute("date",0, 0, "07-2020",ACTIVE);
		attributeRepo.save(attribute2);
		Attribute attribute3 = new Attribute("cost",0, 0, "1857",ACTIVE);
		attributeRepo.save(attribute3);
		Attribute attribute4 = new Attribute("image",50, 20, PATH+"logo.png",ACTIVE);
		attributeRepo.save(attribute4);
		return "good";
	}
	
	public void generatePdf(String body,String fileName){
		try {
			HtmlConverter.convertToPdf(body, new FileOutputStream(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String valueKey(String key){
		return "${"+key+"}";
	}
	
}
