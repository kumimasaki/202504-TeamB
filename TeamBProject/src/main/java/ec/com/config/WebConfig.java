package ec.com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静的ファイル保存場所（lesson-imageフォルダ）の絶対パスを取得
        String imagePath = Paths.get("src/main/resources/static/lesson-image/").toFile().getAbsolutePath();

        // /lesson-image/** というURLへのアクセスを lesson-image フォルダに通す
        registry.addResourceHandler("/lesson-image/**")
                .addResourceLocations("file:" + imagePath + "/");
    }
}