package com.authora.core.i18n

object AuthoraStringsCatalog {
    val english = AuthoraStrings(
        signInTitle = "Sign In",
        signInButton = "Sign In",
        signInNoAccount = "Don't have an account? Sign Up",

        signUpTitle = "Sign Up",
        signUpButton = "Sign Up",
        signUpHaveAccount = "Already have an account? Sign In",

        emailLabel = "Email",
        passwordLabel = "Password",
        confirmPasswordLabel = "Confirm Password",
        fullNameLabel = "Full Name",

        accountManageAccounts = "Manage accounts",
        accountSignOut = "Sign Out",

        accountSelectorTitle = "Switch Account",
        accountSelectorAddAccount = "Add account",

        mfaTitle = "Verify Your Identity",
        mfaDescriptionSmsTemplate = "Enter the code we sent to %s",
        mfaDescriptionEmailTemplate = "Enter the code we sent to %s",
        mfaDescriptionTotp = "Enter the code from your authenticator app",
        mfaCodeLabel = "Verification Code",
        mfaVerifyButton = "Verify",
        mfaResendButton = "Resend Code",
        mfaResending = "Resending...",
        mfaCodeRequired = "Verification code is required",
        mfaResendSuccess = "A new verification code was sent.",
        mfaResendFailure = "Could not resend the code. Please try again.",
        mfaIncorrectCode = "Verification code was incorrect. Please try again.",

        passwordShow = "Show password",
        passwordHide = "Hide password",

        validationRequiredTemplate = "%s is required",
        validationEmailInvalid = "Enter a valid email address",
        validationPasswordTooShort = "Password must be at least 8 characters",
        validationPasswordNeedsDigit = "Password must contain at least one number",
        validationPasswordNeedsLetter = "Password must contain at least one letter",
        validationConfirmPasswordRequired = "Please confirm your password",
        validationConfirmPasswordMismatch = "Passwords do not match",
        validationFullNameTooShort = "Full name is too short",
        validationPhoneInvalid = "Enter a valid phone number",
        validationUrlInvalid = "Enter a valid URL",
        validationUsernameInvalid = "Username must be 3-20 characters (letters, numbers, underscore)",

        errorSignInFailed = "Sign in failed. Please try again.",
        errorSignUpFailed = "Sign up failed. Please try again.",
        errorGeneric = "Something went wrong. Please try again."
    )

    val indonesian = AuthoraStrings(
        signInTitle = "Masuk",
        signInButton = "Masuk",
        signInNoAccount = "Belum punya akun? Daftar",

        signUpTitle = "Daftar",
        signUpButton = "Daftar",
        signUpHaveAccount = "Sudah punya akun? Masuk",

        emailLabel = "Email",
        passwordLabel = "Kata Sandi",
        confirmPasswordLabel = "Konfirmasi Kata Sandi",
        fullNameLabel = "Nama Lengkap",

        accountManageAccounts = "Kelola akun",
        accountSignOut = "Keluar",

        accountSelectorTitle = "Ganti Akun",
        accountSelectorAddAccount = "Tambah akun",

        mfaTitle = "Verifikasi Identitas Anda",
        mfaDescriptionSmsTemplate = "Masukkan kode yang kami kirim ke %s",
        mfaDescriptionEmailTemplate = "Masukkan kode yang kami kirim ke %s",
        mfaDescriptionTotp = "Masukkan kode dari aplikasi authenticator Anda",
        mfaCodeLabel = "Kode Verifikasi",
        mfaVerifyButton = "Verifikasi",
        mfaResendButton = "Kirim Ulang Kode",
        mfaResending = "Mengirim ulang...",
        mfaCodeRequired = "Kode verifikasi wajib diisi",
        mfaResendSuccess = "Kode verifikasi baru telah dikirim.",
        mfaResendFailure = "Gagal mengirim ulang kode. Silakan coba lagi.",
        mfaIncorrectCode = "Kode verifikasi salah. Silakan coba lagi.",

        passwordShow = "Tampilkan kata sandi",
        passwordHide = "Sembunyikan kata sandi",

        validationRequiredTemplate = "%s wajib diisi",
        validationEmailInvalid = "Masukkan alamat email yang valid",
        validationPasswordTooShort = "Kata sandi minimal 8 karakter",
        validationPasswordNeedsDigit = "Kata sandi harus mengandung minimal satu angka",
        validationPasswordNeedsLetter = "Kata sandi harus mengandung minimal satu huruf",
        validationConfirmPasswordRequired = "Silakan konfirmasi kata sandi Anda",
        validationConfirmPasswordMismatch = "Kata sandi tidak cocok",
        validationFullNameTooShort = "Nama lengkap terlalu pendek",
        validationPhoneInvalid = "Masukkan nomor telepon yang valid",
        validationUrlInvalid = "Masukkan URL yang valid",
        validationUsernameInvalid = "Username harus 3-20 karakter (huruf, angka, garis bawah)",

        errorSignInFailed = "Gagal masuk. Silakan coba lagi.",
        errorSignUpFailed = "Gagal mendaftar. Silakan coba lagi.",
        errorGeneric = "Terjadi kesalahan. Silakan coba lagi."
    )

    private val byLanguageCode = mapOf(
        "en" to english,
        "id" to indonesian
    )

    fun forLanguageCode(code: String): AuthoraStrings? = byLanguageCode[code.lowercase()]

    fun supportedLanguageCodes(): Set<String> = byLanguageCode.keys
}