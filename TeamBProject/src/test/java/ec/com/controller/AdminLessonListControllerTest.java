package ec.com.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import ec.com.model.dto.LessonStatsWithInfoDto;
import ec.com.model.entity.Admin;
import ec.com.services.LessonService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminLessonListControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private LessonService lessonService;

	private MockHttpSession session;

	@BeforeEach
	public void prepareData() {
		session = new MockHttpSession();
		Admin admin = new Admin();
		// 偽物を作る
		admin.setAdminEmail("admin@test.com");
		session.setAttribute("AdminLogin", admin);
	}

	@Test
	public void testShowLessonList_WithLogin() throws Exception {
		// 講座を作る
		LessonStatsWithInfoDto dto = new LessonStatsWithInfoDto();
		dto.setLessonId(1L);
		dto.setLessonName("Java入門");
		dto.setLessonFee(1000);
		dto.setApplyCount(5);
		dto.setTotalSales(5000);
		dto.setCapacity(30);
		// 一連の流れ
		when(lessonService.getLessonStatsList()).thenReturn(List.of(dto));

		mockMvc.perform(get("/admin/lesson/all").session(session)).andExpect(status().isOk())
				.andExpect(view().name("admin_lesson_lineup")).andExpect(model().attributeExists("statsList"))
				.andExpect(model().attribute("statsList", List.of(dto)));
	}

	@Test
	public void testShowLessonList_WithoutLogin() throws Exception {
		mockMvc.perform(get("/admin/lesson/all")) // 没有 session
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/login"));
	}

	@Test
	void testSearchLessonByKeyword_Unauthenticated_ShouldRedirectToLogin() throws Exception {
		mockMvc.perform(get("/admin/lesson/search").param("keyword", "Java")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/login"));
	}

	@Test
	void testSearchLessonByKeyword_LoggedIn_ShouldReturnFilteredResults() throws Exception {
		LessonStatsWithInfoDto dto1 = new LessonStatsWithInfoDto();
		dto1.setLessonId(1L);
		dto1.setLessonName("Java");
		dto1.setLessonFee(1000);
		dto1.setApplyCount(5);
		dto1.setTotalSales(5000);
		dto1.setCapacity(30);

		LessonStatsWithInfoDto dto2 = new LessonStatsWithInfoDto();
		dto2.setLessonId(2L);
		dto2.setLessonName("Python");
		dto2.setLessonFee(2000);
		dto2.setApplyCount(3);
		dto2.setTotalSales(6000);
		dto2.setCapacity(25);

		when(lessonService.getLessonStatsList()).thenReturn(List.of(dto1, dto2));

		mockMvc.perform(get("/admin/lesson/search").param("keyword", "Java").session(session))
				.andExpect(status().isOk()).andExpect(view().name("admin_lesson_lineup"))
				.andExpect(model().attributeExists("statsList"));
	}

}
