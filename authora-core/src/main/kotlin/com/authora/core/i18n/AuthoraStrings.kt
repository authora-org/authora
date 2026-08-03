package com.authora.core.i18n

data class AuthoraStrings(
    val signInTitle: String,
    val signInButton: String,
    val signInNoAccount: String,

    val signUpTitle: String,
    val signUpButton: String,
    val signUpHaveAccount: String,

    val emailLabel: String,
    val passwordLabel: String,
    val confirmPasswordLabel: String,
    val fullNameLabel: String,

    val accountManageAccounts: String,
    val accountSignOut: String,

    val accountSelectorTitle: String,
    val accountSelectorAddAccount: String,

    val mfaTitle: String,
    val mfaDescriptionSmsTemplate: String,
    val mfaDescriptionEmailTemplate: String,
    val mfaDescriptionTotp: String,
    val mfaCodeLabel: String,
    val mfaVerifyButton: String,
    val mfaResendButton: String,
    val mfaResending: String,
    val mfaCodeRequired: String,
    val mfaResendSuccess: String,
    val mfaResendFailure: String,
    val mfaIncorrectCode: String,

    val passwordShow: String,
    val passwordHide: String,

    val validationRequiredTemplate: String,
    val validationEmailInvalid: String,
    val validationPasswordTooShort: String,
    val validationPasswordNeedsDigit: String,
    val validationPasswordNeedsLetter: String,
    val validationConfirmPasswordRequired: String,
    val validationConfirmPasswordMismatch: String,
    val validationFullNameTooShort: String,
    val validationPhoneInvalid: String,
    val validationUrlInvalid: String,
    val validationUsernameInvalid: String,

    val errorSignInFailed: String,
    val errorSignUpFailed: String,
    val errorGeneric: String
)