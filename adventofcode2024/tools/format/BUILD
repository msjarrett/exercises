load("@aspect_rules_lint//format:defs.bzl", "format_multirun")

format_multirun(
    name = "format",
    kotlin = ":ktfmt",
    starlark = "@buildifier_prebuilt//:buildifier",
    #markdown = ":prettifier",
    visibility = ["//visibility:public"],
)

java_binary(
    name = "ktfmt",
    main_class = "com.facebook.ktfmt.cli.Main",
    runtime_deps = ["@maven//:com_facebook_ktfmt"],
)
