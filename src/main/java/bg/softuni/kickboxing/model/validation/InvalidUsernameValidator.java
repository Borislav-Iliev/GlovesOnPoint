package bg.softuni.kickboxing.model.validation;

import bg.softuni.kickboxing.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InvalidUsernameValidator implements ConstraintValidator<InvalidUsername, String> {

    private final UserRepository userRepository;

    public InvalidUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.userRepository.findByUsername(value).isPresent();
    }
}
