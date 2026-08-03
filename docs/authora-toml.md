# Authora.toml

This is the single source of truth for your app's authentication setup. Every setting Authora needs lives here — nothing is configured in `build.gradle.kts` or in Kotlin code.

## `[authora]`

```toml
[authora]
version = "0.1.0"
language = "auto"
```

|Key | Type | Description|
|---|---|---|
|version | string | The Authora runtime version this project uses. Managed by
authoraUpgrade.|
|language | string | UI language. "auto" follows the device locale, or a
language code such as "id".|

## `[auth.provider]`

Firebase:

```Toml
[auth.provider]
type = "firebase"
managed_config = true
config_name = "default"
```

Supabase:

```Toml
[auth.provider]
type = "supabase"
url = "https://your-project.supabase.co"
anon_key = "your-anon-key"
```

Managed by authoraAddFirebase / authoraAddSupabase — you normally never edit this section by hand.

## `[ui]` **and** `[ui.theme]`

```Toml
[ui]
app_name = "MyApp"
app_icon = "auto"

[ui.theme]
dynamic_color = true
dark_mode = "system"
```

|Key | Type | Description|
|---|---|
|ui.app_name | string | Shown on Sign In / Sign Up headers if your own layout uses it.|
|ui.app_icon | string | "auto" detects your launcher icon, or a drawable resource name.|
|ui.theme.dynamic_color | bool | Uses Material You dynamic color on Android 12+.|
|ui.theme.dark_mode | string | "system", "light", or "dark".|

## `[[fields]]`

Repeatable array-of-tables block declaring extra Sign Up fields. See [Custom Fields](./custom-fields.md).

```Toml
[[fields]]
name = "username"
required = true

[[fields]]
name = "phone"
required = false
```

### Full Example

```Toml
[authora]
version = "0.1.0"
language = "auto"

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

[[fields]]
name = "username"
required = true

[[fields]]
name = "phone"
required = false
```