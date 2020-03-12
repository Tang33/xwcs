package fast.main.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiBody {
	//值
	String name();
	
	//描述
	String context();
	
	//请求类型
	String type() default "Post";
}
