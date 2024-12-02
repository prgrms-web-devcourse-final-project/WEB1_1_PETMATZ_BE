package com.petmatz.domain.user.provider;

import java.security.SecureRandom;

public class RePasswordProvider {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*";
    private static final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    public static String generatePassword() {
        SecureRandom random = new SecureRandom();

        // 랜덤 길이 선택 (8 ~ 20자)
        int passwordLength = random.nextInt(MAX_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH + 1) + MIN_PASSWORD_LENGTH;

        StringBuilder password = new StringBuilder();

        // 각 조건을 만족하는 문자 추가
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));  // 최소 1개의 소문자
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));  // 최소 1개의 대문자
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));        // 최소 1개의 숫자
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length()))); // 최소 1개의 특수문자

        // 나머지 문자 랜덤 추가
        for (int i = 4; i < passwordLength; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // 생성된 비밀번호를 셔플하여 순서 랜덤화
        return shufflePassword(password.toString());
    }

    private static String shufflePassword(String password) {
        char[] characters = password.toCharArray();
        SecureRandom random = new SecureRandom();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[index];
            characters[index] = temp;
        }
        return new String(characters);
    }
}