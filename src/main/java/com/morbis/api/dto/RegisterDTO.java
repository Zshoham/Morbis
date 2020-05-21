package com.morbis.api.dto;

import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.util.HashMap;

@Schema(name = "Fan Registration")
@AllArgsConstructor
public class RegisterDTO {

    @Schema(example = "jane",
            required = true,
            minLength = 3,
            type = "string",
            pattern = "[a-zA-Z][a-zA-Z0-9_.@]{3,}")
    public String username;

    @Schema(example = "pAssw0rD",
            required = true,
            minLength = 8,
            maxLength = 20,
            type = "string",
            pattern = "(.*[a-z].*) &&" +
                      "(.*[A-Z].*) &&" +
                      "(.*[0-9].*) &&" +
                      "(.{8,20})")
    public String password;

    @Schema(example = "Jane Doe",
            required = true,
            minLength = 3,
            type = "string",
            pattern = "( ?[a-zA-Z]{3,})+")
    public String name;

    @Schema(example = "Jane@gmail.com",
            required = true,
            format = "email")
    public String email;


    public Member asMember () {
        if(!validateRegistration())
            throw new IllegalArgumentException("invalid data in member");
        return Fan.newFan()
                .fromMember(username, password, name, email)
                .build();
    }

    private boolean validateRegistration(){
        return validatePassword() &&
               validateUsername() &&
               validateEmail() &&
               validateName();
    }
    /**
     * <b>Validate user's name by the following rules:</b>
     * <ul>
     *     <li>only alphabetic characters</li>
     *     <li>at least 3 letters</li>
     *     <li>name can contain spaces following by more names</li>
     * </ul>
     * @return true if the name is valid
     */
    private boolean validateName(){
        String nameRule = "( ?[a-zA-Z]{3,})+";
        return name.matches(nameRule);
    }
    /**
     * <b>Validate user's email by the following rules:</b>
     * <ul>
     *     <li>legitimate email address</li>
     * </ul>
     * @return true if the email is valid
     */
    private boolean validateEmail(){
        String emailRule = "\\w+([.-]?\\w+)@\\w+([.-]?\\w+)(.\\w{2,3})+";
        return email.matches(emailRule);
    }
    /**
     * <b>Validate user's username by the following rules:</b>
     * <ul>
     *     <li>first char- alphabetic letter</li>
     *     <li>after- can contain numbers, upper/lower letters/some special characters</li>
     *     <li>only valid special characters- '_', '.', '@'</li>
     *     <li>at least 3 characters</li>
     * </ul>
     * @return true if the username is valid
     */
    private boolean validateUsername(){
        String usernameRule = "[a-zA-Z][a-zA-Z0-9_.@]{3,}";
        return username.matches(usernameRule);
    }
    /**
     * <b>Validate user's password by the following rules:</b>
     * <ul>
     *      <li>length- 8 to 20 characters</li>
     *      <li>must contain a digit</li>
     *      <li>must contain upper case letter</li>
     *      <li>must contain lower case letter</li>
     * </ul>
     * @return true if the name is valid
     */
    private boolean validatePassword(){
        String passwordLengthRule = ".{8,20}";
        String passwordDigitsRule = ".*\\d.*";
        String passwordLowerLetterRule = ".*[a-z].*";
        String passwordCapitalLettersRule = ".*[A-Z].*";

        return password.matches(passwordCapitalLettersRule) &&
               password.matches(passwordLowerLetterRule) &&
               password.matches(passwordDigitsRule) &&
               password.matches(passwordLengthRule);
    }


    public static void main(String[] args) {
        String password = "asas32aA3";
        String username = "aHodtw_.it34o";
        String email = "a1@23sdasdgmail.com.as.aa";
        String name = "maerio hhg";

        HashMap<String, Boolean> test = new HashMap<String, Boolean>();

        String passwordLengthRule = ".{8,20}";
        test.put("pass length", password.matches(passwordLengthRule));
        String passwordDigitsRule = ".*\\d.*";
        test.put("pass contains digit", password.matches(passwordDigitsRule));
        String passwordLowerLetterRule = ".*[a-z].*";
        test.put("pass cotains lower case", password.matches(passwordLowerLetterRule));
        String passwordCapitalLettersRule = ".*[A-Z].*";
        test.put("pass contains upper case", password.matches(passwordCapitalLettersRule));
        String passwordAlphanumericRule = "^.*[^a-zA-Z0-9 ].*$";
        test.put("passLength not contains non alphanumeric chars", !password.matches(passwordAlphanumericRule));

//        test.put("valid password", validatePassword(password));
//        test.put("valid email", validateEmail(email));
//        test.put("valid name", validateName(name));
//        test.put("valid username", validateUsername(username));
//        test.put("TOTAL valid registration", validateRegistration(password, username, email, name));
    }
}