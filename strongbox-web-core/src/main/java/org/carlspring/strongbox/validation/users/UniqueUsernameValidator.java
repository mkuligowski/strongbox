package org.carlspring.strongbox.validation.users;

import org.carlspring.strongbox.users.domain.User;
import org.carlspring.strongbox.users.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

/**
 * @author Pablo Tirado
 */
public class UniqueUsernameValidator
        implements ConstraintValidator<UniqueUsername, String>
{

    private UserService userService;

    public UniqueUsernameValidator(UserService userService)
    {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueUsername constraint)
    {
        // Empty method, not used.
    }

    @Override
    public boolean isValid(String username,
                           ConstraintValidatorContext context)
    {
        User user = userService.findByUserName(username);
        return StringUtils.isEmpty(username) || user == null;
    }

}
