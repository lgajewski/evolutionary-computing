package pl.edu.agh.toik.ec.visualization.conf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Website serving.
 * 
 * @author Ron Schnell
 */
@Controller
public class ViewController {

	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}

}
