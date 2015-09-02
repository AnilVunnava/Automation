package com.srinsoft.slenium.tests.web.mvc;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.srinsoft.slenium.tests.web.domain.Configuration;
import com.srinsoft.slenium.tests.web.domain.InputData;
import com.srinsoft.slenium.tests.web.domain.Member;
import com.srinsoft.slenium.tests.web.repo.MemberDao;

@Controller
@RequestMapping(value = "/")
public class MemberController {
	@Autowired
	private MemberDao memberDao;

	@RequestMapping(method = RequestMethod.GET, value = "index")
	public String displaySortedMembers(Model model) {
		model.addAttribute("newMember", new Member());
		model.addAttribute("members", memberDao.findAllOrderedByName());
		return "index";
	}

	@RequestMapping(method = RequestMethod.GET, value = "tabpanel")
	public String displayTabbedPanel(Model model) {
		model.addAttribute("config", new Configuration());
		model.addAttribute("testInput", new InputData());
		return "tabpanel";
	}

	@RequestMapping(method = RequestMethod.POST, value = "register")
	public String registerTests(
			@Valid @ModelAttribute("config") Configuration config,
			BindingResult result, Model model) {
		System.out.println("===============================================");
		System.out.println("Configuration : " + config.getBrowser());
		System.out.println("Configuration : "
				+ config.getDesiredBrowserVersion());
		System.out
				.println("Configuration : " + config.getScreenshotDirectory());
		System.out.println("Configuration : " + config.getTestResultsDir());
		System.out.println("Configuration : " + config.getContext());
		System.out.println("Configuration : " + config.getHost());
		System.out.println("Configuration : " + config.getPort());
		System.out.println("Configuration : " + config.isEnableVideo());
		System.out.println("===============================================");
		System.out.println("Reports Mail : " + config.getEmail());
		System.out.println("===============================================");
		System.out.println("Setup : " + config.getModuleName());
		System.out.println("Setup : " + config.getTestUrl());
		System.out.println("Setup : " + config.getTestCase());
		System.out.println("Setup : " + config.getTestExpected());
		System.out.println("Setup : " + config.getTestInput());
		System.out.println("===============================================");
		model.addAttribute("url", config.getTestUrl());
		model.addAttribute("elements",
				SeleniumElements.getWebElements(config.getTestUrl()));
		return "html-report";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String registerNewMember(
			@Valid @ModelAttribute("newMember") Member newMember,
			BindingResult result, Model model) {
		if (!result.hasErrors()) {
			memberDao.register(newMember);
			return "redirect:/";
		} else {
			model.addAttribute("members", memberDao.findAllOrderedByName());
			return "index";
		}
	}
}