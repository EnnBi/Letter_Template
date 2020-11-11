package com.aaratech.letter.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aaratech.letter.dao.ImageRepo;
import com.aaratech.letter.dao.LetterTemplateRepo;
import com.aaratech.letter.model.Image;
import com.aaratech.letter.model.LetterTemplate;
import com.itextpdf.html2pdf.HtmlConverter;

@Controller
@RequestMapping("/letter")
public class LetterTemplateController {

	@Autowired
	LetterTemplateRepo letterTemplateRepo;
	@Autowired
	ImageRepo imageRepo;
	
	final String PATH = "D:\\reports_output\\";
	
	@RequestMapping(value="/template",method=RequestMethod.GET)
	public String getTemplate(Model model){
		
		model.addAttribute("letterTemplate", new LetterTemplate());
		return "template";
	}
	
	
	@RequestMapping(value="/template",method=RequestMethod.POST)
	public String saveTemplate(LetterTemplate letterTemplate,Model model){
	
		 List<String> valuekeys = new ArrayList<String>(Arrays.asList(StringUtils.substringsBetween(letterTemplate.getBody(),"${","}")));
		 List<String> imageKeys = new ArrayList<String>(Arrays.asList(StringUtils.substringsBetween(letterTemplate.getBody(),"#{","}")));
		 
		 valuekeys.addAll(imageKeys);
		 letterTemplate.setKeys(new HashSet<>(valuekeys));
		 letterTemplateRepo.save(letterTemplate);
		
		return "template";
	}
	
	@RequestMapping(value="/template/{name}",method=RequestMethod.GET)
	public @ResponseBody String generateTemplate(@PathVariable("name") String name){
		LetterTemplate template= letterTemplateRepo.findByName(name);
		Map<String,String> map = new HashMap<>();
		map.put("name","Ravi");
		map.put("date","June-2020");
		map.put("cost","8795");

		Map<String,Image> imageMap= new HashMap<>();
		Image headerImage = new Image(PATH+"yt.jpg", 50, 50);
		imageMap.put("headerimage",headerImage);
		
		Image footerImage = new Image(PATH+"logo.png",50,20);
		imageMap.put("footerimage",footerImage); 
		String body = template.getBody();
		
		
		for (String key : template.getKeys()) {
			if(body.contains(valueKey(key)))
				body = body.replace(valueKey(key),map.getOrDefault(key,""));
			else if(body.contains(imageKey(key))){
				Image image=imageMap.getOrDefault(key,null);
				if(image!= null){
					String imgTag ="<img src="+image.getPath()+" width="+image.getWidth()+"  height="+image.getHeight()+"/>";
					body=body.replace(imageKey(key), imgTag);
				}
			}
			
		}
		
		String fileName = PATH+template.getName()+".pdf";
		generatePdf(body, fileName);
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
	
	public String imageKey(String key){
		return "#{"+key+"}";
		
	}
	
}
