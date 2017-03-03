package Proj.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Proj.models.RecallWebMessage;
import Proj.models.wordsSql;

@Controller
public class RecallWebController {

	int userid;
	int testid;
	double score = 0.0;
	ArrayList<String> wordList = new ArrayList<String>();
	ArrayList<String> answerKey = new ArrayList<String>();
	ArrayList<String> userAnswers = new ArrayList<String>();
	//ArrayList<Integer> testOptionsList = new ArrayList<Integer>();
	
	//@Autowired
	//private RecallWebLogDao recallWebLogDao;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String introForm(){
		return "introForm";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String intro(HttpServletRequest request, Model model) throws Exception{
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		
		if (name == null || name == ""){
			name = "world";
		}

		model.addAttribute("name", name);
		model.addAttribute("message", RecallWebMessage.getMessage(name));
		
		RecallWebMessage registrar = new RecallWebMessage();
		if(registrar.authenticate(name, password) == false)
		{
			//registrar.register(name, password);
			//userid = registrar.saveId(name, password);
			model.addAttribute("feedback", "invalid login");
			return "introForm";
		}
		userid = registrar.saveId(name, password);
		return "intro";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registration(){
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request, Model model) throws Exception{
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		
		if (name == null || name == ""){
			name = "world";
		}
		
		model.addAttribute("name", name);
		model.addAttribute("message", RecallWebMessage.getMessage(name));
		model.addAttribute("feedback", "username exists");
		
		RecallWebMessage registrar = new RecallWebMessage();
		if(registrar.authenticate(name, password) == false)
		{
			registrar.register(name, password);
			userid = registrar.saveId(name, password);
			return "intro";
		}
		else{
			//user exists
			return "register";
		}
	}
	
	//testing
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String introPage(){
		return "intro";
	}
	
	
	@RequestMapping(value = "/wordsPage", method = RequestMethod.GET)
	public String wordsPage() throws Exception{
		
		wordsSql creator = new wordsSql();
		testid = creator.addTest(userid);
		wordList.clear();
		return "wordsPage";
		
	}
	
	@RequestMapping(value = "/wordsPage", method = RequestMethod.POST)
	public void wordsPage(HttpServletRequest request, Model model) throws Exception{

		String word = request.getParameter("word");
		wordsSql adder = new wordsSql();
		String wordLower = word.toLowerCase();
		wordList.add(wordsSql.getWord(wordLower));
		model.addAttribute("testwords", wordList);
		
		adder.addWord(testid, wordsSql.getWord(wordLower));
	}
	
	@RequestMapping(value = "/seeTests", method = RequestMethod.GET)
	public String seeTests(HttpServletRequest request, Model model) throws Exception{
		
		wordsSql testOptions = new wordsSql();
		model.addAttribute("testIdNumbs", testOptions.selTestIds(userid));
		
		return "seeTests";
		
	}
	
	@RequestMapping(value = "/seeTests", method = RequestMethod.POST)
	public String seeTestWords(HttpServletRequest request, Model model) throws Exception{
		
		int currTestId = Integer.parseInt(request.getParameter("testIdSelects"));
		wordsSql currentTestWords = new wordsSql();
		wordsSql testOptions = new wordsSql();
		wordsSql average = new wordsSql();
		
		model.addAttribute("testIdNumbs", testOptions.selTestIds(userid));
		model.addAttribute("testWords", currentTestWords.selTestWords(currTestId));
		//String avgStr = Double.toString(average.showAvg(currTestId));
		
		ArrayList<Double> scoreList = average.showAvg(currTestId);
        double sum = 0.0;
        double avg = 0.0;
        if(!scoreList.isEmpty()) {
          for (double singleScore : scoreList) {
              sum += singleScore;
          }
          avg = sum/scoreList.size();
        }
        else{
        	avg = sum;
        }
        String avgStr = String.format("%.2f", avg);
		model.addAttribute("avg", avgStr);
		String timesTakenStr = Integer.toString(scoreList.size());
		model.addAttribute("timesTaken", timesTakenStr);
		
		return "seeTests";
		
	}
	
	//request mapping. value = start. 
	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public String startForm(HttpServletRequest request, Model model) throws Exception{
		wordsSql testOptions = new wordsSql();
		model.addAttribute("testIdNumbs", testOptions.selTestIds(userid));
		
		return "start";
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public String start(HttpServletRequest request, Model model) throws Exception{
		
		int currTestId = Integer.parseInt(request.getParameter("testIdSelects"));
		testid = Integer.parseInt(request.getParameter("testIdSelects"));
		wordsSql currentTestWords = new wordsSql();
		model.addAttribute("testWords", currentTestWords.selTestWords(currTestId));

	return "start2";
	}
	
	@RequestMapping(value = "/answerBox", method = RequestMethod.GET)
	public String answerBox(HttpServletRequest request, Model model) throws Exception{
		userAnswers.clear();
		score = 0.0;
		return "answerBox";
	}
	
	@RequestMapping(value = "/answerBox", method = RequestMethod.POST)
	public void answer(HttpServletRequest request, Model model) throws Exception{
		
		String userAnswer = request.getParameter("userAnswer");
		
		wordsSql currentTestWords = new wordsSql();
		wordsSql grader = new wordsSql();
		
		answerKey = currentTestWords.selTestWords(testid);
		double keySize = answerKey.size();
		String userAnswerLower = userAnswer.toLowerCase();
		score = score + (grader.grade(userAnswerLower, answerKey, userAnswers)/keySize);
		String scoreStr = String.format("%.2f", score);
		model.addAttribute("currScore", scoreStr);
		
		userAnswers.add(wordsSql.getWord(userAnswerLower));
		model.addAttribute("wordsAnswered", userAnswers);

	}
	
	@RequestMapping(value = "/finish", method = RequestMethod.GET)
	public String finish(HttpServletRequest request, Model model) throws Exception{
		//send score to memory.scores
		wordsSql saveScores = new wordsSql();
		saveScores.saveScore(score, testid);
		return "finish";
	}
	
	
}
