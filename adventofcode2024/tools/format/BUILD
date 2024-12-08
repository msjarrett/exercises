load("@aspect_rules_lint//format:defs.bzl", "format_multirun")
load("@rules_java//java:defs.bzl", "java_binary")

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
    runtime_deps = ["@ktfmt//jar"],
)
