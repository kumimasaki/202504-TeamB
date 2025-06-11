package ec.com.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;

import ec.com.model.entity.Lesson;

public class test {
	/* 一覧画面の表示
	セッションからカートの内容を取得、表示
	
	*/
@GetMapping("/lesson/show/cart")
	public String getLessonShowCart(
			HttpSession session,Model model
			) {
	List<Lesson> list = (List<Lesson>)session.getAttribute("list");
		if(list == null) {
			list = new ArrayList<>();
		}
			model.addAttribute("loginFlg", true);
			model.addAttribute("list", list);
			return "user_planned_application.html";
}
}
