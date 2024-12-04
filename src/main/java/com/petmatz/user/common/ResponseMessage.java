package com.petmatz.user.common;

public interface ResponseMessage {
    String SUCCESS = "Success";

    String VALIDATION_FAIL = "Validation failed";

    String DUPLICATE_ID = "Duplicate ID";

    String SIGN_IN_FAIL = "Login information mismatch";

    String CERTIFICATION_FAIL = "Certification failed";

    String MAIL_FAIL = "Failed to send email";

    String DATABASE_ERROR = "Database error";

    String ID_NOT_FOUND = "ID Not found";

    String WRONG_ROLE = "Wrong role.";

    String WRONG_PASSWORD="Wrong password.";

    String EDIT_FAIL = "Edit profile failed";

    String HEARTED_ID_NOT_FOUND = "Hearted Id Not Found";

    String WRONG_LOCATION= "Wrong location";

    String USER_NOT_FOUND = "User Not Found";

    String LOCATION_FAIL = "Location Fail"; // LF

    String INVALID_PASSWORD = "Invalid Password"; // IP

    String MISSING_FIELDS = "Missing Fields"; // MF

    String UNKNOWN_ERROR = "Unknown Error"; // UE
}
