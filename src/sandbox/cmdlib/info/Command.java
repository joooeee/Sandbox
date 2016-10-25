package sandbox.cmdlib.info;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Command {
    
    String name();
    
    String[] aliases() default {};
    
    String description()
    
    default "";
    
    String permission()
    
    default "";
    
    int requiredArguments()
    
    default 0;
    
    String permissionMessage()
    
    default "\u00A7cYou do not have permission to execute this command";
    
    String usage() default "";
    
}
