## Authora

«Build apps. Not authentication.»

Authora is an opinionated authentication framework for Android.

Built to make authentication simple, consistent, and developer-friendly.

Authora provides authentication UI, provider integration, session management, multi-factor authentication, configurable fields, and Material 3 components through one consistent framework.

---

## Philosophy

Authora is opinionated.

We don't give developers dozens of ways to configure authentication.

We give them one consistent way.

- One dependency.
- One configuration.
- One way.

Don't give developers more choices. Give them the right choices.

---

## Features

- Material 3 authentication UI
- Sign In
- Sign Up
- Account management
- Account selector
- Profile
- Session management
- Multiple accounts
- Multi-Factor Authentication
- Firebase integration
- Supabase integration
- Custom fields
- Multi-language support
- Configurable themes
- UI preview
- Developer inspector
- Authora Playground
- Gradle integration
- Centralized "Authora.toml" configuration

---

## Installation

Add Authora to your application:

```kotlin
implementation("com.authora:authora:<version>")
```

Then configure your application using:

Authora.toml

Authora handles the rest.

---

## Configuration

All Authora configuration belongs in "Authora.toml".

Example:

```Toml
[auth.provider]

type = "firebase"

managed_config = true
config_name = "default"

[ui]

app_name = "MyApp"
app_icon = "auto"

[ui.theme]

dynamic_color = true
dark_mode = "system"
```

Authora keeps configuration centralized and predictable.

---

## Providers

Authora currently focuses on:

- Firebase
- Supabase

Provider setup is handled through the Authora Gradle Plugin.

Firebase

```code
./gradlew authoraAddFirebase
```

Supabase

```code
./gradlew authoraAddSupabase
```

Authora is designed with a provider abstraction so additional providers can be added in the future.

---

## Authentication UI

Authora provides Material 3 authentication components.

The framework handles:

- Sign In
- Sign Up
- Account
- Account Selector
- Profile
- Session
- Multi-Factor Authentication

Authentication UI is designed to remain consistent across applications using Authora.

---

## Custom Fields

Authora supports configurable authentication and profile fields.

Examples include:

- Username
- Email
- Password
- Confirm Password
- Phone
- Full Name
- Display Name
- Bio
- Website
- Country
- GitHub
- Microsoft
- Discord
- Telegram
- LinkedIn

Fields can be configured through "Authora.toml".

---

## Multi-Factor Authentication

MFA is handled as an independent authentication flow.

Supported methods can include:

- Email verification
- SMS verification
- TOTP
- Other provider-supported second factors

MFA is intentionally separated from the main Sign In and Sign Up UI.

---

## Opinionated by Design

Authora intentionally follows strict conventions.

Configuration belongs in "Authora.toml".

Providers are configured through the Authora Gradle Plugin.

Authentication UI uses Authora components.

Invalid configuration should be detected early.

The goal is simple:

«Less configuration.

Less confusion.

More building.»

---

## Project Status

Authora is currently under active development.

The API and configuration format may change before the first stable release.

---

## Organization

Authora is developed under the "authora-org" GitHub organization.

Main repository:

authora-org/authora

Documentation:

authora-org/authora.github.io

---

## Contributing

Contributions, ideas, bug reports, and discussions are welcome.

Please read the contribution guidelines before submitting a Pull Request.

---

## License

Authora is open source.

See the [LICENSE](./LICENSE) file for details.

---

<p align="center">Authora

Build apps. Not authentication.

</p>