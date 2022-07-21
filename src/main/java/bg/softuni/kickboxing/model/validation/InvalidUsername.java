package bg.softuni.kickboxing.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = InvalidUsernameValidator.class)
public @interface InvalidUsername {
    String message() default "Username is invalid taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
