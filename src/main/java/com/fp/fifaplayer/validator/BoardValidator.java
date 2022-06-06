package com.fp.fifaplayer.validator;

import com.fp.fifaplayer.form.BoardForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BoardForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BoardForm boardForm = (BoardForm) target;



    }
}
